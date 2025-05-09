


#include <iostream>
#include <queue>
#include <cstdlib>
#include <ctime>
#include <cmath>


//  Function to generate random random base on Poisson Disstribution
double generateRandomTime(double rate) {
    double random = ((double) rand() / (RAND_MAX));
    return -log(1 - random) / rate;
}

//  Event class for Arrival or Departure
class Event {
    public:
        double time;
        bool isArrival;

        Event(double t, bool arrival) : time(t), isArrival(arrival) {}
};

//  Comparator for Priority Queue to sort by time
class CompareEvent {
    public:
        bool operator() (Event const& e1, Event const& e2) {
            return e1.time > e2.time;
        }
};

int main() {
    
    //  Seed random number generator
    srand(time(0));

    //  User input
    double arrivalRate, serviceRate, simulationTime;

    std::cout << "Enter arrival rate (tasks per unit time): ";
    std::cin >> arrivalRate;
    std::cout << "Enter service rate (tasks per unit time): ";
    std::cin >> serviceRate;
    std::cout << "Enter total simulation time: ";
    std::cin >> simulationTime;

    double currentTime = 0.0;

    std::priority_queue<Event, std::vector<Event>, CompareEvent> eventQueue;
    std::queue<double> taskQueue;

    double totalTask = 0;
    double totalWaitingTime = 0;
    double taskServed = 0;

    //  Initialize first Arrival Event
    double nextArrivalTime = generateRandomTime(arrivalRate);
    eventQueue.push(Event(nextArrivalTime, true));

    //  Start Simulation
    while(currentTime < simulationTime) {

        //  Get next Event
        Event currentEvent = eventQueue.top();
        eventQueue.pop();
        currentTime = currentEvent.time;

        if(currentEvent.isArrival) {
            //  Handle Arrival Event
            std::cout << "Arrival time: " << currentTime << std::endl;
            totalTask++;
            taskQueue.push(currentTime);

            //  Schedule Next Arrival
            nextArrivalTime = currentTime + generateRandomTime(arrivalRate);
            if(nextArrivalTime < simulationTime) {
                eventQueue.push(Event(nextArrivalTime, true));
            }

            //  If server is idle -> schedule departure
            nextArrivalTime = currentTime + generateRandomTime(arrivalRate);
            if(taskQueue.size() == 1) {
                double serviceTime = generateRandomTime(serviceRate);
                eventQueue.push(Event(currentTime + serviceTime, false));
            }
        } else {
            //  Handle Departure Event
            double arrivalTime = taskQueue.front();
            taskQueue.pop();
            taskServed++;
            double waitingTime = currentTime - arrivalTime;
            totalWaitingTime += waitingTime;

            std::cout << "Departure time: " << currentTime << " (Waiting Time: " << waitingTime << ")" << std::endl;

            //  If tasks exist in queue -> schedule next Departure
            if(!taskQueue.empty()) {
                double serviceTime = generateRandomTime(serviceRate);
                eventQueue.push(Event(currentTime + serviceTime, false));
            }
        }
    }

    //  Output results
    std::cout <<"\nSimulation Summary:" << std::endl;
    std::cout <<"Total tasks arrived: " << totalTask << std::endl;
    std::cout <<"Tasks served: " << taskServed << std::endl;
    std::cout <<"Average waiting time: " << totalWaitingTime / taskServed << std::endl;
    std::cout <<"Utilization: " << taskServed / totalTask << std::endl;

    return 0;
}