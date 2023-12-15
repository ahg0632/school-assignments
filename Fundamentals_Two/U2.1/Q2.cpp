#include <iostream>
#include <iomanip>

using namespace std;

void inputHandler(int [], const int);
void selectionSortDescendTrace(int [], const int);
void printValues(int [], const int);

int main() {
    const int SIZE = 10;
    int userDigits[SIZE] = {34, 89, 23, 45, 34};

    inputHandler(userDigits, SIZE);
    selectionSortDescendTrace(userDigits, SIZE);
};

void inputHandler(int userDigits[], const int SIZE) {
    int userInput;
    bool condition = false;
    cout << "Input up to 10 digits greater than 0 (type -1 to end early): " << endl;
    for(int i = 0; i < SIZE; ++i) {
        cout << "Input: ";
        cin >> userInput;
        while (true) {
            if (userInput == -1) {
                condition = true;
                break;
            }

            if (userInput > -1) {
                userDigits[i] = userInput;
                break;
            }else {
                cout << "Please input a greater than 0";
            }
        };
        if(condition) {break;}
    }
};

void selectionSortDescendTrace(int userDigits[], const int SIZE) {
    int largestIndex;
    int temp;
    for(int i = 0; i < SIZE - 1; ++i) {
        largestIndex = i;
        for(int j = i + 1; j < SIZE; ++j) {
            if (userDigits[j] > userDigits[largestIndex])
                largestIndex = j;
        };
        temp = userDigits[i];
        userDigits[i] = userDigits[largestIndex];
        userDigits[largestIndex] = temp;
        printValues(userDigits, SIZE);
    };
};

void printValues(int userDigits[], const int SIZE) {
    for(int i = 0; i < SIZE; ++i) {
        if (userDigits[i] > 0)
            cout << setw(3) << userDigits[i];
    };
    cout << endl;
};