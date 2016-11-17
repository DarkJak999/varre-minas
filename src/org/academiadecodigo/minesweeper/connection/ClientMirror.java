package org.academiadecodigo.minesweeper.connection;

import java.io.*;
import java.net.Socket;

/**
 * Created by codecadet on 14/11/16.
 */
public class ClientMirror implements Runnable {

    private Server server;
    private Socket clientSocket;
    private DataOutputStream output;
    private BufferedReader input;
    private String username;

    public ClientMirror(Server server, Socket clientSocket) throws IOException {
       this.server = server;
       this.clientSocket = clientSocket;
    }

    @Override
    public void run() {

        try {
            output = new DataOutputStream(clientSocket.getOutputStream());
            input = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            getUsername();
            gameOn();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public boolean gameOn (){

        String confirmGame = getUsername() + ", are you ready to find a whole lot of bombs?(yes/no)";

        try {
            output.write(confirmGame.getBytes());
            output.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }

        if(confirmGame.equals("yes".toUpperCase())){
            return true;
        } else {
            return false;
        }
    }

   public String getUsername () {

       String getUsername = "Please, tell me your name: ";

       try {
           output.write(getUsername.getBytes());
           output.flush();
           Thread.currentThread().setName(input.readLine());
           username = Thread.currentThread().getName();

       } catch (IOException e) {
           e.printStackTrace();
       }

       return username;
   }

   public void sendMessage (String[][] matrix) {

       try {
           output.write(matrix.toString().getBytes());
           output.write("\n".getBytes());// verificar se é usado
           output.flush();
       } catch (IOException e) {
           e.printStackTrace();
       }

   }

}
