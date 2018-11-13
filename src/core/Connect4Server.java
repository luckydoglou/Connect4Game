package core;

import java.io.*;
import java.net.*;
import java.util.Date;
import java.util.Random;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;

import core.Connect4;
import core.Connect4ComputerPlayer;

/**
 * This class deal with connect 4 game logic, communicate with clients.
 * 
 * @author Xiaolou Huang (Lor)
 * @version 11/12/2018
 *
 */
public class Connect4Server extends Application {

    public static int PLAYER1 = 1; // Indicate player 1
    public static int PLAYER2 = 2; // Indicate player 2
    public static int PLAYER1_WON = 1; // Indicate player 1 won
    public static int PLAYER2_WON = 2; // Indicate player 2 won
    public static int DRAW = 3; // Indicate a draw
    public static int CONTINUE = 4; // Indicate to continue

    private int sessionNo = 1; // Number a session

    // ----------------------------
    public final int COLS = 7;
    public final int ROWS = 6;

    // player 1
    Socket player1;
    DataInputStream fromPlayer1;
    DataOutputStream toPlayer1;
    // player 2
    Socket player2;
    DataInputStream fromPlayer2;
    DataOutputStream toPlayer2;
    // ----------------------------

    @Override // Override the start method in the Application class
    public void start(Stage primaryStage) {
        TextArea taLog = new TextArea();

        // Create a scene and place it in the stage
        Scene scene = new Scene(new ScrollPane(taLog), 450, 200);
        primaryStage.setTitle("Connect4Server"); // Set the stage title
        primaryStage.setScene(scene); // Place the scene in the stage
        primaryStage.show(); // Display the stage

        new Thread(() -> {
            try {
                boolean isPair = false;
                // Create a server socket
                @SuppressWarnings("resource")
                ServerSocket serverSocket = new ServerSocket(8000);
                Platform.runLater(() -> taLog.appendText(new Date() + ": Server started at socket 8000\n"));

                // Ready to create a session for every two players
                while (true) {

                    Socket player = serverSocket.accept();
                    // Create data input and output streams
                    DataInputStream fromPlayer = new DataInputStream(player.getInputStream());
                    DataOutputStream toPlayer = new DataOutputStream(player.getOutputStream());
                    char option = fromPlayer.readChar();

                    // if user choose to play with computer
                    if (option == 'C') {
                        Platform.runLater(() -> taLog.appendText(new Date() + ": Start playing with computer \n"));

                        // Launch a new thread for this session of two players
                        new Thread(new AgainstComputer(fromPlayer, toPlayer)).start();

                        System.out.println("---start thread with computer");
                    }

                    // if user choose to play against players
                    else if (option == 'P') {
                        Platform.runLater(() -> taLog
                                .appendText(new Date() + ": Wait for players to join session " + sessionNo + '\n'));

                        if (isPair == false) {
                            // Connect to player 1
                            player1 = player;
                            fromPlayer1 = fromPlayer;
                            toPlayer1 = toPlayer;

                            Platform.runLater(() -> {
                                taLog.appendText(new Date() + ": Player 1 joined session " + sessionNo + '\n');
                                taLog.appendText(
                                        "Player 1's IP address" + player1.getInetAddress().getHostAddress() + '\n');
                            });

                            // Notify that the player is Player 1
                            new DataOutputStream(player1.getOutputStream()).writeInt(PLAYER1);
                            isPair = true;
                        }

                        // Connect to player 2
                        else if (isPair == true) {
                            player2 = player;
                            fromPlayer2 = fromPlayer;
                            toPlayer2 = toPlayer;

                            Platform.runLater(() -> {
                                taLog.appendText(new Date() + ": Player 2 joined session " + sessionNo + '\n');
                                taLog.appendText(
                                        "Player 2's IP address" + player2.getInetAddress().getHostAddress() + '\n');
                            });

                            // Notify that the player is Player 2
                            new DataOutputStream(player2.getOutputStream()).writeInt(PLAYER2);

                            // Display this session and increment session number
                            Platform.runLater(() -> taLog
                                    .appendText(new Date() + ": Start a thread for session " + sessionNo++ + '\n'));

                            // Launch a new thread for this session of two players
                            new Thread(new AgainstPlayer(fromPlayer1, toPlayer1, fromPlayer2, toPlayer2)).start();
                            isPair = false;
                        }
                    }
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }).start();
    }

    // ---------------------------------------------------------------------------------------------
    // -----Against Player-----
    // ---------------------------------------------------------------------------------------------
    // Define the thread class for handling a new session for two players
    /**
     * This class contains procedures when client choose to play against other
     * player.
     *
     */
    class AgainstPlayer implements Runnable {

        // initialize game logic
        Connect4 player = new Connect4();

        private DataInputStream fromPlayer1;
        private DataOutputStream toPlayer1;
        private DataInputStream fromPlayer2;
        private DataOutputStream toPlayer2;

        /** Construct a thread */
        public AgainstPlayer(DataInputStream fromPlayer1, DataOutputStream toPlayer1, DataInputStream fromPlayer2,
                DataOutputStream toPlayer2) {
            this.fromPlayer1 = fromPlayer1;
            this.toPlayer1 = toPlayer1;
            this.fromPlayer2 = fromPlayer2;
            this.toPlayer2 = toPlayer2;
        }

        /** Implement the run() method for the thread */
        public void run() {
            try {

                // Write anything to notify player 1 to start
                // This is just to let player 1 know to start
                toPlayer1.writeInt(1);

                // Continuously serve the players and determine and report
                // the game status to the players
                while (true) {
                    // gameState
                    Connect4.gameState gameState;

                    // -----player 1-----
                    int c1 = fromPlayer1.readInt(); // receive which button clicked
                    int r1 = player.getFirstEmptyRow(c1); // find out which row to setToken
                    toPlayer1.writeInt(r1); // write back the row to player 1

                    // Receive a move from player 1
                    int row = fromPlayer1.readInt();
                    int column = fromPlayer1.readInt();
                    gameState = player.updateGrid(column, 'X');

                    // Check if Player 1 wins
                    if (gameState == Connect4.gameState.XWin) {
                        toPlayer1.writeInt(PLAYER1_WON);
                        toPlayer2.writeInt(PLAYER1_WON);
                        sendMove(toPlayer2, row, column);
                        break; // Break the loop
                    } else if (gameState == Connect4.gameState.DRAW) { // Check if all cells are filled
                        toPlayer1.writeInt(DRAW);
                        toPlayer2.writeInt(DRAW);
                        sendMove(toPlayer2, row, column);
                        break;
                    } else {
                        // Notify player 2 to take the turn
                        toPlayer2.writeInt(CONTINUE);

                        // Send player 1's selected row and column to player 2
                        sendMove(toPlayer2, row, column);
                    }

                    // -----player 2-----
                    int c2 = fromPlayer2.readInt(); // receive which button clicked
                    int r2 = player.getFirstEmptyRow(c2); // find out which row to setToken
                    toPlayer2.writeInt(r2); // write back the row to player 1

                    // Receive a move from Player 2
                    row = fromPlayer2.readInt();
                    column = fromPlayer2.readInt();
                    gameState = player.updateGrid(column, 'O');

                    // Check if Player 2 wins
                    if (gameState == Connect4.gameState.OWin) {
                        toPlayer1.writeInt(PLAYER2_WON);
                        toPlayer2.writeInt(PLAYER2_WON);
                        sendMove(toPlayer1, row, column);
                        break;
                    } else {
                        // Notify player 1 to take the turn
                        toPlayer1.writeInt(CONTINUE);

                        // Send player 2's selected row and column to player 1
                        sendMove(toPlayer1, row, column);
                    }
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }

        /** Send the move to player. */
        private void sendMove(DataOutputStream out, int row, int column) throws IOException {
            out.writeInt(row); // Send row index
            out.writeInt(column); // Send column index
        }
    }

    // ---------------------------------------------------------------------------------------------
    // -----Against Computer-----
    // ---------------------------------------------------------------------------------------------
    // Define the thread class for handling a new session for two players
    /**
     * This class contains procedures to play against computer.
     *
     */
    class AgainstComputer implements Runnable {

        // initialize game logic
        Connect4ComputerPlayer computer = new Connect4ComputerPlayer();

        private DataInputStream fromPlayer;
        private DataOutputStream toPlayer;

        /** Construct a thread */
        public AgainstComputer(DataInputStream fromPlayer, DataOutputStream toPlayer) {
            this.fromPlayer = fromPlayer;
            this.toPlayer = toPlayer;
        }

        /** Implement the run() method for the thread */
        public void run() {
            try {

                // Write anything to notify player 1 to start
                // This is just to let player 1 know to start
                toPlayer.writeInt(1);

                // Continuously serve the players and determine and report
                // the game status to the players
                while (true) {
                    // gameState
                    Connect4ComputerPlayer.gameState gameState;

                    // -----Player-----
                    int c1 = fromPlayer.readInt(); // receive which button clicked
                    int r1 = computer.getFirstEmptyRow(c1); // find out which row to setToken
                    toPlayer.writeInt(r1); // write back the row to player 1

                    // Receive a move from player 1
                    @SuppressWarnings("unused")
                    int row = fromPlayer.readInt();
                    int column = fromPlayer.readInt();
                    gameState = computer.updateGrid(column, 'X');

                    // Check if Player wins
                    if (gameState == Connect4ComputerPlayer.gameState.XWin) {
                        toPlayer.writeInt(PLAYER1_WON);
                        break; // Break the loop
                    } else if (gameState == Connect4ComputerPlayer.gameState.DRAW) { // Check if all cells are filled
                        toPlayer.writeInt(DRAW);
                        break;
                    } else {

                    }

                    // -----Computer-----
                    Random rand = new Random();
                    int c2;
                    int r2;
                    do {
                        c2 = Math.abs(rand.nextInt() % COLS); // generate the column
                        r2 = computer.getFirstEmptyRow(c2); // find out which row to setToken
                    } while (r2 == -1);

                    // Generate a move from computer
                    gameState = computer.updateGrid(c2, 'O');

                    // Check if Player 2 wins
                    if (gameState == Connect4ComputerPlayer.gameState.OWin) {
                        toPlayer.writeInt(PLAYER2_WON);
                        sendMove(toPlayer, r2, c2);
                        break;
                    } else if (gameState == Connect4ComputerPlayer.gameState.DRAW) { // Check if all cells are filled
                        toPlayer.writeInt(DRAW);
                        break;
                    } else {
                        // Notify player 1 to take the turn
                        toPlayer.writeInt(CONTINUE);

                        // Send player 2's selected row and column to player 1
                        sendMove(toPlayer, r2, c2);
                    }
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }

        /** Send the move to player. */
        private void sendMove(DataOutputStream out, int row, int column) throws IOException {
            out.writeInt(row); // Send row index
            out.writeInt(column); // Send column index
        }
    }

    /**
     * The main method is only needed for the IDE with limited JavaFX support. Not
     * needed for running from the command line.
     * 
     * @param args arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
}