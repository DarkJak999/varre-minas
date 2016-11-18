package org.academiadecodigo.minesweeper;

import org.academiadecodigo.minesweeper.client.Player;

/**
 * Created by codecadet on 17/11/16.
 */
public class MainClient {

    public static void main(String[] args) {

        Player player = new Player("localhost", 8888);
        player.start();


    }
}
