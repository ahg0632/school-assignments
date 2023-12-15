#include <iostream>

void userNums(int []);
void largestValue(int [] ,int&);
void smallestValue(int [] ,int&);

int main() {
    int values[10];
    int largestNum;
    int smallestNum;

    userNums(values);
    largestValue(values, largestNum);
    smallestValue(values, smallestNum);

    std::cout << "Largest number is: " << largestNum << std::endl;
    std::cout << "Smallest number is: " << smallestNum << std::endl;
}

void userNums(int values[]) {
    for (int i = 0; i < 10; ++i) {
        std::cout << "Enter a number: ";
        std::cin >> values[i];
    };
}

void largestValue(int values[] ,int& largestNum) {
    largestNum = values[0];
    for(int i = 0; i < 10; ++i) {
        if (largestNum < values[i])
            largestNum = values[i];
    };
}

void smallestValue(int values[] ,int& smallestNum) {
    smallestNum = values[0];
    for(int i = 0; i < 10; ++i) {
        if (smallestNum > values[i])
            smallestNum = values[i];
    };
}