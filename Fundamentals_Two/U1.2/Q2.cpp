#include <iostream>

void timesTen(int);

int main() {
    timesTen(4);    //should equal 40
    timesTen(10);   //should equal 100
    timesTen(60);   //should equal 600
    timesTen(7);    //should equal 70
    timesTen(14);   //should equal 140
}

void timesTen(int number) {
    number *= 10;
    std::cout << number << std::endl;
}