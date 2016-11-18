package org.academiadecodigo.minesweeper.server;

import org.academiadecodigo.minesweeper.game.GameEngine;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Vector;

/**
 * Created by codecadet on 14/11/16.
 */
public class Server {

    private Vector<ClientMirror> clientMirrorList;
    private ServerSocket serverSocket;
    private Socket clientSocket;
    private int portNumber;
    private String username;
    private GameEngine gameEngine;

    public Server(int portNumber) {
        clientMirrorList = new Vector<>();
        this.portNumber = portNumber;
        gameEngine = new GameEngine();
    }

    public void start() {

        Thread thread;
        ClientMirror clientMirror;


        try {
            serverSocket = new ServerSocket(portNumber);
            System.out.println("Waiting for server...");

            while (true) {

                clientSocket = serverSocket.accept();
                clientMirror = new ClientMirror(this, clientSocket);
                thread = new Thread(clientMirror);
                thread.start();
                clientMirrorList.add(clientMirror);
                System.out.println(clientMirrorList.size());

                if (clientMirrorList.size() == 2 && clientMirrorList.get(0).isGameMode()
                        && clientMirrorList.get(1).isGameMode()){
                    gameEngine.start();
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public synchronized void broadcast (String message) {

        message = Thread.currentThread().getName()+ ": " + message;

        for (int i = 0; i < clientMirrorList.size() ; i++) {
            clientMirrorList.get(i).sendMessage(message);
        }
    }


    //chamar método de conversão matrix para byte[]

    // for testing purposes

    private byte [] matrixLine1 = "abcdefghi".getBytes();

    public synchronized void broadcastMatrix(byte[] matrixLine) {

        for (int i = 0; i < clientMirrorList.size() ; i++) {
            clientMirrorList.get(i).sendMatrix(matrixLine);
        }

    }

    public synchronized void exitGame (String username) {

        String exitMessage = username + " exit the conversation!";

        for (int i = 0; i <clientMirrorList.size(); i++) {

            if (clientMirrorList.get(i).getUsername().equals(username));
                clientMirrorList.remove(i);
                broadcast(exitMessage);
                return;
            }

        }

    public Vector<ClientMirror> getClientMirrorList() {
        return clientMirrorList;
    }
}
