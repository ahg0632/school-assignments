#include <cassert>
#include <iostream>
#include <string>

class DynIntStack {
    private:
        struct Node {
            char delimeter;
            Node *next;
        };
        Node *head;
    public:
        DynIntStack() {head = nullptr;}
        ~DynIntStack();
        void push(char);
        char pop();
        //bool checkBalance(std::string);
        bool isFull() const {return false;}
        bool isEmpty() const {return head == nullptr;}
};

void DynIntStack::push(char num) {
    assert(!isFull());
    Node *temp = new Node;
    temp->delimeter = num;
    temp->next = head;
    head = temp;
};

char DynIntStack::pop() {
    assert(!isEmpty());
    char result = head->delimeter;
    Node *temp = head;
    head = head->next;
    delete temp;
    return result;
};

// bool DynIntStack::checkBalance(std::string input) {

// };

DynIntStack::~DynIntStack() {
    Node *temp;
    while (!isEmpty()) {
        temp = head;
        head = head->next;
        delete temp;
    };
};

bool checkBalance(std::string, DynIntStack);

int main() {
    DynIntStack stack;
    std::string userInput;
    getline(std::cin, userInput);

    if(userInput == "") {std::cout << "No input" << std::endl;}
    if(checkBalance(userInput, stack)) {
        std::cout << "True: You have bracket balance" << std::endl;
    } else {
        std::cout << "False: You do not have bracket balance" << std::endl;
    };

    stack.~DynIntStack();

    return 0;
};

bool checkBalance(std::string input, DynIntStack temp) {
    for(int i = 0; i < static_cast<int>(input.length()); i++) {
        char index = input.at(i);
        switch(index) {
            case '[':
            case '{':
            case '(':
                temp.push(index);
                break;
            case ']':
            case '}':
            case ')':
                if(!temp.isEmpty()) {
                    char closing = temp.pop();
                    if(closing == '[' && index != ']') {return false;}
                    if(closing == '{' && index != '}') {return false;}
                    if(closing == '(' && index != ')') {return false;}
                } else {
                    return false;
                };
            break;
        };
    };
    if(temp.isEmpty()) {
        return true;
    } else {
        return false;
    };
};
