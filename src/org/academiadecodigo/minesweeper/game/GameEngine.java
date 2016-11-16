package org.academiadecodigo.minesweeper.game;

import org.academiadecodigo.minesweeper.Difficulty;

import java.util.Scanner;

/**
 * Created by codecadet on 14/11/16.
 */
public class GameEngine {

    private static final int PROB = 90;
    private int gridRows;
    private int gridCols;
    private int numBombs;
    private int bombsBlown;
    private int[][] gameMatrix;
    private boolean[][] expanded;

    public void start() {

        int row, col;
        Scanner input = new Scanner(System.in);

        chooseDifficulty(Difficulty.BEGINNER);

        placeBombs();
        calculateAdjacent();
        printMatrix();
        //printBombsOnly();

        System.out.println();

        //expandMatrix();

        System.out.println("make a move (row, col)");
        row = input.nextInt();
        col = input.nextInt();

        checkMove(row, col);

        printGameMatrix();

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
        bombsBlown = 0;


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

                        if (gameMatrix[rr][cc] == -1) {
                            if (gameMatrix[r][c] != -1)
                                gameMatrix[r][c]++;
                        }
    }

    public void printBombsOnly(){

        for (int r = 1; r <= gridRows; r++) {
            for (int c = 1; c <= gridCols; c++) {
                if (gameMatrix[r][c] == -1)
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
                if (gameMatrix[r][c] == -1)
                    System.out.print("[*]");
                else if (gameMatrix[r][c] > 0)
                    System.out.print("[" + gameMatrix[r][c] + "]");
                else
                    System.out.print("[ ]");
            }
            System.out.println();
        }
    }

    public void printGameMatrix() {

        for (int r = 1; r <= gridRows; r++) {
            for (int c = 1; c <= gridCols; c++) {
                if (gameMatrix[r][c] == 0 && expanded[r][c])
                    System.out.print("[e]");
                else if (gameMatrix[r][c] > 0 && expanded[r][c])
                    System.out.print("[" + gameMatrix[r][c] + "]");
                else
                    System.out.print("[ ]");
            }
            System.out.println();
        }
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
     * This checks if the position that was inserted is a bomb. If not, it will expand the nodes until they bump into a bomb, stopping the expansion
     *
     * @param row
     * @param col
     */
    public void checkMove(int row, int col) {

        if (gameMatrix[row][col] == -1) {
            System.out.println("bomb hit");
            expanded[row][col] = true;
            bombsBlown++;
        } else{
            expandCell(row, col);
        }

    }


    /**
     *This expands the cell selected and the cells right next to it until they bump into a number
     * @param row
     * @param col
     */

    public void expandCell(int row, int col) {

        if(row == 0 || row == gridRows+1 || col == 0 || col == gridCols+1)
            return;

        if(gameMatrix[row][col] > 0){
            expanded[row][col] = true;
            return;
        }

        //check a cell for expansion status
        //if it can be expanded, expand it and move to the adjacent cells via recursion

        if(gameMatrix[row][col] == 0 && !expanded[row][col]){
            expanded[row][col] = true;

            //check adjacent cells
            for (int rr = row - 1; rr <= row + 1; rr++)
                for (int cc = col - 1; cc <= col + 1; cc++)
                    expandCell(rr, cc);
        }
    }
}

