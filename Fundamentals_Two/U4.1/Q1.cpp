#include <iostream>
#include <string>

using namespace std;

class Drink {
    private:
    string brand;
    double price;
    int quantity;

    public:
    void setPrice(double);
    void setQuantity(int);
    double getPrice() const;
    int getQuantity() const;

    double totalCost();

    Drink(string);
};

Drink::Drink(string name) {
    brand = name;
    price = 0;
    quantity = 0;
}

void Drink::setPrice(double p) {
    price = p;
};

void Drink::setQuantity(int q) {
    quantity = q;
};

double Drink::getPrice() const {
    return price;
};

int Drink::getQuantity() const {
    return quantity;
}

double Drink::totalCost() {
    return price * quantity;
};

int main() {
    Drink Coke = Drink("Coke");
    Coke.setPrice(2.5);
    Coke.setQuantity(10);

    Drink Beer = Drink("Beer");
    Beer.setPrice(3.5);
    Beer.setQuantity(5);

    cout << "Total cost of Coke is: " << "$" << Coke.totalCost() << endl;
    cout << "Total cost of Beer is: " << "$" << Beer.totalCost() << endl;
    return 0;
}