#include <iostream>
#include <string>
#include <iomanip>

using namespace std;

struct Book {
    string title;
    int year;
};

void bubbleSort(struct Book[], int, int&);
void printBookshelf(struct Book[], int, int);

int main(){
    int size = 4;
    int count = 0;
    Book bookshelf[size] = {
        {"Alpha", 1994},
        {"Beta", 1848},
        {"Gamma", 1950},
        {"Delta", 1340}
    };
    bubbleSort(bookshelf, size, count);
    printBookshelf(bookshelf, size, count);
    
}


void bubbleSort(struct Book bookshelf[], int size, int& count) {
    bool swapped = false;
    do {
        swapped = false;
        for (int i = 0; i < size - 1; ++i) {
            if (bookshelf[i].year > bookshelf[i+1].year) {
                //swap
                Book temp = bookshelf[i];
                bookshelf[i] = bookshelf[i+1];
                bookshelf[i+1] = temp;
                swapped = true;
            };
            count++;
        };
    } while (swapped == true);
}

void printBookshelf(struct Book bookshelf[], int size, int count) {
    for(int i = 0; i < size; ++i) {
        cout << bookshelf[i].year << setw(8) << bookshelf[i].title << endl;
    };
    cout << "Number of comparisons: " << count << endl;
}