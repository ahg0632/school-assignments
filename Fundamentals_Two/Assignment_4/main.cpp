
#include <iostream>
#include <sstream>
using namespace std;

#include "GameState.h"

int main() {
    GameState game;
    
    string command;
    int x;
    int y;
    
    bool gameOver = false;

    game.displayBoardState(cout);

    while (!gameOver) {
        cout << "Player " << game.getCurrentPlayer() << " make a turn." << endl;
        cout << "Type 'move' or 'undo': ";
        cin >> command;
        if (command == "move") {
            cin >> x >> y;
            Move move(x,y);
            int result = game.addMove(move);
            if (result == -1) {
                cout << "Incorrect move. Please try again." << endl;
            } else {
                game.displayBoardState(cout);
                if (game.checkLastPlayerWin()) {
                    cout << "Player " << !game.getCurrentPlayer() << " won!\n";
                    gameOver = true;
                } else if (result == 0) {
                    cout << "It's a draw!" << endl;
                    gameOver = true;
                }
            }
        } else if (command == "undo") {
            bool undid = game.undoLast();
            if (!undid) {
                cout << "No moves to undo." << endl;
            } else {
                game.displayBoardState(cout);
            }
        } else {
            cout << "Invalid command" << endl;
        }
    }
        
}
