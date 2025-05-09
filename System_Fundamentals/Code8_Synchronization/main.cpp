#include <iostream>
#include <thread>
#include <mutex>
#include <condition_variable>
#include <vector>
#include <semaphore.h>
#include <chrono>
#include <random>

const int NUM_MOTORS = 2;
const int NUM_PROCESSES = 5;

//  Semaphore to control access to motor
sem_t motorSemaphores[NUM_MOTORS];

//  Random delay
int random_delay(int min_ms = 500, int max_mx = 1500) {
    static thread_local std::mt19937 rng(std::random_device{}());
    std::uniform_int_distribution<int> dist(min_ms, max_mx);
    return dist(rng);
}

//  Monitor class (Encapsulation)
class MotorMonitor {
    private:
        std::mutex mtx;
        std::condition_variable cv;
        bool motorBusy[NUM_MOTORS] = {false};
    public:
        int requestMotor(int processId) {
            std::unique_lock<std::mutex> lock(mtx);
            std::cout << "[Process " << processId << "] Requesting motor...\n";

            cv.wait(lock, [&]() {
                for (bool busy : motorBusy) {
                    if(!busy) return true;
                }
                return false;
            });

            for(int i = 0; i < NUM_MOTORS; ++i) {
                if(!motorBusy[i]) {
                    motorBusy[i] = true;
                    std::cout << "[Process " << processId << "] Woke up and acquired Motor " << i << "\n";
                    return i;
                }
            }
            return -1;  //  Never happen
        }

        void releaseMotor(int motorId, int processId) {
            std::unique_lock<std::mutex> lock(mtx);
            motorBusy[motorId] = false;
            std::cout << "[Process " << processId << "] Released Motor " << motorId << "\n";
            cv.notify_all();
        }
};

MotorMonitor monitor;

//  Simulate Process
void processFunction(int processId) {
    while(true) {
        //  Simulate thinking
        std::this_thread::sleep_for(std::chrono::milliseconds(random_delay()));
        
        //  Try to get a motor
        int motorId = monitor.requestMotor(processId);

        //  Acquire Semaphore
        sem_wait(&motorSemaphores[motorId]);

        //  Simulate motor use
        std::cout << "[Process " << processId << "] Using Motor " << motorId << "\n";
        std::this_thread::sleep_for(std::chrono::milliseconds(random_delay(1000, 2000)));

        //  Release motor acess
        sem_post(&motorSemaphores[motorId]);
        monitor.releaseMotor(motorId, processId);

        //  Optional cool-down period
        std::this_thread::sleep_for(std::chrono::milliseconds(random_delay(1000, 1500)));
    }
}


int main() {
    //  Initialize semaphores
    for(int i = 0; i < NUM_MOTORS; ++i) {
        sem_init(&motorSemaphores[i], 0, 1);
    }

    //  Launch processes
    std::vector<std::thread> processes;
    for(int i = 0; i < NUM_PROCESSES; ++i) {
        processes.emplace_back(processFunction, i);
    }

    //  Join threads
    for(auto& t : processes) {
        t.join();
    }

    //  Cleanup
    for (int i = 0; i < NUM_MOTORS; ++i) {
        sem_destroy(&motorSemaphores[i]);
    }

    return 0;
}