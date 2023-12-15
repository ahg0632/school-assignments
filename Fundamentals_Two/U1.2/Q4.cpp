#include <iostream>

void getNumber(int&);

int main() {
    int userInput;
    getNumber(userInput);

    while(true) {
        std::cout << "Is this your input " << userInput << " (Y/N): ";
        char validate;
        std::cin >> validate;
        if (validate == 'Y') {
            break;
        } else {
            getNumber(userInput);
        }
    };
}

void getNumber(int& userInput) {
    while(true) {
        std::cout << "Enter a number from 1 to 100: ";
        std::cin >> userInput;
        if (userInput > 0 && userInput < 101)
            break;
    };
}
    