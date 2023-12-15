#include <iostream>
#include <cstddef>

using namespace std;

class LinkedList{
    private: 
        struct Node {
            int num;
            Node *next;
        };
        Node *head;

    public:
        LinkedList();
        void insertAfter(int, int);
        void displayList();
};

LinkedList::LinkedList() {
    head = nullptr;
}

void LinkedList::insertAfter(int value, int target) {
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
    };
};

void LinkedList::displayList() {
    Node *p = head;
    while(p) {
        cout << p->num << " ";
        p = p->next;
    }
    cout << endl;
}

int main() {
    LinkedList list;

    list.insertAfter(1, 0);
    list.insertAfter(2, 1);
    list.insertAfter(3, 2);
    list.insertAfter(4, 3);
    list.insertAfter(5, 4);

    list.displayList();


    return 0;
}