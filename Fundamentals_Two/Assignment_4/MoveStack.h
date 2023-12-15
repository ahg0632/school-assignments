

#ifndef MOVESTACK_H
#define MOVESTACK_H

struct Move{
    int x;
    int y;
    Move(){ x=0; y=0; }
    Move(int a, int b) { x=a, y=b;}
};

class MoveStack{
private:
    struct Node {
        int position;
        Move playerMoves;
        Node *next;
    };
    Node *head;
public:
    MoveStack();
    ~MoveStack();
    int getSize();
    Move top();
    void push(Move move);
    void pop();
};

#endif //MOVESTACK_H



