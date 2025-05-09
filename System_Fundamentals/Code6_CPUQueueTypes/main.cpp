


#include <iostream>
#include <queue>
#include <vector>
#include <algorithm>


struct Process {
    int id;
    int arrival_time;
    int burst_time;
    int remaining_time;
    int completion_time;
    int waiting_time;
    int turnaround_time;
    bool finished = false;
};

//  FCS Scheduling
void FCFS(std::vector<Process>& processes) {
    int time = 0;
    for(auto& p : processes) {
        if (time < p.arrival_time) time = p.arrival_time;
        p.completion_time = time + p.burst_time;
        p.turnaround_time = p.completion_time - p.arrival_time;
        p.waiting_time = p.turnaround_time - p.burst_time;
        time += p.burst_time;
    }
}

//  SJF Scheduling (Non-preemptive)
void SJF(std::vector<Process>& processes) {
    int time = 0;
    int completed = 0;
    int n = processes.size();
    while(completed < n) {
        //  Find all processes that arrived at current time and not finished
        std::vector<Process*> ready_queue;
        for(auto& p : processes) {
            if(p.arrival_time <= time && !p.finished) ready_queue.push_back(&p);
        }

        if(!ready_queue.empty()) {
            //  Sort ready queue by burst time
            std::sort(ready_queue.begin(), ready_queue.end(), [](Process* p1, Process* p2) {
                return p1->burst_time < p2->burst_time;
            });

            //  Execute process with shortest burst time
            Process* p = ready_queue[0];
            time += p->burst_time;
            p->completion_time = time;
            p->turnaround_time = p->completion_time - p->arrival_time;
            p->waiting_time = p->turnaround_time - p->burst_time;
            p->finished = true;
            completed++;
        } else {
            time++;
        }
    }
}

//  RR Scheduling
void RR(std::vector<Process>& processes, int time_quantum) {
    std::queue<Process*> ready_queue;
    int time = 0;
    int completed = 0;
    int n = processes.size();

    //  Track process arrival
    int current_index = 0;
    while(completed < n) {
        //  Add processes that arrived by current time to ready queue
        while(current_index < n && processes[current_index].arrival_time <= time) {
            ready_queue.push(&processes[current_index]);
            current_index++;
        }

        if(!ready_queue.empty()) {
            Process* p = ready_queue.front();
            ready_queue.pop();

            if(p->remaining_time > time_quantum) {
                time += time_quantum;
                p->remaining_time -= time_quantum;
            } else {
                time += p->remaining_time;
                p->remaining_time = 0;
                p->completion_time = time;
                p->turnaround_time = p->completion_time - p->arrival_time;
                p->waiting_time = p->turnaround_time - p->burst_time;
                completed++;
            }

            //  After executing, re-add process if has remaining time
            if(p->remaining_time > 0) {
                ready_queue.push(p);
            }

            //  Add new process while executing current one
            while(current_index < n && processes[current_index].arrival_time <= time) {
                ready_queue.push(&processes[current_index]);
                current_index++;
            }
        } else {
            //  No process ready -> increase time
            time++;
        }
    }
}

//  Display Results
void displayResults(const std::vector<Process>& processes) {
    double total_waiting_time = 0;
    double total_turnaround_time = 0;
    std::cout << "Process\tArrival\tBurst\tWaiting\tTurnaround\n";
    for(const auto& p : processes) {
        std::cout << "P" << p.id << "\t" << p.arrival_time << "t" << p.burst_time << "\t"
                  << p.waiting_time << "\t" << p.turnaround_time << "\n";
        total_waiting_time += p.waiting_time;
        total_turnaround_time += p.turnaround_time;
    }
    std::cout << "Average waiting time: " << total_waiting_time / processes.size() << std::endl;
    std::cout << "Average turnaround time: " << total_turnaround_time / processes.size() << std::endl;
}

int main() {
    int n;
    std::cout << "Enter the number of processes: ";
    std::cin >> n;

    std::vector<Process> processes(n);
    std::cout << "Enter arrival time and burst time for each process:\n";
    
    for(int i = 0; i < n; ++i) {
        processes[i].id = i + 1;
        std::cout << "P" << i + 1 << ": ";
        std::cin >> processes[i].arrival_time >> processes[i].burst_time;
        processes[i].remaining_time = processes[i].burst_time;
    }

    std::vector<Process> processes_fcfs = processes;
    std::vector<Process> processes_sjf = processes;
    std::vector<Process> processes_rr = processes;

    //  FCFS
    std::cout << "\n--- FCFS Scheduling ---\n";
    FCFS(processes_fcfs);
    displayResults(processes_fcfs);

    //  SJF
    std::cout << "\n--- SJF Scheduling ---\n";
    SJF(processes_sjf);
    displayResults(processes_sjf);

    //  RR
    int time_quantum;
    std::cout << "\nEnter time quantum for Round Robin: ";
    std::cin >> time_quantum;
    std::cout << "\n--- RR Scheduling ---\n";
    RR(processes_rr, time_quantum);
    displayResults(processes_rr);

    return 0;
}