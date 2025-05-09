#include <iostream>
#include <vector>

std::vector<double> recursiveMultiply(std::vector<std::vector<double>>& statesMatrix, std::vector<double>& initState, int recurse) {
    if(recurse == 0) {return initState;}
    int n = initState.size();
    std::vector<double> nextState(n, 0.0);

    for (int j = 0; j < n; ++j) {
        for (int i = 0; i < n; ++i) {
            nextState[j] += initState[i] * statesMatrix[i][j];
        }
    }

    return recursiveMultiply(statesMatrix, nextState, recurse - 1);
}


int main() {
                                              //RDY  RUN  BLK
    std::vector<std::vector<double>> states = {{0.8, 0.2, 0.0}, //RDY
                                               {0.1, 0.1, 0.8}, //RUN
                                               {0.2, 0.0, 0.8}};//BLK
    
    std::vector<double> startState = {1.0, 0.0, 0.0};

    int steps = 10000;

    std::vector<double> finalState = recursiveMultiply(states, startState, steps);
    double totalProbability = 0;

    std::cout << "State after " << steps << " steps: ";
    for(double probability : finalState) {
        std::cout << probability << " ";
        totalProbability += probability;
    };
    std::cout << "\n" << totalProbability << std::endl;

    return 0;
}