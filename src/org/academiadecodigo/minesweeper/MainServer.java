package org.academiadecodigo.minesweeper;

import org.academiadecodigo.minesweeper.server.Server;

/**
 * Created by codecadet on 14/11/16.
 */
public class MainServer {

    public static void main(String[] args) {

        Server server = new Server(8888);
        server.start();
    }
}
