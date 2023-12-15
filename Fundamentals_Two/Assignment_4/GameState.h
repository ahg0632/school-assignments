

#ifndef GAMESTATE_H
#define GAMESTATE_H
#include <iostream>
#include "MoveStack.h"


class GameState {
    char boardState[3][3];
    MoveStack moveStack;
public:
    GameState();
    int getCurrentPlayer();
    int addMove(Move move);
    bool undoLast();
    void displayBoardState(std::ostream& out);
    bool checkLastPlayerWin();
};


#endif //GAMESTATE_H
