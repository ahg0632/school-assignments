#include <iostream>

void largerThanInput(int [], const int, int);

int main() {
    const int SIZE = 5;
    int values[SIZE];
    int n;

    std::cout << "Enter a value to compare against: ";
    std::cin >> n;

    for(int i = 0; i < SIZE; ++i) {
        std::cout << "Enter number: ";
        std::cin >> values[i];
    };

    largerThanInput(values, SIZE, n);
}

void largerThanInput(int values[], const int SIZE, int n) {
    std::cout << "Values larger than the comparator: " << std::endl;
    for(int i = 0; i < SIZE; ++i) {
        if (values[i] > n)
            std::cout << values[i] << " ";
    };
}