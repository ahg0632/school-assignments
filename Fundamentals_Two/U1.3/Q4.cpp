#include <iostream>
#include <string>

using namespace std;

//No idea

struct Car{
    string carMake;
    string carModel;
    int yearModel;
    double cost;
};


int main() {
    Car vehicle1;
    vehicle1.carMake = "Ford";
    vehicle1.carModel = "Mustang";
    vehicle1.yearModel = 1968;
    vehicle1.cost = 20000;

    Car garage1[25];
    Car garage2[35] = {
        {"Ford", "Taurus", 1997, 21000},
        {"Honda", "Accord", 1992, 11000},
        {"Lamborghini", "Countach", 1997, 2000000}
    };

    for(int i = 0; i < 35; ++i) {
        cout << garage2[1] << endl;
    };

}

