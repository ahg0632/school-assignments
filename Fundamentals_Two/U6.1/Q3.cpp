#include <iostream>
#include <cassert>

class MovingAverage {
    private:
        struct Node {
            int data;
            int windowSize = 3;
            Node *next;
        };
        Node *front;
        Node *rear;
    public:
        MovingAverage() {front = nullptr; rear = nullptr;}
        ~MovingAverage();
        void enqueue(int);
        int dequeue();
        double average() const;
        bool window() const;
        bool isFull() const {return false;}
        bool isEmpty() const {return front == nullptr;}
};

void MovingAverage::enqueue(int num) {
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

int MovingAverage::dequeue() {
    assert(!isEmpty());
    int value = front->data;
    Node *temp = front;
    front = front->next;
    delete temp;

    return value;
}

double MovingAverage::average() const {
    double sum = 0;
    int counter = 0;
    Node *p = front;
    while(p) {
        sum += p->data;
        p = p->next;
        counter++;
    };
    if(counter == 0) {counter = 1;}
    return ((sum / counter) * 1);
}

bool MovingAverage::window() const {
    if(isEmpty()) {return false;}
    int counter = 0;
    Node *p = front;
    while(p) {
        p = p->next;
        counter++;
    }
    if (counter >= front->windowSize) {
        return true;
    } else {
        return false;
    };
}

MovingAverage::~MovingAverage() {
    while(!isEmpty())
        dequeue();
}



int main() {
    MovingAverage m;
    int input;
    do {
        std::cout << "Enter a digit or -1 to exit: ";
        std::cin >> input;
        switch(input) {
            case 0 ... 99:
                if(m.window()) {
                    m.dequeue();
                    m.enqueue(input);
                    std::cout << m.average() << std::endl;
                } else {
                    m.enqueue(input);
                    std::cout << m.average() << std::endl;
                };
                break;
            case -1:
                break;
            default:
                std::cout << "Invalid choice \nEnter a digit or -1 to exit: ";
                std::cin >> input;
        }
    } while (input != -1);
}