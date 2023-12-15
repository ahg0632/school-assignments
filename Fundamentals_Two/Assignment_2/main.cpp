#include <iostream>
#include <fstream>
using namespace std;

struct Result {
    int bibNumber;
    string name;
    double distance;
    string time;
} results[100];

void readDataset(ifstream&, struct Result[], int&);

int main() {

    ifstream inputFile;
    inputFile.open("dataset.txt");
    if(inputFile.is_open()) {
        cout << "File opened successfully" << endl;
    } else {
        cerr << "Error opening file" << endl;
        if(inputFile.fail()) {
            cerr << "Fail bit is set" << endl;
        };
        if(inputFile.bad()) {
            cerr << "Bad bit is set" << endl;
        };
    };
    
}

void readDataset(ifstream& in, Result results[], int &count) {
    
    while (in >> results[count].bibNumber && count < 50) {
        getline(in,results[count].name);
        in >> results[count].distance;
        in >> results[count].time;
        in >>  ws;

        count++;
        cout << count << " ";
    };

    
    in.close();
}
