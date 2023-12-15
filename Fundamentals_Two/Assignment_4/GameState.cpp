
#include <iostream>
using namespace std;

#include "GameState.h"

bool GameState::checkLastPlayerWin() {
    char targetSymbol = getCurrentPlayer() ? 'o' : 'x';
    int sumHorizontal, sumVertical;
    for (int i = 0; i < 3; ++i){
        sumHorizontal = 0;
        sumVertical = 0;
        for (int j = 0; j < 3; ++j){
            sumHorizontal += boardState[i][j] == targetSymbol;
            sumVertical += boardState[j][i] == targetSymbol;
        }
        if (sumHorizontal == 3 || sumVertical == 3)
            return true;
    }
    int sumDiagonal1 = 0, sumDiagonal2 = 0;
    for (int i = 0; i < 3; ++i){
        sumDiagonal1 += boardState[i][i] == targetSymbol;
        sumDiagonal2 += boardState[i][2 - i] == targetSymbol;
    }
    if (sumDiagonal1 == 3 || sumDiagonal2 == 3)
        return true;

    return false;
}

GameState::GameState() {
    for(int i = 0; i < 3; i++) {
        for(int j = 0; j < 3; j++) {
            boardState[i][j] = '_';
        };
    };
}

int GameState::getCurrentPlayer() {
    int player = moveStack.getSize() % 2;
    int currentPlayer;
    if(player == 0) {
        currentPlayer = 0;
    } else {
        currentPlayer = 1;
    }
    return currentPlayer;
}

int GameState::addMove(Move move) {
    char targetSymbol = getCurrentPlayer() ? 'o' : 'x';
    int validity;
    if(boardState[move.x][move.y] == '_') {
        validity = 1;
        moveStack.push(move);
        boardState[move.x][move.y] = targetSymbol;
        if(moveStack.getSize() == 9) {validity = 0;}
    } else {
        validity = -1;
    };
    return validity;
}

bool GameState::undoLast() {
    if(moveStack.getSize() != 0) {
        boardState[moveStack.top().x][moveStack.top().y] = '_';
        moveStack.pop();
        return true;
    }
    return false;
}

void GameState::displayBoardState(std::ostream& out) {
    for(int i = 0; i < 3; i++) {
        for(int j = 0; j < 3; j++) {
            out << boardState[i][j];
        };
        out << "\n";
    }
}


