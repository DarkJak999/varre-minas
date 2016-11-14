package org.academiadecodigo.minesweeper.game;

import org.academiadecodigo.minesweeper.Difficulty;

import java.util.Scanner;

/**
 * Created by codecadet on 14/11/16.
 */
public class GameEngine {

    private int gridRows;
    private int gridCols;
    private int numBombs;
    //private Bomb[] bombs;
    private String[][] gameMatrix;

    public void start() {

        int row, col;
        Scanner input = new Scanner(System.in);

        chooseDifficulty(Difficulty.BEGINNER);
        drawMatrix();

        System.out.println("make a move:");
        row = input.nextInt();
        col = input.nextInt();

        checkMove(row, col);

    }

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

        //bombs = new Bomb[numBombs];
        gameMatrix = new String[gridRows][gridCols];


    }

    public void drawMatrix() {

        int bombsPlaced = 0;
        int attempts = 0;

        //this makes sure that we never get a map without all the bombs given by the difficulty

        while (bombsPlaced < numBombs) {
            System.out.println(attempts);
            bombsPlaced = 0;

            clearMatrix();
            for (int rows = 0; rows < gridRows; rows++) {
                for (int cols = 0; cols < gridRows; cols++) {
                    if (bombsPlaced < numBombs) {
                        //random chance to generate a bomb
                        if (((Math.random() * (100 - 1)) + 1) > 90) {
                            bombsAway(cols, rows);
                            bombsPlaced++;
                        } else
                            gameMatrix[rows][cols] = "[ ]";
                    } else {
                        gameMatrix[rows][cols] = "[ ]";
                    }
                }
            }
            attempts++;
        }

        System.out.println(bombsPlaced);

        for (int rows = 0; rows < gridRows; rows++) {
            for (int cols = 0; cols < gridCols; cols++) {
                System.out.print(gameMatrix[rows][cols]);
            }
            System.out.println();
        }
    }

    public void clearMatrix() {

        for (int i = 0; i < gridRows; i++) {
            for (int j = 0; j < gridCols; j++)
                gameMatrix[i][j] = "[ ]";
        }
    }

    public void bombsAway(int x, int y) {

        int minx, miny, maxx, maxy;

        // Don't check outside the edges of the board
        minx = (x <= 0 ? 0 : x - 1);
        miny = (y <= 0 ? 0 : y - 1);
        maxx = (x >= gridCols - 1 ? gridCols : x + 2);
        maxy = (y >= gridRows - 1 ? gridRows : y + 2);

        // Check all immediate neighbours for mines
        for (int i = minx; i < maxx; i++) {
            for (int j = miny; j < maxy; j++) {
                if (!gameMatrix[i][j].equals("[*]"))
                    gameMatrix[i][j] = "[*]";
                else
                    gameMatrix[i][j] = "[1]";
            }
        }
    }

    public void checkMove(int row, int col) {

        if (gameMatrix[row][col].equals("[*]"))
            System.out.println("bomb hit");
        else
            System.out.println("missed");
    }
}

