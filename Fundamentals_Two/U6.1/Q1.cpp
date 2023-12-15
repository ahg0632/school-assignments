#include <cassert>
#include <iostream>

class DynIntStack {
    private:
        struct Node {
            int data;
            Node *next;
        };
        Node *head;
    public:
        DynIntStack() {head = nullptr;}
        ~DynIntStack();
        void push(int);
        void reverse();
        void stackPrint() const;
        int pop();
        bool isFull() const {return false;}
        bool isEmpty() const {return head == nullptr;}
};

void DynIntStack::push(int num) {
    assert(!isFull());
    Node *temp = new Node;
    temp->data = num;
    temp->next = head;
    head = temp;
};

int DynIntStack::pop() {
    assert(!isEmpty());
    int result = head->data;
    Node *temp = head;
    head = head->next;
    delete temp;
    return result;
};

DynIntStack::~DynIntStack() {
    Node *temp;
    while (!isEmpty()) {
        temp = head;
        head = head->next;
        delete temp;
    };
};

void DynIntStack::stackPrint() const {
    assert(!isEmpty());
    Node *p = head;
    while(p) {
        std::cout << p->data << " ";
        p = p->next;
    };
    std::cout << std::endl;
};

void DynIntStack::reverse() {
    assert(!isEmpty());
    DynIntStack temp;
    while(!isEmpty()) {
        temp.push(head->data);
        pop();
    };
    head = temp.head;
    temp.~DynIntStack();
};


int main() {
    DynIntStack stack;
    stack.push(1);
    stack.push(2);
    stack.push(3);
    stack.push(4);
    stack.push(5);
    stack.push(6);
    stack.push(7);
    stack.stackPrint();
    
    stack.reverse();
    stack.stackPrint();

    stack.~DynIntStack();

    return 0;
};

