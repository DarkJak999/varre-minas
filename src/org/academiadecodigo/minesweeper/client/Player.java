package org.academiadecodigo.minesweeper.client;

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

    public Player(String hostName, int portNumber) {

        this.hostName = hostName;
        this.portNumber = portNumber;

    }

    public void start() {

       keyboardInput = new Scanner(System.in);
        String playerMessage;

        try {
            clientSocket = new Socket(hostName, portNumber);
            output = new DataOutputStream(clientSocket.getOutputStream());
            Thread thread = new Thread(new Reader());
            thread.start();

            while (!clientSocket.isClosed()){
                playerMessage = keyboardInput.nextLine();
                output.write(playerMessage.getBytes());
                output.write("\n".getBytes());
                output.flush();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private class Reader implements Runnable {

        private String playerMessage2;

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
                    playerMessage2 = input.readLine();

                    if (playerMessage2 == null) {
                        clientSocket.close();
                        return;
                    }

                    System.out.println(playerMessage2);

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        }
    }
}
