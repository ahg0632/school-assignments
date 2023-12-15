#include <iostream>
#include <fstream>
#include <regex>
#include <string>
#include "PasswordManager.h"

using namespace std;

int findUser(PasswordManager[], std::string, std::string, int);
void displayLogins(PasswordManager[], int);

int main() {
    const int MAX = 4;
    PasswordManager Logins[MAX];
    ifstream inputFile;
    inputFile.open("passwords.txt");

    if(inputFile.is_open()) {
        cout << "File opened successfully" << endl;
    } else {
        cerr << "Error opening file" << endl;
        return -1;
    };

    for (int i = 0; i < MAX; i++) {
        std::string first, second;
        inputFile >> ws;
        getline(inputFile, first, ' ');
        inputFile >> ws;
        getline(inputFile, second);
        Logins[i] = PasswordManager(first, second);
    };
    inputFile.close();

    std::string fileName;
    cout << "Please Enter the name of the input file: " << endl;
    getline(cin, fileName);
    inputFile.open(fileName);
    if(inputFile.is_open()) {
        cout << "File opened successfully" << endl;
    } else {
        cerr << "Error opening file" << endl;
        return -1;
    };
    std::regex r("\\s+");
    std::string netID;
    std::string netPass;
    std::string newPass;
    newPass = std::regex_replace(newPass, r, "");
    cout << "Please enter your NetID: " << endl;
    getline(cin, netID);
    cout << "Please enter your old password: " << endl;
    getline(cin, netPass);
    cout << "Please enter your new password: " << endl;
    getline(cin, newPass);

    int position = findUser(Logins, netID, netPass, MAX);
    switch (position) {
        case 0 ... 3:
            if (Logins[position].setNewPassword(newPass)) {
            cout << "Password has been changed for netID: " << Logins[position].getUsername();
            } else {
                cout << "New Password does not meet criteria" << endl;
            }         
        break;
        case -1:
            cout << "Either NetID or Old Password is invalid, password not changed" << endl;
        default:
            cout << "Error" << endl;
        break;
    };
    inputFile.close();
    displayLogins(Logins, MAX);
    return 0;
};

int findUser(PasswordManager arr [], std::string name, std::string code, int SIZE) {
    for (int i = 0; i < SIZE; i++) {
        if (arr[i].getUsername() == name && arr[i].authenticate(code))
            return i;
    };
    return -1;
};

void displayLogins(PasswordManager logs[], int SIZE) {
    for (int i = 0; i < SIZE; i++) {
        cout << logs[i].getUsername() << "\t" << logs[i].getEncryptedPassword();
    };
};
