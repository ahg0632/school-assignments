


#include <iostream>
#include <set>
#include <vector>
#include <cmath>
#include <numeric>

// Calculate Probability of a Set
double probability(std::set<int>& event, std::set<int>& space) {
    return static_cast<double>(event.size()) / space.size();
}

// Set Union Operation
std::set<int> setUnion(std::set<int>& set1, std::set<int>& set2) {
    std::set<int> result = set1;
    result.insert(set2.begin(), set2.end());
    return result;
}

// Set Intersection Operation
std::set<int> setIntersection(std::set<int> set1, std::set<int>& set2) {
    std::set<int> result;
    for(int element : set1) {
        if(set2.find(element) != set2.end()) {
            result.insert(element);
        }
    }
    return result;
}

// Set Compliment Operation
std::set<int> setCompliment(std::set<int>& set1, std::set<int>& space) {
    std::set<int> result;
    for(int element : space) {
        if(set1.find(element) == set1.end()) {
            result.insert(element);
        }
    }
    return result;
}

// Expected Value Calculation
double expectedValue(std::vector<int>& rand_var, std::vector<double>& probs) {
    double expectedValue = 0.0;
    for(size_t i = 0; i < rand_var.size(); i++) {
        expectedValue += rand_var[i] * probs[i];
    }
    return expectedValue;
}

// Variance Calculation
double varianceValue(std::vector<int>& rand_var, std::vector<double>& probs) {
    double variance = 0.0;
    double expSquared = 0.0;
    for(size_t i = 0; i < rand_var.size(); i++) {
        expSquared += std::pow(rand_var[i], 2) * probs[i];
    }

    double eX = expectedValue(rand_var, probs);
    eX *= eX;

    variance = expSquared - eX;
    return variance;
}


int main() {
    // Sample Space S = {1-10}
    std::set<int> sample_space = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10};

    // Event Samples Set A & Set B
    std::set<int> A = {1, 2, 3};
    std::set<int> B = {2, 3, 4, 5, 6};

    // Calculate Probability of Events
    double Prob_A = probability(A, sample_space);
    double Prob_B = probability(B, sample_space);

    // Union of A & B
    std::set<int> A_union_B = setUnion(A, B);
    double Prob_A_union_B = probability(A_union_B, sample_space);

    // Intersection of A & B
    std::set<int> A_intersect_B = setIntersection(A, B);
    double Prob_A_intersect_B = probability(A_intersect_B, sample_space);

    // Compliment of A & B
    std::set<int> A_bar = setCompliment(A, sample_space);
    double Prob_A_bar = probability(A_bar, sample_space);

    // Display Probabilities
    std::cout << "P(A) = " << Prob_A << "\n"
              << "P(B) = " << Prob_B << "\n"
              << "P(A ∪ B) = " << Prob_A_union_B << "\n"
              << "P(A ∩ B) = " << Prob_A_intersect_B << "\n"
              << "P(!A) = " << Prob_A_bar << "\n"
              << std::endl;

    // Modeling a Random Variable X with Probabilities
    std::vector<int> random_variable = {3, 5, 6, 8, 9};
    std::vector<double> probabilities = {0.1, 0.2, 0.45, 0.56, 0.6};
    
    double Exp_X = expectedValue(random_variable, probabilities);
    std::cout << "E[x] = " << Exp_X << std::endl;

    double Var_X = varianceValue(random_variable, probabilities);
    std::cout << "Var(x) = " << Var_X << std::endl;


    return 0;
}
