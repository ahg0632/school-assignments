#include <iostream>
#include <thread>
#include <vector>
#include <mutex>
#include <condition_variable>
#include <chrono>
#include <queue>
#include <atomic>
#include <random>
#include <memory>

//  Thread states
enum ThreadStatus {READY, RUNNING, WAITING, BLOCKED, FINISHED};

//  Simulate Thread control block
struct ThreadControlBlock {
    int id;
    int priority;
    ThreadStatus status;
    std::thread thread;
    int waiting_time;

    ThreadControlBlock(int id, int priority) : id(id), priority(priority), status(READY), waiting_time(0) {}
};

//  Comparator for priority queue
struct ThreadComparator {
    bool operator()(const std::shared_ptr<ThreadControlBlock>& a, const std::shared_ptr<ThreadControlBlock>& b) {
        return a->priority < b->priority;
    }
};

//  Global variables
std::vector<std::shared_ptr<ThreadControlBlock>> thread_pool;
std::mutex mtx;
std::condition_variable cv;
std::atomic<bool> scheduling_done(false);

//  Simulation of work
void simulate_work(int id, int work_units) {
    std::random_device rd;
    std::mt19937 gen(rd());
    std::uniform_int_distribution<> distr(1, 3);

    for(int i = 0; i < work_units; ++i) {
        std::this_thread::sleep_for(std::chrono::milliseconds(distr(gen) * 100));
    }
}

//  Simulate priority scheduling with aging
void priority_scheduler(int num_threads, int time_slice_ms, bool enable_aging = true) {
    while(!scheduling_done) {
        std::unique_lock<std::mutex> lock(mtx);

        //  Rebuild priority queue by current priorities
        std::priority_queue<std::shared_ptr<ThreadControlBlock>, std::vector<std::shared_ptr<ThreadControlBlock>>, ThreadComparator> pq;
        for(int i = 0; i < num_threads; ++i) {
            if(thread_pool[i]->status == READY || thread_pool[i]->status == WAITING) {
                pq.push(thread_pool[i]);
            }
        }

        if (!pq.empty()) {
            auto highest_priority_thread = pq.top();
            int id = highest_priority_thread->id;

            thread_pool[id]->status = RUNNING;
            std::cout << "Scheduler: Thread " << id << " with priority " << thread_pool[id]->priority << " is now RUNNING.\n";

            //  Simulate time slice
            cv.notify_all();
            cv.wait_for(lock, std::chrono::milliseconds(time_slice_ms));

            if(thread_pool[id]->status == RUNNING) {
                thread_pool[id]->status = READY;
            }

            std::cout << "Scheduler: Thread " << id << " is now READY.\n";

            if(enable_aging) {
                for(int i = 0; i < num_threads; ++i) {
                    if(thread_pool[i]->status == READY || thread_pool[i]->status == WAITING) {
                        thread_pool[i]->waiting_time += time_slice_ms;
                        if(thread_pool[i]->waiting_time >= 1000) {  //  1000 is 1 second
                            ++thread_pool[i]->priority;
                            thread_pool[i]->waiting_time = 0;       //  Reset waiting time
                            std::cout << "Thread " << i << " has aged and increased its priority to " 
                                      << thread_pool[i]->priority << std::endl;
                        }
                    }
                }
            }
        }
    }    
}

//  Thread function
void thread_function(int id, int work_units) {
    simulate_work(id, work_units);
    std::unique_lock<std::mutex> lock(mtx);

    //  Work has finished
    thread_pool[id]->status = FINISHED;
    std::cout << "Thread " << id << " has finished its work and is now FINISHED.\n";
    cv.notify_all();
}

//  Create and launch threads
void launch_threads(int num_threads, int work_units, std::vector<int> priorities) {
    for(int i = 0; i < num_threads; ++i) {
        thread_pool.emplace_back(std::make_shared<ThreadControlBlock>(i, priorities[i]));
        thread_pool[i]->thread = std::thread(thread_function, i, work_units);
    }
}

void wait_for_threads(int num_threads) {
    for(int i = 0; i < num_threads; ++i) {
        if(thread_pool[i]->thread.joinable()) {
            thread_pool[i]->thread.join();
        }
    }
    scheduling_done = true;
    cv.notify_all();
}


int main() {
    const int num_threads = 4;
    const int time_slice_ms = 500;
    const int work_units = 5;
    std::vector<int> priorities = {2, 5, 1, 4}; //  initial priorities

    std::cout << "Starting threads with priority scheduling...\n";

    launch_threads(num_threads, work_units, priorities);

    std::thread scheduler(priority_scheduler, num_threads, time_slice_ms, true);

    wait_for_threads(num_threads);

    scheduler.join();

    std::cout << "All threads finished, exiting..." << std::endl;

    return 0;
}