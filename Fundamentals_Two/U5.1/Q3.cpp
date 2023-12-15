#include <iostream>
#include <cstddef>

using namespace std;

struct Node {
        int num;
        Node *next;
    };


class LinkedList{
    private: 
        
    public:
        Node *head;
        LinkedList();
        ~LinkedList();
        void circularList(int, int);
        void displayList();
};

LinkedList::LinkedList() {
    head = nullptr;
}

void LinkedList::circularList(int value, int target) {
    Node *newNode = new Node;
    newNode->num = value;
    newNode->next = nullptr;

    if (head == nullptr)
        head = newNode;
    else {
        Node *p = head;
        while(p->num != target) {
            p = p->next;
        };
        p ->next = newNode;
        newNode->next = head;
    };
};

void LinkedList::displayList() {
    Node *p = head;
    int count = 0;
    while(p && (count < 20)) {
        cout << p->num << " ";
        p = p->next;
        count++;
    }
    cout << endl;
}


LinkedList::~LinkedList() {
    Node *p;
    while (!(head == nullptr)) {
        p = head;
        head = head->next;
        delete p;
    }
}


int main() {
    LinkedList list1;
    list1.circularList(1, 0);
    list1.circularList(2, 1);
    list1.circularList(3, 2);
    list1.circularList(4, 3);
    list1.circularList(5, 4);


    list1.displayList();


    return 0;
}