package org.academiadecodigo.minesweeper.server;

import java.io.*;
import java.net.Socket;

/**
 * Created by codecadet on 14/11/16.
 */
public class ClientMirror implements Runnable {

    private Server server;
    private Socket clientSocket;
    private DataOutputStream output;
    private BufferedOutputStream output2;
    private BufferedReader input;
    private String username;
    private boolean gameMode;

    public ClientMirror(Server server, Socket clientSocket) throws IOException {
        this.server = server;
        this.clientSocket = clientSocket;
    }

    @Override
    public void run() {

        try {
            output = new DataOutputStream(clientSocket.getOutputStream());
            //output2 = new BufferedOutputStream(new DataOutputStream(clientSocket.getOutputStream()));
            input = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            gameTitle();
            setUsername();
            gameOn();


        } catch (IOException e) {
            e.printStackTrace();
        }

        String clientMessage = "";

        while (!clientSocket.isClosed()) {
            try {
                clientMessage = input.readLine();

                if (clientMessage == null) {
                    server.exitGame(username);
                    clientSocket.close();
                } else {
                    if (!gameMode){
                        server.broadcast(clientMessage);
                    } else {
                        //método que recebe clientMessage e devolve array bytes da matrix actualizada (GameEngine.getRepresentableMatrix())
                        server.broadcastMatrix(clientMessage.getBytes());
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    public void gameTitle () {

       String title = "____   ____                                _____  .__                         \n" +
                "\\   \\ /   /____ ______________   ____     /     \\ |__| ____ _____    ______   \n" +
                " \\   Y   /\\__  \\\\_  __ \\_  __ \\_/ __ \\   /  \\ /  \\|  |/    \\\\__  \\  /  ___/   \n" +
                "  \\     /  / __ \\|  | \\/|  | \\/\\  ___/  /    Y    \\  |   |  \\/ __ \\_\\___ \\    \n" +
                "   \\___/  (____  /__|   |__|    \\___  > \\____|__  /__|___|  (____  /____  >   \n" +
                "               \\/                   \\/          \\/        \\/     \\/     \\/    \n" +
                "                                      __                                      \n" +
                "                              _____  |  | _______                             \n" +
                "                              \\__  \\ |  |/ /\\__  \\                            \n" +
                "                               / __ \\|    <  / __ \\_                          \n" +
                "                              (____  /__|_ \\(____  /                          \n" +
                "                                   \\/     \\/     \\/                           \n" +
                "   _____  .__                                                                 \n" +
                "  /     \\ |__| ____   ____   ________  _  __ ____   ____ ______   ___________ \n" +
                " /  \\ /  \\|  |/    \\_/ __ \\ /  ___/\\ \\/ \\/ // __ \\_/ __ \\\\____ \\_/ __ \\_  __ \\\n" +
                "/    Y    \\  |   |  \\  ___/ \\___ \\  \\     /\\  ___/\\  ___/|  |_> >  ___/|  | \\/\n" +
                "\\____|__  /__|___|  /\\___  >____  >  \\/\\_/  \\___  >\\___  >   __/ \\___  >__|   \n" +
                "        \\/        \\/     \\/     \\/              \\/     \\/|__|        \\/     ";
        try {
            output.write(title.getBytes());
            output.write("\n \n \n".getBytes());
            output.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void setUsername() {

        String getUsername = "Welcome my brave friend! Let's play a GAME! But first...TELL ME YOUR NAME!!! ";

        try {
            output.write(getUsername.getBytes());
            output.write("\n".getBytes());
            output.flush();
            Thread.currentThread().setName(input.readLine());
            username = Thread.currentThread().getName();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void gameOn() {

        String confirmGame = username + ", are you ready to find a whole lot of bombs?(yes/no)";
        String answer = "";

        try {
            output.write(confirmGame.getBytes());
            output.write("\n".getBytes());
            output.flush();
            answer = input.readLine();
            System.out.println(answer);

        } catch (IOException e) {
            e.printStackTrace();
        }

       if (answer.equals("yes")) {
           System.out.println("entrei aqui");
           gameMode = true;
        } else {
           System.out.println("fiz borrada");
           gameMode=false;
        }
    }

    public void sendMessage(String message) {

        try {
            output.write(message.getBytes());
            output.write("\n".getBytes());
            output.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public void sendMatrix(byte[] matrixLine){
        try {
            output.write(matrixLine);
            output.write("\n".getBytes());
            output.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String getUsername() {
        return username;
    }

    public boolean isGameMode() {
        return gameMode;
    }
}
