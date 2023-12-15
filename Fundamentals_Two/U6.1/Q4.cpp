#include <iostream>
#include <cassert>

class DynIntQueue {
    private:
        struct Node {
            int data;
            int windowSize = 3;
            Node *next;
        };
        Node *front;
        Node *rear;
    public:
        DynIntQueue() {front = nullptr; rear = nullptr;}
        ~DynIntQueue();
        void enqueue(int);
        int dequeue();
        int pop();
        void push(int num);
        bool isFull() const {return false;}
        bool isEmpty() const {return front == nullptr;}
};

void DynIntQueue::push(int num) {
    assert(!isFull());
    Node *temp = new Node;
    temp->data = num;
    temp->next = front;
    front = temp;
};

int DynIntQueue::pop() {
    assert(!isEmpty());
    char result = front->data;
    Node *temp = front;
    front = front->next;
    delete temp;
    return result;
};

void DynIntQueue::enqueue(int num) {
    assert(!isFull());
    Node *temp = new Node;
    temp->data = num;
    temp->next = nullptr;

    if(isEmpty()) {
        front = rear = temp;
    } else {
        rear->next = temp;
        rear = temp;
    };
}

int DynIntQueue::dequeue() {
    assert(!isEmpty());
    int value = front->data;
    Node *temp = front;
    front = front->next;
    delete temp;

    return value;
}

DynIntQueue::~DynIntQueue() {
    while(!isEmpty())
        dequeue();
}



int main() {
    DynIntQueue queue1;
    DynIntQueue queue2;

    DynIntQueue stack;
    return 0;
}