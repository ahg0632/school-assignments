#include <iostream>

void inputMonkeyData(int[][5], const int, const int);
void averageTotalFood(int [][5], const int, const int);
void leastFoodEaten(int [][5], const int, const int);
void mostFoodEaten(int [][5], const int, const int);

int main() {
    const int MONKEY = 3;
    const int DAY = 5;
    int tracker[MONKEY][DAY];

    inputMonkeyData(tracker, MONKEY, DAY);
    averageTotalFood(tracker, MONKEY, DAY);
    leastFoodEaten(tracker, MONKEY, DAY);
    mostFoodEaten(tracker, MONKEY, DAY);
}

void inputMonkeyData(int tracker[][5], const int MONKEY, const int DAY) {
    for(int i = 0; i < MONKEY; ++i) {
        for(int j = 0; j < DAY; ++j) {
            int buffer;
            std::cout << "Enter pounds of food eaten by (Monkey " << i + 1 << " Day " << j + 1 << "): ";
            while (true){
                std::cin >> buffer;
                if (buffer >= 0) {
                    tracker[i][j] = buffer;
                    break;
                } else {
                    std::cout << "Enter a positive number: ";
                };
            };
        };
    };
}

void averageTotalFood(int tracker[][5], const int MONKEY, const int DAY) {
    double averageAmount = 0.0;
    for(int i = 0; i < MONKEY; ++i) {
        for(int j = 0; j < DAY; ++j) {
            averageAmount += tracker[i][j];
        };
    };
    averageAmount /= DAY;
    std::cout << "Average amount of food eaten per day by the whole family of monkeys is: ";
    std::cout << averageAmount << std::endl;
}

void leastFoodEaten(int tracker[][5], const int MONKEY, const int DAY) {
    int leastAmount = tracker[0][0];
    for(int i = 0; i < MONKEY; ++i) {
        for(int j = 0; j < DAY; ++j) {
            if (leastAmount > tracker[i][j])
                leastAmount = tracker[i][j];
        };
    };
    std::cout << "The least amount of food eaten during the week by any one monkey is: ";
    std::cout << leastAmount << std::endl;
}

void mostFoodEaten(int tracker[][5], const int MONKEY, const int DAY) {
    int mostAmount = tracker[0][0];
    for(int i = 0; i < MONKEY; ++i) {
        for(int j = 0; j < DAY; ++j) {
            if (mostAmount < tracker[i][j]) {
                mostAmount = tracker[i][j];
            }
        };
    };
    std::cout << "The most amount of food eaten during the week by any one monkey is: ";
    std::cout << mostAmount << std::endl;
}