#include<iostream>
using namespace std;

int * inputArray(int);
void swap1(int *, int *);

int main() {
    int const size = 5;
    int *array = inputArray(size);
    for(int j = 0; j < size; ++j) {
        cout << array[j] << " ";
    }
    cout << "\n";

    int x = 4, y = 6;
    swap1(&x, &y);
    cout << x << " " << y << endl;

    return 0;
}

int * inputArray(int n) {
    int *ptr = new int[n];
    for(int i = 0; i < n; i++) {
        cout << "Enter a number: ";
        cin >> *(ptr+i);
    };

    return ptr;
}

void swap1(int *a, int *b) {
    int temp;
    temp = *a;
    *a = *b;
    *b = temp;
}