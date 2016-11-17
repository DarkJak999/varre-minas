package org.academiadecodigo.minesweeper.connection;

import com.sun.xml.internal.bind.v2.TODO;

import java.io.*;
import java.net.Socket;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by codecadet on 17/11/16.
 */
public class Player {

    private Socket clientSocket;
    private DataOutputStream output;
    private String hostName;
    private int portNumber;
    private Scanner keyboardInput;
    private ExecutorService pool = Executors.newFixedThreadPool(2);

    public Player(String hostName, int portNumber) {

        this.hostName = hostName;
        this.portNumber = portNumber;

    }

    public void start() {

       keyboardInput = new Scanner(System.in);

        try {
            clientSocket = new Socket(hostName, portNumber);
            output = new DataOutputStream(clientSocket.getOutputStream());
            Thread thread = new Thread(new Reader());
            thread.start();
            pool.submit(thread);
            //TODO IMPLEMENTAR ISCONNECTED

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private class Reader implements Runnable {

        private BufferedReader input;

        public Reader() {
            try {
                this.input = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void run() {

            while (!clientSocket.isClosed()){

                try {
                    String message = input.readLine();

                    if (message == null) {
                        clientSocket.close();
                        return;
                    }

                    System.out.println(message);

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        }
    }
}
