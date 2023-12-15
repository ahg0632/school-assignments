#include <iostream>
#include <bits/stdc++.h>

double celsius(int);

int main() {
    for(int i = 0; i <= 20; i++) {
        std::cout << std::fixed << std::setprecision(2) << celsius(i) << std::endl;
    }
}

double celsius(int fahrenheit) {
    double temperature;
    const double FRACTION = 5 / 9.0;
    temperature = FRACTION * (fahrenheit - 32);
    return temperature;
}

    