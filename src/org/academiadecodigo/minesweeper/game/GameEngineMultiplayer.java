package org.academiadecodigo.minesweeper.game;

import org.academiadecodigo.minesweeper.Difficulty;

import java.util.Scanner;

/**
 * Created by codecadet on 17/11/16.
 */
public class GameEngineMultiplayer {

    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_RED = "\u001B[31m";

    private static final int PROB = 90;
    private int gridRows;
    private int gridCols;
    private int numBombs;
    private boolean bombBlown;
    private int numberOfBombsFound;
    private int[][] gameMatrix;
    private boolean[][] expanded;
    private int playerTurn;
    private int[] score = new int[2];


    /**
     * This calls all the functions that initialize the game matrix and all the variables that will influence the game
     */
    public void init() {
        chooseDifficulty(Difficulty.BEGINNER);
        placeBombs();
        calculateAdjacent();
    }

    public void start() {

        int row, col;
        Scanner input = new Scanner(System.in);

        init();

        while (true) {

            clearScreen();
            printGameMatrix();
            System.out.println();

            System.out.println("Player active: " + playerTurn + " Score: " + score[playerTurn]);
            System.out.println("make a move (row, col)");
            row = input.nextInt();
            col = input.nextInt();

            checkMove(row, col, playerTurn);

            if (!checkGameConditions()) {
                changePlayer();
            }

            if (checkForGameOver()) {
                System.out.println("Player 1 score: " + score[0]);
                System.out.println("Player 2 score: " + score[1]);
                printGameMatrix();
                break;
            }

            bombBlown = false;
        }

        System.out.println("Game Over");

    }

    /**
     * Chooses the difficulty level on which the game will be played and initializes the variables that dictate the grid size and number of bombs
     *
     * @param dif
     */

    public void chooseDifficulty(Difficulty dif) {

        switch (dif) {
            case BEGINNER:
                gridRows = gridCols = 9;
                numBombs = 10;
                break;
            case INTERMIDIATE:
                gridRows = gridCols = 16;
                numBombs = 40;
                break;
            case ADVANCED:
                gridRows = 16;
                gridCols = 30;
                numBombs = 99;
                break;
        }

        //we add another 2 to the matrix size in order to take care of the boundaries
        //when we check the adjacent cells for other bombs and update the solution
        gameMatrix = new int[gridRows + 2][gridCols + 2];
        expanded = new boolean[gridRows + 2][gridCols + 2];
        bombBlown = false;
        numberOfBombsFound = 0;
        playerTurn = 1;


    }

    /**
     * Runs throw the matrix and places a bomb according to the probability.
     * It doesn't stop until the matrix is complete with all the bombs
     */
    public void placeBombs() {
        int bombsPlaced = 0;

        while (bombsPlaced < numBombs) {
            bombsPlaced = 0;
            clearMatrix();
            for (int r = 1; r <= gridRows; r++) {
                for (int c = 1; c <= gridRows; c++) {

                    if (bombsPlaced < numBombs) {
                        if (((Math.random() * (100 - 1)) + 1) > PROB) {
                            gameMatrix[r][c] = -1;
                            bombsPlaced++;
                        }
                    }

                }
            }
            //System.out.println(bombsPlaced);
        }
    }

    /**
     * Method that calculates the value of each cell if there is an bomb adjacent to it.
     * If the adjacent cell isn't a bomb it increments the value the cell contains and that refers to the number of bombs adjacent to it.
     */

    public void calculateAdjacent() {

        for (int r = 1; r <= gridRows; r++)
            for (int c = 1; c <= gridCols; c++)

                for (int rr = r - 1; rr <= r + 1; rr++)
                    for (int cc = c - 1; cc <= c + 1; cc++)

                        if (gameMatrix[rr][cc] <= -1) {
                            if (gameMatrix[r][c] > -1)
                                gameMatrix[r][c]++;
                        }
    }

    /**
     * Debug printing method that only shows where the bombs are
     */

    public void printBombsOnly() {

        for (int r = 1; r <= gridRows; r++) {
            for (int c = 1; c <= gridCols; c++) {
                if (gameMatrix[r][c] <= -1)
                    System.out.print("[*]");
                else
                    System.out.print("[ ]");
            }
            System.out.println();
        }
    }

    /**
     * Draws the matrix to the screen with the bombs represented by an asterisk (*) and the open cells represented by it's value according to the adjacent bombs.
     * If there aren't adjacent bombs the cell stays empty.
     * This print function is mostly used for debug
     */

    public void printMatrix() {

        for (int r = 1; r <= gridRows; r++) {
            for (int c = 1; c <= gridCols; c++) {
                if (gameMatrix[r][c] <= -1)
                    System.out.print("[*]");
                else if (gameMatrix[r][c] > 0)
                    System.out.print("[" + gameMatrix[r][c] + "]");
                else
                    System.out.print("[ ]");
            }
            System.out.println();
        }
    }

    /**
     * Print function that prints the game matrix as the game plays out, only showing the values of cells that were expanded already
     */

    public void printGameMatrix() {

        System.out.print("   ");
        for (int i = 1; i <= gridRows; i++)
            System.out.print(" " + i + " ");

        System.out.println();

        for (int r = 1; r <= gridRows; r++) {
            System.out.print(" " + r + " ");
            for (int c = 1; c <= gridCols; c++) {
                if (gameMatrix[r][c] == 0 && expanded[r][c])
                    System.out.print("[" + colorPicker(gameMatrix[r][c]) + "e" + ANSI_RESET + "]");
                else if (gameMatrix[r][c] > 0 && expanded[r][c])
                    System.out.print("[" + colorPicker(gameMatrix[r][c]) + gameMatrix[r][c] + ANSI_RESET + "]");
                else if (gameMatrix[r][c] == -1 && expanded[r][c])
                    System.out.print(colorPicker(1) + "[*]" + ANSI_RESET);
                else if (gameMatrix[r][c] == -2 && expanded[r][c])
                    System.out.print(colorPicker(2) + "[*]" + ANSI_RESET);
                else
                    System.out.print("[ ]");
            }
            System.out.println();
        }
    }

    /**
     *
     */
    public String colorPicker(int value) {

        switch (value) {
            case 1:
                return "\u001B[34m";
            case 2:
                return "\u001B[32m";
            case 3:
                return "\u001B[31m";
            case 4:
                return "\u001B[35m";
            case 5:
                return "\u001B[34m";
            case 6:
                return "\u001B[36m";
            case 7:
                return "\u001B[30m";
            case 8:
                return "\u001B[37m";
            case 0:
                return "\u001B[33m";
            default:
                System.out.println("something went very wrong");
                break;
        }
        return "\u001B[0m";
    }

    public void changePlayer() {

        if(playerTurn == 0)
            playerTurn = 1;
        else if(playerTurn == 1)
            playerTurn = 0;
    }

    /**
     * Empties the game matrix with all the cells with the value 0 in order use the matrix in another match
     */

    public void clearMatrix() {

        for (int r = 1; r <= gridRows; r++) {
            for (int c = 1; c <= gridCols; c++) {
                gameMatrix[r][c] = 0;
                expanded[r][c] = false;
            }
        }
    }

    /**
     * This clears the screen in order to only show one grid at each time
     */

    public void clearScreen() {
        for (int i = 0; i < 15; i++) {
            System.out.println();
        }
    }

    /**
     * Checks the game conditions to see if the game is over and if the player either lost or won the game
     *
     * @return
     */
    public boolean checkGameConditions() {

        if (bombBlown)
            return true;

        return false;
    }

    public boolean checkForGameOver() {
        if (numberOfBombsFound == numBombs){
            if(score[0] > score[1])
                System.out.println("Player 1 wins");
            else
                System.out.println("Player 2 wins");
            return true;
        }
        else
            return false;
    }

    public void updateBombCell(int row, int col){
        if(playerTurn == 1){
            gameMatrix[row][col] = -2;
        }
    }


    /**
     * This checks if the position that was inserted is a bomb. If not, it will expand the nodes until they bump into a bomb, stopping the expansion
     *
     * @param row
     * @param col
     */
    public void checkMove(int row, int col, int playerTurn) {

        if (gameMatrix[row][col] == -1) {
            System.out.println("bomb hit");
            expanded[row][col] = true;
            score[playerTurn]++;
            numberOfBombsFound++;
            bombBlown = true;
            updateBombCell(row, col);
        } else {
            expandCell(row, col);
        }

    }


    /**
     * This expands the cell selected and the cells right next to it until they bump into a number
     *
     * @param row
     * @param col
     */

    public void expandCell(int row, int col) {

        //if it reaches the bounderies of the board, there's no need to check
        if (row == 0 || row == gridRows + 1 || col == 0 || col == gridCols + 1)
            return;

        //stopping condition - if we reach a cell that's adjacent to a bomb, we don't need to expand anymore
        if (gameMatrix[row][col] > 0) {
            expanded[row][col] = true;
            return;
        }

        //check a cell for expansion status
        //if it can be expanded, expand it and move to the adjacent cells via recursion

        if (gameMatrix[row][col] == 0 && !expanded[row][col]) {
            expanded[row][col] = true;

            //check adjacent cells in a 3x3 sub-matrix
            for (int rr = row - 1; rr <= row + 1; rr++)
                for (int cc = col - 1; cc <= col + 1; cc++)
                    expandCell(rr, cc);
        }
    }
}
