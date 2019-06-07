package com.mjasinski.tictactoe.engine;

import javax.script.Invocable;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.Scanner;

/**
 * Created by Marcin on 27.05.2019
 */
public class Main {

    private static char[] gameBoard = {' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' '};

    private static ScriptEngine scriptEngine;

    public static void main(String[] args) {

        scriptEngine = new ScriptEngineManager().getEngineByName("nashorn");

        System.out.println(printBoard());

        boolean computerMove = false;
        Scanner scanner = new Scanner(System.in);

        while(true){

            if(checkIfHasWon('X')){
                System.out.println("Player X has won!");
                break;
            }

            if(checkIfHasWon('O')){
                System.out.println("Player O has won!");
                break;
            }

            computerMove = !computerMove;

            if(computerMove){

                try {
                    scriptEngine.eval(new FileReader("resources/test.js"));
                    Invocable invocable = (Invocable)scriptEngine;

                    gameBoard = (char[])invocable.invokeFunction("insertOnRandomPosition", gameBoard);

                } catch (ScriptException | FileNotFoundException | NoSuchMethodException e) {
                    e.printStackTrace();
                }

            } else {

                System.out.println("Your turn!");
                int position;
                boolean validPosition;
                do {
                    System.out.printf("Row and column to insert X: \n");
                    System.out.print("Column:");
                    int column = scanner.nextInt();
                    System.out.print("Row: ");
                    int row = scanner.nextInt();

                    position = 4 * row + column;

                    validPosition = column >= 0 && row >= 0 && position >= 0 && position <= 15;
                }while(!validPosition || !insertMove('X', position));
            }

            if(computerMove) System.out.println("Computer move: ");
            System.out.println(printBoard());
        }
    }

    public static boolean insertMove(char playerChar, int position){

        if(gameBoard[position] == ' '){
            gameBoard[position] = playerChar;
            return true;
        } else {
            System.err.println("This field is already taken!");
            return false;
        }
    }

    private static boolean checkIfHasWon(char playerChar){

        if (
            checkRow(playerChar, 0) || checkRow(playerChar, 1) ||
            checkRow(playerChar, 4) || checkRow(playerChar, 5) ||
            checkRow(playerChar, 8) || checkRow(playerChar, 9) ||
            checkRow(playerChar, 12) || checkRow(playerChar, 13) ||

            checkCol(playerChar, 0) || checkCol(playerChar, 4) ||
            checkCol(playerChar, 1) || checkCol(playerChar, 5) ||
            checkCol(playerChar, 2) || checkCol(playerChar, 6) ||
            checkCol(playerChar, 3) || checkCol(playerChar, 7) ||

            checkDiagUp(playerChar, 8) || checkDiagUp(playerChar, 9) ||
            checkDiagUp(playerChar, 12) || checkDiagUp(playerChar, 13) ||

            checkDiagDown(playerChar, 0) || checkDiagDown(playerChar,1) ||
            checkDiagDown(playerChar, 4) || checkDiagDown(playerChar,5)
        ){
            return true;
        } else {

            return false;
        }
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

    public static String printBoard() {

        StringBuilder sb = new StringBuilder();

        sb.append("     0  1  2  3\n");
        for(int i = 0; i < gameBoard.length; i++){
            if(i % 4 == 0){
                sb.append((i / 4) + "   ");
            }
            sb.append(" " + gameBoard[i] + " ");
            if((i+1) % 4 == 0){
                sb.append("\n");
            }
        }

        return sb.toString();
    }
}
