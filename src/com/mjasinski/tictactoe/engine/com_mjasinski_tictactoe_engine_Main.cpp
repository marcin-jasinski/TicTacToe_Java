#include <iostream>
#include <math.h>
#include <random>
#include <stdio.h>
#include <vector>
#include <map>
#include <utility>
#include <bitset>
#include <algorithm>
#include <string>
#include <cstring>

#include "jni.h"
#include "com_mjasinski_tictactoe_engine_Main.h"

const int maxDepth = 5;

int getNumberOfMovesMade(char*);
char checkIfGameIsWon(char*, char);
std::pair<int,int> getBestMove(char*, char, int);

JNIEXPORT void JNICALL Java_com_mjasinski_tictactoe_engine_Main_sayHello(JNIEnv* env, jobject thisObject){
    std::cout << "Hello, world!" << std::endl;
}

JNIEXPORT jint JNICALL Java_com_mjasinski_tictactoe_engine_Main_randomMove
(JNIEnv * env, jobject thisObject, jcharArray gameBoard, jchar symbol) {

    int arrLength = env->GetArrayLength(gameBoard);
    char* tempBoard = new char[arrLength];

    jchar *character = env->GetCharArrayElements(gameBoard, 0);
    for (int i = 0; i < arrLength; i++) {
        tempBoard[i] = (char)character[i];
    }

    int min=0;
    int max=15;
    int randomPosition = 0;

    std::random_device dev;
    std::mt19937 rng(dev());
    std::uniform_int_distribution<std::mt19937::result_type> dist(min, max);

    do{
        randomPosition = dist(rng);
    }while(tempBoard[randomPosition] != ' ');

    return randomPosition;
}

JNIEXPORT jint JNICALL Java_com_mjasinski_tictactoe_engine_Main_intelligentMove
(JNIEnv * env, jobject thisObject, jcharArray gameBoard, jchar symbol) {

    int arrLength = env->GetArrayLength(gameBoard);
    char* tempBoard = new char[arrLength];

    jchar *character = env->GetCharArrayElements(gameBoard, 0);
    for (int i = 0; i < arrLength; i++) {
        tempBoard[i] = (char)character[i];
    }

    std::pair<int,int> nextMove = getBestMove(tempBoard, 'O', 0);

    return nextMove.first;
}

int getNumberOfMovesMade(char* board){
    int moveCounter = 0;
    int arrLength = (unsigned)strlen(board);

    for (int i = 0; i < arrLength; i++){
        if (board[i] != ' '){
            moveCounter++;
        }
    }
    return moveCounter;
}

char checkIfGameIsWon(char* board, char symbol){

    char result = 'I';

    if (getNumberOfMovesMade(board)<5){
        return result;
    }

    int positionsToCheck[24][3] = {

        {0,1,2},{1,2,3},{4,5,6},{5,6,7},{8,9,10},{9,10,11},{12,13,14},{13,14,15},
        {0,4,8},{4,8,12},{1,5,9},{5,9,13},{2,6,10},{6,10,14},{3,7,11},{7,11,15},
        {1,6,11},{0,5,10},{5,10,15},{4,9,14},
        {2,5,8},{3,6,9},{6,9,12},{7,10,13}
    };

    for(int i = 0; i < 24; i++){
        int* positions = positionsToCheck[i];
        char line[3] = {board[positions[0]],board[positions[1]],board[positions[2]]};

        if(line[0] == symbol && line[1] == symbol && line[2] == symbol){
            result = symbol;
            return result;
        }
    }

    if (getNumberOfMovesMade(board) == 16){
        result = 'T';
        return result;
    }

    return result;
}

bool comparePairs(std::pair<int,int> p1, std::pair<int,int> p2)
{
    return (p1.second > p2.second);
}

std::pair<int,int> getBestMove(char* board, char symbol, int currentDepth){

    std::vector<int> availableMoves;
    for (int i = 0 ; i < 16 ; i++){
        if (board[i] == ' '){
            availableMoves.push_back(i);
        }
    }

    std::vector<std::pair<int,int>> availableMovesAndScores;

    for (int i = 0; i < availableMoves.size(); i++){

        int move = availableMoves[i];
        char* newBoard = new char[16];
        for(int i = 0; i < 16; i++) newBoard[i] = board[i];

        newBoard[move] = symbol;
        char result = checkIfGameIsWon(newBoard,symbol);
        int score = 0;

        if (result == 'T') score = 0;
        else if (result == symbol) {
            score = 1;
        }
        else {
            if(currentDepth != maxDepth){
                char otherSymbol = (symbol == 'X') ? 'O' : 'X';
                std::pair<int,int> nextMove = getBestMove(newBoard, otherSymbol, currentDepth+1);
                score = -(nextMove.second);
            }
        }

        std::pair<int,int> moveAndScore(move, score);

        if(score == 1) {
            return moveAndScore;
        }

        availableMovesAndScores.push_back(moveAndScore);
    }

    sort(availableMovesAndScores.begin(), availableMovesAndScores.end(), comparePairs);

    return availableMovesAndScores[0];
}