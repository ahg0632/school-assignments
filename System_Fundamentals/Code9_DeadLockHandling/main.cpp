#include <iostream>
#include <thread>
#include <mutex>
#include <condition_variable>
#include <vector>
#include <string>
#include <unordered_map>
#include <unordered_set>
#include <algorithm>
#include <random>
#include <chrono>


std::mutex coutMutex;

//  Deadlock Detector Class
class DeadlockDetector {
    private:
        std::unordered_map<std::string, std::vector<std::string>> waitForGraph;

        bool dfs(std::string node, std::unordered_set<std::string>& visited, std::unordered_set<std::string>& recStack) {
            visited.insert(node);
            recStack.insert(node);

            for(const std::string& neighbor : waitForGraph[node]) {
                if(recStack.count(neighbor)) return true;
                if(!visited.count(neighbor) && dfs(neighbor, visited, recStack)) return true;
            }

            recStack.erase(node);
            return false;
        }
    public:
        void addEdge(const std::string& from, const std::string& to) {
            waitForGraph[from].push_back(to);
        }

        void removeEdge(const std::string& from, const std::string& to) {
            if(waitForGraph.count(from)) {
                auto& vec = waitForGraph[from];
                vec.erase(remove(vec.begin(), vec.end(), to), vec.end());
            }
        }

        void clearGraph() {
            waitForGraph.clear();
        }

        void printedGraph() {
            {std::cout << "Wait-For Graph:\n";}
            
            for(auto& pair : waitForGraph) {
                {std::lock_guard<std::mutex> lock(coutMutex);
                std::cout << " " << pair.first << " -> ";}
                for(const auto& dest : pair.second) {
                    {std::lock_guard<std::mutex> lock(coutMutex);
                    std::cout << dest << " ";}
                }
                {std::cout << "\n";}
            }
        }

        bool detectedDeadlock() {
            std::unordered_set<std::string> visited, recStack;
            for(const auto& node : waitForGraph) {
                if(!visited.count(node.first)) {
                    if(dfs(node.first, visited, recStack)) return true;
                }
            }
            return false;
        }

        std::string recoverFromDeadlock() {
            if(!waitForGraph.empty()) {
                std::string toTerminate = waitForGraph.begin()->first;
                {std::cout << "[Recovery] Terminating " << toTerminate << " to resolve deadlock.\n";}
                waitForGraph.erase(toTerminate); 
                for(auto& [_, neighbors] : waitForGraph) {
                    neighbors.erase(remove(neighbors.begin(), neighbors.end(), toTerminate), neighbors.end());
                }
                return toTerminate;
            }
            return "";
        }
};

//  Semaphore & Resource Class
class Semaphore {
    private:
        int count;
        std::mutex mtx;
        std::condition_variable cv;
    public:
        Semaphore(int value) : count(value) {}

        void wait() {
            std::unique_lock<std::mutex> lock(mtx);
            cv.wait(lock, [&]() {return count > 0;});
            --count;
        }

        void signal() {
            std::unique_lock<std::mutex> lock(mtx);
            ++count;
            cv.notify_one();
        }

        bool tryWait() {
            std::unique_lock<std::mutex> lock(mtx);
            if(count > 0) {
                --count;
                return true;
            }
            return false;
        }
};

class Resource {
    private:
        std::string name;
        std::string owner;
        Semaphore sem;
        DeadlockDetector* detector;
    public:
        Resource(std::string name, DeadlockDetector* detector = nullptr)
            : name(name), sem(1), owner(""), detector(detector){}
        
        void acquire(const std::string& threadName) {
            {std::lock_guard<std::mutex> lock(coutMutex);
            std::cout << threadName << " requesting " << name << std::endl;}
            if(!sem.tryWait()) {
                if(detector && !owner.empty()) {
                    detector->addEdge(threadName, owner);
                    if(detector->detectedDeadlock()) {
                        {std::lock_guard<std::mutex> lock(coutMutex);
                        std::cout << "[Deadlock Detected] while " << threadName << " waiting for " << name << "\n";}
                        detector->printedGraph();
                        if(detector->recoverFromDeadlock() == threadName) {
                            detector->removeEdge(threadName, owner);
                            return;
                        }
                    }
                }
                sem.wait();
            }

            owner = threadName;

            if(detector) {
                detector->removeEdge(threadName, owner);
            }
            std::cout << threadName << " acquired " << name << "\n";
        }

        void release(const std::string& threadName) {
            std::cout << threadName << " releasing " << name << "\n";
            owner = "";
            sem.signal();
        }

        std::string getName() const {return name;}
};


//  Process Class
class Process {
    private:
        int pid;
        std::thread t;
        std::vector<Resource*> neededResources;

        void run() {
            std::string threadName = "Process_" + std::to_string(pid);

            for(Resource* res : neededResources) {
                std::this_thread::sleep_for(std::chrono::milliseconds(rand() % 100));
                res->acquire(threadName);
            }

            std::this_thread::sleep_for(std::chrono::milliseconds(200));

            for(Resource* res : neededResources) {
                res->release(threadName);
            }

            {std::lock_guard<std::mutex> lock(coutMutex);
            std::cout << threadName << " finished." << "\n";}
        }
    public:
        Process(int id, std::vector<Resource*> resources) : pid(id), neededResources(resources) {
            t = std::thread(&Process::run, this);
        }

        void join() {
            if(t.joinable()) {
                t.join();
            }
        }
};


//  Main Section
int main() {
    srand(time(0));
    std::cout << "--- SORM Project: Semaphore, Deadlock & Multi-thread Simulation ---\n";

    DeadlockDetector detector;

    Resource printer("Printer", &detector);
    Resource disk("disk", &detector);
    Resource network("Network", &detector);

    Process p1(1, {&printer, &disk});
    Process p2(2, {&disk, &network});
    Process p3(3, {&network, &printer});

    p1.join();
    p2.join();
    p3.join();

    std::cout << "\n";
    std::cout << "--- Simulation Completed ---" << std::endl;
    
    return 0;
}