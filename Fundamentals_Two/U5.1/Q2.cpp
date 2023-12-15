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
        void insertAfter(int, int);
        void displayList();
        void joinLists(Node*);
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

void LinkedList::joinLists(Node* otherList) {
    if (!otherList) {
            return;
        }

    if (!head) {
        head = new Node();
        head->num = otherList->num;
        head->next = nullptr;
    } else {
        Node* current = head;
        while (current->next) {
            current = current->next;
        }

        Node* otherCurrent = otherList;
        while (otherCurrent) {
            Node* newNode = new Node();
            newNode->num = otherCurrent->num;
            newNode->next = nullptr;
            current->next = newNode;
            current = current->next;
            otherCurrent = otherCurrent->next;
        }
    }
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
    list1.insertAfter(1, 0);
    list1.insertAfter(2, 1);
    list1.insertAfter(3, 2);
    list1.insertAfter(4, 3);
    list1.insertAfter(5, 4);

    LinkedList list2;
    list2.insertAfter(1, 0);
    list2.insertAfter(2, 1);
    list2.insertAfter(3, 2);
    list2.insertAfter(4, 3);
    list2.insertAfter(5, 4);

    list1.joinLists(list2.head);

    list1.displayList();


    return 0;
}