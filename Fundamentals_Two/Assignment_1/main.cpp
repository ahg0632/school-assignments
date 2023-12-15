#include <iostream>
#include <iomanip>
#include <string>

using namespace std;

struct Country {
    string name;
    int goldMedals;
    int silverMedals;
    int bronzeMedals;
};

void scoreboard(struct Country[], const int);
void updateMedalCounts(struct Country[], int, char);
int totalMedals(struct Country[], const int);
int mostGoldMedals(struct Country[], const int);

int main() {
    const int SIZE = 8;
    Country countries[SIZE] = {
        {"Australia", 11, 14, 12},
        {"Canada", 5, 0, 1},
        {"China", 9, 14, 11},
        {"Great Britain", 8, 4, 8},
        {"Japan", 8, 10, 10},
        {"Netherlands", 7, 6, 7},
        {"Russia", 6, 10, 8},
        {"USA", 10, 6, 7},
    };

    int fullMedals = totalMedals(countries, SIZE);
    scoreboard(countries, SIZE);

    while(true) {
        int userInt;
        char userChar;

        cout << "Enter the country number (0 to quit):" << endl;
        cin >> userInt;
        if(userInt == 0) {
            cout << "Total Medals Awarded: " << fullMedals << endl;
            cout << "Country with the most Gold Medals: " << mostGoldMedals(countries, SIZE) << endl;
            break;
        };
        cout << "Enter the medal type (G, S, or B):" << endl;
        cin >> userChar;
        updateMedalCounts(countries, userInt, userChar);
        scoreboard(countries, SIZE);

    };

    return 0;

}

int mostGoldMedals(struct Country countries[], const int SIZE) {
    int currentTop = countries[0].goldMedals;
    for(int i = 0; i < SIZE; ++i) {
        if(countries[i].goldMedals > currentTop)
            currentTop = countries[i].goldMedals;
    };
    return currentTop;
}

int totalMedals(struct Country countries[], const int SIZE) {
    int total = 0;
    for (int i = 0; i < SIZE; ++i) {
        total += countries[i].goldMedals + countries[i].silverMedals + countries[i].bronzeMedals;
    };
    return total;
}

void updateMedalCounts(struct Country countries[], int countryNum, char medalType) {
    switch (medalType) {
        case 'G': case 'g':
            countries[countryNum - 1].goldMedals++;
            break;
        case 'S': case 's':
            countries[countryNum - 1].silverMedals++;
            break;
        case 'B': case 'b':
            countries[countryNum - 1].bronzeMedals++;
            break;
    }
}

void scoreboard(struct Country countries[], const int SIZE) {
    cout << left
    << setw(3) << "N"
    << setw(15) << "Country"
    << right
    << setw(10) << "Gold"
    << setw(10) << "Sliver"
    << setw(10) << "Bronze"
    << setw(10) << "Total"
    << endl;

    for(int i = 0; i < SIZE; ++i){
        cout << left
        << setw(3)  << i + 1
        << setw(15) << countries[i].name
        << right
        << setw(10) << countries[i].goldMedals
        << setw(10) << countries[i].silverMedals
        << setw(10) << countries[i].bronzeMedals
        << setw(10) << countries[i].goldMedals + countries[i].silverMedals + countries[i].bronzeMedals
        << endl;
    };
}