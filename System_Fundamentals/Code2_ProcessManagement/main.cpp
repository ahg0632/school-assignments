//  CS3360_Code2_Process
//  Hector Guzman
//  ole10

#include <iostream>
#include <thread>
#include <vector>
#include <chrono>
#include <mutex>
#include <memory>

//  classes and data types/states
enum ProcessStatus {READY, RUNNING, WAITING, BLOCKING, TERMINATED};

class Process {
    private:
        int processID;
        ProcessStatus state;    //  enum data type
        int cpuUsage;           //  simulate CPU
        int resourceUsage;      //  simulate resource
        std::mutex mtx;         //  avoid conflicts between processes for shared data
    public:
        //  Disable copy constructor/assignment (Process can not be copyable)
        Process(const Process&) = delete;
        Process& operator = (const Process&) = delete;

        Process(int id) : processID(id), state(READY), cpuUsage(0), resourceUsage(0) {}     //  constructor
        Process(Process&& other) noexcept :                                                 //  constructor for C++ 11
            processID(other.processID), state(other.state), 
            cpuUsage(other.cpuUsage), resourceUsage(other.resourceUsage) {}
        Process& operator = (Process&& other) noexcept {                                    //  assignment operator for C++ 11
            if(this != &other) {
                processID = other.processID;
                state = other.state;
                cpuUsage = other.cpuUsage;
                resourceUsage = other.resourceUsage;
            }
            return *this;
        }

        ProcessStatus getstate() const{return state;}       //  get current state

        void setState(ProcessStatus newState) {             //  set state
            {
            std::lock_guard<std::mutex> lock(mtx);          //  protect shared data
            state = newState;
            std::cout << "Process " << processID << " state changed to: " << getStateAsString() <<"\n";
            }
        }

        int getProcessID() const{                           //  get process ID
            return processID;
        }

        void run() {                                        //  set state to running
            setState(RUNNING);
            for(int i = 0; i < 5; i++) {                    //  simulate CPU usage
                {
                    std::this_thread::sleep_for(std::chrono::microseconds(500));
                    std::lock_guard<std::mutex> lock(mtx);  //  protect shared data
                    cpuUsage += 10;
                    std::cout << "Process " << processID << " is running. CPU usage: " << cpuUsage << "% \n";
                }
            }

            setState(WAITING);                              //  simulate waiting
            std::this_thread::sleep_for(std::chrono::milliseconds(1000));

            setState(RUNNING);                              //  simulate running
            std::this_thread::sleep_for(std::chrono::milliseconds(1000));

            terminated();                                   // simulate terminated

        }

        void terminated() {                                  //  simlulate termination
            {
            setState(TERMINATED);
            std::cout << "Process" << processID << " Terminated\n";
            }
        }

        std::string getStateAsString() const{                    //  return process as string
            switch(state) {
                case READY: return "READY";
                case RUNNING: return "RUNNING";
                case WAITING: return "WAITING";
                case BLOCKING: return "BLOCKING";
                case TERMINATED: return "TERMINATED";
                default: return "UNKNOWN";
            }
        }
};

//  separate thread management for each process
class ProcessThreadManager {
    private:
        std::thread processThread;
    public:
        void runProcess(Process& process) {                         //  attach process to thread
            processThread = std::thread(&Process::run, &process);
        }
        void join() {
            if(processThread.joinable()) {
                processThread.join();
            }
        }
};

class ProcessManager {
    private:
        std::vector<std::unique_ptr<Process>> processes;        //  list of processes
        std::vector<ProcessThreadManager> threadManager;        //  manage threads for processes
        std::mutex processMutex;                                //  mutex for process management
    public:
        void createProcess() {
            {
            std::lock_guard<std::mutex> lock(processMutex);
            int id = processes.size() + 1;
            processes.push_back(std::make_unique<Process>(id));
            threadManager.emplace_back();                       //  add thread manager for new process
            threadManager.back().runProcess(*processes.back()); //  run process in new thread
            std::cout << "Process " << id << " created by user.\n";
            }
        }
        
        void createProcessChild(int parentID) {
            {
            std::lock_guard<std::mutex> lock(processMutex);
            int id = processes.size() + 1;
            processes.push_back(std::make_unique<Process>(id));
            threadManager.emplace_back();
            threadManager.back().runProcess(*processes.back());
            std::cout << "Process " << id << " created by " << parentID << ".\n";
            }
        }

        void createProcessKernel() {
            {
            std::lock_guard<std::mutex> lock(processMutex);
            int id = processes.size() + 1;
            processes.push_back(std::make_unique<Process>(id));
            threadManager.emplace_back();
            threadManager.back().runProcess(*processes.back());
            std::cout << "Process " << id << " created by kernel.\n";
            }
        }

        void waitForAllProcess() {
            for(auto& manager: threadManager) {
                manager.join();
            }
        }

        void showProcessStatus() {
            std::lock_guard<std::mutex> lock(processMutex);
            for(const auto& process: processes) {
                std::cout << "Process " << process->getProcessID() << ": " << process->getStateAsString() << "\n";
            }
            std::cout << std::endl;
        }
};


int main() {
    ProcessManager pm;

    //  user created processes
    pm.createProcess();

    //  Kernel created processes
    pm.createProcessKernel();

    //  A process creating child processes
    pm.createProcessChild(1);

    //  wait for proccesses to finish
    pm.waitForAllProcess();

    //  display status of processes
    pm.showProcessStatus();

    return 0;
}