package com.mjasinski.tictactoe.engine;

import java.util.Scanner;

/**
 * Created by Marcin on 27.05.2019
 */
public class Main {

    static {
        System.loadLibrary("native");
    }

    private static char[] gameBoard = {' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' '};

    public static void main(String[] args) {

        System.out.println(printBoard());

        boolean computerMove = false;
        Scanner scanner = new Scanner(System.in);

        while(true){

            if (getNumberOfMovesMade() == 16) {
                System.out.println("Game has tied!");
                break;
            }

            if(checkIfHasWon('X')){
                System.out.println("Player X has won!");
                break;
            }

            if(checkIfHasWon('O')){
                System.out.println("Player O has won!");
                break;
            }

            if(computerMove){

                sayHello();

            } else {

                System.out.println("Your turn!");
                int position;
                boolean validPosition;
                do {
                    System.out.print("Column: ");
                    int column = scanner.nextInt();
                    System.out.print("Row: ");
                    int row = scanner.nextInt();

                    position = 4 * row + column;

                    validPosition = column >= 0 && row >= 0 && position >= 0 && position <= 15;

                    if(!validPosition) System.out.println("Invalid position");
                }while(!validPosition || !insertMove('X', position));
            }

            if(computerMove) System.out.println("Computer move: ");
            System.out.println(printBoard());

            computerMove = !computerMove;
        }
    }

    public static native void sayHello();

    private static boolean insertMove(char playerChar, int position) {

        if(gameBoard[position] == ' '){
            gameBoard[position] = playerChar;
            return true;
        } else {
            System.err.println("This field is already taken!");
            return false;
        }
    }

    private static boolean checkIfHasWon(char playerChar){

        return checkRow(playerChar, 0) || checkRow(playerChar, 1) ||
                checkRow(playerChar, 4) || checkRow(playerChar, 5) ||
                checkRow(playerChar, 8) || checkRow(playerChar, 9) ||
                checkRow(playerChar, 12) || checkRow(playerChar, 13) ||

                checkCol(playerChar, 0) || checkCol(playerChar, 4) ||
                checkCol(playerChar, 1) || checkCol(playerChar, 5) ||
                checkCol(playerChar, 2) || checkCol(playerChar, 6) ||
                checkCol(playerChar, 3) || checkCol(playerChar, 7) ||

                checkDiagUp(playerChar, 8) || checkDiagUp(playerChar, 9) ||
                checkDiagUp(playerChar, 12) || checkDiagUp(playerChar, 13) ||

                checkDiagDown(playerChar, 0) || checkDiagDown(playerChar, 1) ||
                checkDiagDown(playerChar, 4) || checkDiagDown(playerChar, 5);
    }

    private static boolean checkRow(char playerChar, int startPosition){
        return gameBoard[startPosition] == playerChar
                && gameBoard[startPosition + 1] == playerChar
                && gameBoard[startPosition + 2] == playerChar;
    }

    private static boolean checkCol(char playerChar, int startPosition){
        return gameBoard[startPosition] == playerChar
                && gameBoard[startPosition + 4] == playerChar
                && gameBoard[startPosition + 8] == playerChar;
    }

    private static boolean checkDiagDown(char playerChar, int startPosition){
        return gameBoard[startPosition] == playerChar
                && gameBoard[startPosition + 5] == playerChar
                && gameBoard[startPosition + 10] == playerChar;
    }

    private static boolean checkDiagUp(char playerChar, int startPosition){
        return gameBoard[startPosition] == playerChar
                && gameBoard[startPosition - 3] == playerChar
                && gameBoard[startPosition - 6] == playerChar;
    }

    private static int getNumberOfMovesMade() {
        int movesMade = 0;
        for (char field : gameBoard) {
            if (field != ' ') movesMade++;
        }

        return movesMade;
    }

    private static String printBoard() {

        StringBuilder sb = new StringBuilder();

        sb.append("     0  1  2  3\n");
        for(int i = 0; i < gameBoard.length; i++){
            if(i % 4 == 0){
                sb.append((i / 4));
                sb.append("   ");
            }
            sb.append(" ");
            sb.append(gameBoard[i]);
            sb.append(" ");
            if((i+1) % 4 == 0){
                sb.append("\n");
            }
        }

        return sb.toString();
    }
}
