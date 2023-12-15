
#include <cassert>
using namespace std;

#include "MoveStack.h"


MoveStack::MoveStack() { 
    head = nullptr;
    Node *temp = new Node;
    temp->position = 0;
    temp->next = nullptr;
    head = temp;
}


MoveStack::~MoveStack() {
    Node *temp;
    while(head) {
        temp = head;
        head = head->next;
        delete temp;
    };
}

int MoveStack::getSize() {
    return head->position;
}

Move MoveStack::top() {
    return head->playerMoves;
}

void MoveStack::push(Move move) {
    Node *temp = new Node;
    temp->playerMoves = move;
    temp->position = head->position + 1;
    temp->next = head;
    head = temp;
};

void MoveStack::pop() {
    Node *temp = head;
    head = head->next;
    delete temp;
}
