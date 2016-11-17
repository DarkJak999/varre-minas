package org.academiadecodigo.minesweeper.connection;

import org.academiadecodigo.minesweeper.connection.ClientMirror;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Vector;

/**
 * Created by codecadet on 14/11/16.
 */
public class Server {

    Vector<ClientMirror> clientMirrorList;
    ServerSocket serverSocket;
    Socket clientSocket;
    private int portNumber;

    public Server(int portNumber) {
        clientMirrorList = new Vector<>();
        this.portNumber = portNumber;
    }

    public void start() {

        Thread thread;
        ClientMirror clientMirror;


        try {
            serverSocket = new ServerSocket(portNumber);
            System.out.println("Waiting for connection...");

            while (true) {

                clientSocket = serverSocket.accept();
                clientMirror = new ClientMirror(this, clientSocket);
                clientMirrorList.add(clientMirror);
                System.out.println("Hello player, welcome to Varre Minas." + clientMirrorList.size()+1 + "players are logged to the family");
                thread = new Thread(clientMirror);
                thread.start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public synchronized void broadcast (String[][] matrix) {

        for (int i = 0; i < clientMirrorList.size() ; i++) {
            clientMirrorList.get(i).sendMessage(matrix);
        }
    }



}
