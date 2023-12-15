#include <iostream>
#include <string>

using namespace std;

class Monster {
    private:
    int health;
    string name;
    
    public:
    void setName(string);
    void setHealth(int);
    string getName() const;
    int getHealth() const;
    double damage() const;

    Monster(string, int);
};

Monster::Monster(string n, int hp) {
    name = n;
    health = hp;
};

void Monster::setHealth(int hp) {
    health = hp;
};

void Monster::setName(string n) {
    name = n;
};

string Monster::getName() const {
    return name;
};

int Monster::getHealth() const{
    return health;
};

double Monster::damage() const{
    return (health / 10.0) + 3;
};

int main() {
    Monster Skeleton = Monster("Skeleton", 100);
    cout << "Monster Spawned: " << Skeleton.getName() << " with " << Skeleton.getHealth() << " HP" << endl;
    cout << "Skeleton can deal " << Skeleton.damage() << " damage" << endl;
    cout << endl;

    Skeleton.setHealth(78);
    cout << "You dealt 22 damage to the skeleton" << endl;
    Skeleton.setName("Damaged Skeleton");
    cout << Skeleton.getName() << " can deal " << Skeleton.damage() << " damage" << endl;
}