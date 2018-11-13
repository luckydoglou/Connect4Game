package ui;

import java.io.*;
import java.net.*;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Ellipse;
import javafx.scene.shape.Line;
import javafx.stage.Stage;

/**
 * This class contains the client side procedures when play against computer or other players.
 * 
 * @author Xiaolou Huang (Lor)
 * @version 11/12/2018
 */
public class Connect4GUI extends Application {

    public static int PLAYER1 = 1; // Indicate player 1
    public static int PLAYER2 = 2; // Indicate player 2
    public static int PLAYER1_WON = 1; // Indicate player 1 won
    public static int PLAYER2_WON = 2; // Indicate player 2 won
    public static int DRAW = 3; // Indicate a draw
    public static int CONTINUE = 4; // Indicate to continue

    // Indicate whether the player has the turn
    private boolean myTurn = false;

    // Indicate the token for the player
    private char myToken = ' ';

    // Indicate the token for the other player
    private char otherToken = ' ';

    // ------------------------------------------------------
    Scene mainScene, playerScene, computerScene;

    public final int COLS = 7;
    public final int ROWS = 6;
    // Create and initialize cell
    public Cell[][] cell = new Cell[ROWS][COLS];
    // ------------------------------------------------------

    // Create and initialize a title label
    private Label lblTitle = new Label();

    // Create and initialize a status label
    private Label lblStatus = new Label();

    // Indicate selected row and column by the current move
    private int rowSelected;
    private int columnSelected;

    // Input and output streams from/to server
    private DataInputStream fromServer;
    private DataOutputStream toServer;

    // Continue to play?
    private boolean continueToPlay = true;

    // Wait for the player to mark a cell
    private boolean waiting = true;

    // Host name or ip
    private String host = "localhost";

    @Override // Override the start method in the Application class
    public void start(Stage primaryStage) {

        // set up connection between client and server
        try {
            // Create a socket to connect to the server
            @SuppressWarnings("resource")
            Socket socket = new Socket(host, 8000);

            // Create an input stream to receive data from the server
            fromServer = new DataInputStream(socket.getInputStream());

            // Create an output stream to send data to the server
            toServer = new DataOutputStream(socket.getOutputStream());
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        // ---------------------------------------------------------------------------------------
        // player scene
        Button player_btn = new Button("Player");
        player_btn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                try {
                    toServer.writeChar('P');
                    playerScene = player_scene();
                    primaryStage.setScene(playerScene);
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        });

        // ---------------------------------------------------------------------------------------
        // computer scene
        Button computer_btn = new Button("Computer");
        computer_btn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                try {
                    toServer.writeChar('C');
                    computerScene = computer_scene();
                    primaryStage.setScene(computerScene);
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        });

        // ---------------------------------------------------------------------------------------
        // main Scene
        HBox hbox = new HBox(20);
        hbox.getChildren().addAll(player_btn, computer_btn);
        hbox.setPadding(new Insets(50, 50, 50, 50));
        VBox vbox = new VBox(20);
        vbox.setPadding(new Insets(50, 50, 50, 50));
        vbox.getChildren().addAll(new Label("Choose to play with player or computer"), hbox);
        StackPane stackPane = new StackPane();
        stackPane.setAlignment(Pos.CENTER);
        stackPane.getChildren().add(vbox);
        mainScene = new Scene(stackPane);

        primaryStage.setScene(mainScene);
        primaryStage.setTitle("Connect4GUI");
        primaryStage.show();
        
    }

    // ---------------------------------------------------------------------------------------------
    // -----Player Scene-----
    // ---------------------------------------------------------------------------------------------
    private Scene player_scene() {
        // Connect to the server
        connectToServerAgainstPlayer();

        Button col1 = new Button("col1");
        // mouse click
        col1.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                int row = 0;
                try {
                    toServer.writeInt(0); // ask server what is available row for this column
                    row = fromServer.readInt(); // available row from server
                } catch (IOException e) {
                    e.printStackTrace();
                }
                cell[row][0].handleButtonClick(row, 0); // display on screen
            }
        });

        Button col2 = new Button("col2");
        col2.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                int row = 0;
                try {
                    toServer.writeInt(1); // ask server what is available row for this column
                    row = fromServer.readInt(); // available row from server
                } catch (IOException e) {
                    e.printStackTrace();
                }
                cell[row][1].handleButtonClick(row, 1); // display on screen
            }
        });

        Button col3 = new Button("col3");
        col3.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                int row = 0;
                try {
                    toServer.writeInt(2); // ask server what is available row for this column
                    row = fromServer.readInt(); // available row from server
                } catch (IOException e) {
                    e.printStackTrace();
                }
                cell[row][2].handleButtonClick(row, 2); // display on screen
            }
        });

        Button col4 = new Button("col4");
        col4.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent event) {
                int row = 0;
                try {
                    toServer.writeInt(3); // ask server what is available row for this column
                    row = fromServer.readInt(); // available row from server
                } catch (IOException e) {
                    e.printStackTrace();
                }
                cell[row][3].handleButtonClick(row, 3);
            }
        });

        Button col5 = new Button("col5");
        col5.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent event) {
                int row = 0;
                try {
                    toServer.writeInt(4); // ask server what is available row for this column
                    row = fromServer.readInt(); // available row from server
                } catch (IOException e) {
                    e.printStackTrace();
                }
                cell[row][4].handleButtonClick(row, 4);
            }
        });

        Button col6 = new Button("col6");
        col6.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent event) {
                int row = 0;
                try {
                    toServer.writeInt(5); // ask server what is available row for this column
                    row = fromServer.readInt(); // available row from server
                } catch (IOException e) {
                    e.printStackTrace();
                }
                cell[row][5].handleButtonClick(row, 5);
            }
        });

        Button col7 = new Button("col7");
        col7.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent event) {
                int row = 0;
                try {
                    toServer.writeInt(6); // ask server what is available row for this column
                    row = fromServer.readInt(); // available row from server
                } catch (IOException e) {
                    e.printStackTrace();
                }
                cell[row][6].handleButtonClick(row, 6);
            }
        });

        GridPane gridPane = new GridPane();
        gridPane.setPadding(new Insets(10, 10, 10, 10));
        gridPane.add(col1, 0, 0);
        gridPane.add(col2, 1, 0);
        gridPane.add(col3, 2, 0);
        gridPane.add(col4, 3, 0);
        gridPane.add(col5, 4, 0);
        gridPane.add(col6, 5, 0);
        gridPane.add(col7, 6, 0);

        for (int c = 0; c < COLS; c++)
            for (int r = 1; r < ROWS + 1; r++)
                gridPane.add(cell[r - 1][c] = new Cell(r, c), c, r);

        BorderPane borderPane = new BorderPane();
        borderPane.setTop(lblTitle);
        borderPane.setCenter(gridPane);
        borderPane.setBottom(lblStatus);

        playerScene = new Scene(borderPane);
        return playerScene;

    }

    // ---------------------------------------------------------------------------------------------
    // -----Computer Scene-----
    // ---------------------------------------------------------------------------------------------
    private Scene computer_scene() {

        // Connect to the server
        connectToServerAgainstComputer();

        Button col1 = new Button("col1");
        // mouse click
        col1.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent event) {
                int row = 0;
                try {
                    toServer.writeInt(0); // ask server what is available row for this column
                    row = fromServer.readInt(); // available row from server
                } catch (IOException e) {
                    e.printStackTrace();
                }
                cell[row][0].handleButtonClick(row, 0); // display on screen
            }
        });

        Button col2 = new Button("col2");
        col2.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent event) {
                int row = 0;
                try {
                    toServer.writeInt(1); // ask server what is available row for this column
                    row = fromServer.readInt(); // available row from server
                } catch (IOException e) {
                    e.printStackTrace();
                }
                cell[row][1].handleButtonClick(row, 1); // display on screen
            }
        });

        Button col3 = new Button("col3");
        col3.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent event) {
                int row = 0;
                try {
                    toServer.writeInt(2); // ask server what is available row for this column
                    row = fromServer.readInt(); // available row from server
                } catch (IOException e) {
                    e.printStackTrace();
                }
                cell[row][2].handleButtonClick(row, 2); // display on screen
            }
        });

        Button col4 = new Button("col4");
        col4.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent event) {
                int row = 0;
                try {
                    toServer.writeInt(3); // ask server what is available row for this column
                    row = fromServer.readInt(); // available row from server
                } catch (IOException e) {
                    e.printStackTrace();
                }
                cell[row][3].handleButtonClick(row, 3); // display on screen
            }
        });

        Button col5 = new Button("col5");
        col5.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent event) {
                int row = 0;
                try {
                    toServer.writeInt(4); // ask server what is available row for this column
                    row = fromServer.readInt(); // available row from server
                } catch (IOException e) {
                    e.printStackTrace();
                }
                cell[row][4].handleButtonClick(row, 4); // display on screen
            }
        });

        Button col6 = new Button("col6");
        col6.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent event) {
                int row = 0;
                try {
                    toServer.writeInt(5); // ask server what is available row for this column
                    row = fromServer.readInt(); // available row from server
                } catch (IOException e) {
                    e.printStackTrace();
                }
                cell[row][5].handleButtonClick(row, 5); // display on screen
            }
        });

        Button col7 = new Button("col7");
        col7.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent event) {
                int row = 0;
                try {
                    toServer.writeInt(6); // ask server what is available row for this column
                    row = fromServer.readInt(); // available row from server
                } catch (IOException e) {
                    e.printStackTrace();
                }
                cell[row][6].handleButtonClick(row, 6); // display on screen
            }
        });

        GridPane gridPane = new GridPane();
        gridPane.setPadding(new Insets(10, 10, 10, 10));
        gridPane.add(col1, 0, 0);
        gridPane.add(col2, 1, 0);
        gridPane.add(col3, 2, 0);
        gridPane.add(col4, 3, 0);
        gridPane.add(col5, 4, 0);
        gridPane.add(col6, 5, 0);
        gridPane.add(col7, 6, 0);

        for (int c = 0; c < COLS; c++)
            for (int r = 1; r < ROWS + 1; r++)
                gridPane.add(cell[r - 1][c] = new Cell(r, c), c, r);

        BorderPane borderPane = new BorderPane();
        borderPane.setTop(lblTitle);
        borderPane.setCenter(gridPane);
        borderPane.setBottom(lblStatus);

        computerScene = new Scene(borderPane);
        return computerScene;

    }

    // ---------------------------------------------------------------------------------------------
    // -----Server Logic Against Player-----
    // ---------------------------------------------------------------------------------------------
    private void connectToServerAgainstPlayer() {

        // Control the game on a separate thread
        new Thread(() -> {
            try {
                // Get notification from the server
                int player = fromServer.readInt();

                // Am I player 1 or 2?
                if (player == PLAYER1) {
                    myToken = 'X';
                    otherToken = 'O';
                    Platform.runLater(() -> {
                        lblTitle.setText("Player 1 with token 'X'");
                        lblStatus.setText("Waiting for player 2 to join");
                    });

                    // Receive startup notification from the server
                    fromServer.readInt(); // Whatever read is ignored

                    // The other player has joined
                    Platform.runLater(() -> lblStatus.setText("Player 2 has joined. I start first"));

                    // It is my turn
                    myTurn = true;
                } else if (player == PLAYER2) {
                    myToken = 'O';
                    otherToken = 'X';
                    Platform.runLater(() -> {
                        lblTitle.setText("Player 2 with token 'O'");
                        lblStatus.setText("Waiting for player 1 to move");
                    });
                }

                // Continue to play
                while (continueToPlay) {
                    if (player == PLAYER1) {
                        waitForPlayerAction(); // Wait for player 1 to move
                        sendMove(); // Send the move to the server
                        receiveInfoFromServer_againstPlayer(); // Receive info from the server
                    } else if (player == PLAYER2) {
                        receiveInfoFromServer_againstPlayer(); // Receive info from the server
                        waitForPlayerAction(); // Wait for player 2 to move
                        sendMove(); // Send player 2's move to the server
                    }
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }).start();
    }

    // ---------------------------------------------------------------------------------------------
    // -----Server Logic Against Computer-----
    // ---------------------------------------------------------------------------------------------
    private void connectToServerAgainstComputer() {

        // Control the game on a separate thread
        new Thread(() -> {
            try {
                    myToken = 'X';
                    otherToken = 'O';
                    Platform.runLater(() -> {
                        lblTitle.setText("Player with token 'X'");
                    });

                    // Receive startup notification from the server
                    fromServer.readInt(); // Whatever read is ignored

                    // The other player has joined
                    Platform.runLater(() -> lblStatus.setText("I start first"));

                    // It is my turn
                    myTurn = true;

                // Continue to play
                while (continueToPlay) {
                        waitForPlayerAction(); // Wait for player 1 to move
                        sendMove(); // Send the move to the server
                        receiveInfoFromServer_againstComputer(); // Receive info from the server
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }).start();
    }

    /** Wait for the player to mark a cell */
    private void waitForPlayerAction() throws InterruptedException {
        while (waiting) {
            Thread.sleep(100);
        }

        waiting = true;
    }

    /** Send this player's move to the server */
    private void sendMove() throws IOException {
        toServer.writeInt(rowSelected); // Send the selected row
        toServer.writeInt(columnSelected); // Send the selected column
    }

    // ---------------------------------------------------------------------------------------------
    // -----Receive Info From Server Against Player-----
    // ---------------------------------------------------------------------------------------------
    /** Receive info from the server */
    private void receiveInfoFromServer_againstPlayer() throws IOException {
        // Receive game status
        int status = fromServer.readInt();

        if (status == PLAYER1_WON) {
            // Player 1 won, stop playing
            continueToPlay = false;
            if (myToken == 'X') {
                Platform.runLater(() -> lblStatus.setText("I won! (X)"));
            } else if (myToken == 'O') {
                Platform.runLater(() -> lblStatus.setText("Player 1 (X) has won!"));
                receiveMove();
            }
        } else if (status == PLAYER2_WON) {
            // Player 2 won, stop playing
            continueToPlay = false;
            if (myToken == 'O') {
                Platform.runLater(() -> lblStatus.setText("I won! (O)"));
            } else if (myToken == 'X') {
                Platform.runLater(() -> lblStatus.setText("Player 2 (O) has won!"));
                receiveMove();
            }
        } else if (status == DRAW) {
            // No winner, game is over
            continueToPlay = false;
            Platform.runLater(() -> lblStatus.setText("Game is over, no winner!"));

            if (myToken == 'O') {
                receiveMove();
            }
        } else {
            receiveMove();
            Platform.runLater(() -> lblStatus.setText("My turn"));
            myTurn = true; // It is my turn
        }
    }

    // ---------------------------------------------------------------------------------------------
    // -----Receive Info From Server Against Computer-----
    // ---------------------------------------------------------------------------------------------
    /** Receive info from the server */
    private void receiveInfoFromServer_againstComputer() throws IOException {
        // Receive game status
        int status = fromServer.readInt();

        if (status == PLAYER1_WON) {
            // Player 1 won, stop playing
            continueToPlay = false;
                Platform.runLater(() -> lblStatus.setText("I won! (X)"));
        } else if (status == PLAYER2_WON) {
            // Player 2 won, stop playing
            continueToPlay = false;
                Platform.runLater(() -> lblStatus.setText("Computer (O) has won!"));
                receiveMove();
        } else if (status == DRAW) {
            // No winner, game is over
            continueToPlay = false;
            Platform.runLater(() -> lblStatus.setText("Game is over, no winner!"));
        } else {
            receiveMove();
            Platform.runLater(() -> lblStatus.setText("My turn"));
            myTurn = true; // It is my turn
        }
    }

    private void receiveMove() throws IOException {
        // Get the other player's move
        int row = fromServer.readInt();
        int column = fromServer.readInt();
        Platform.runLater(() -> cell[row][column].setToken(otherToken));
    }

    // An inner class for a cell
    public class Cell extends Pane {
        // Indicate the row and column of this cell in the board
        private int row;
        private int column;

        // Token used for this cell
        private char token = ' ';

        public Cell(int row, int column) {
            this.row = row;
            this.column = column;
            this.setPrefSize(50, 50); // What happens without this?
            setStyle("-fx-border-color: black"); // Set cell's border
            // this.setOnMouseClicked(e -> handleMouseClick());

        }

        /** 
         * Return token 
         * 
         * @return the token
         */
        public char getToken() {
            return token;
        }

        /** 
         * Set a new token 
         * 
         * @param c set token
         */
        public void setToken(char c) {
            token = c;
            repaint();
        }

        protected void repaint() {
            if (token == 'X') {
                Line line1 = new Line(10, 10, this.getWidth() - 10, this.getHeight() - 10);
                line1.endXProperty().bind(this.widthProperty().subtract(10));
                line1.endYProperty().bind(this.heightProperty().subtract(10));
                Line line2 = new Line(10, this.getHeight() - 10, this.getWidth() - 10, 10);
                line2.startYProperty().bind(this.heightProperty().subtract(10));
                line2.endXProperty().bind(this.widthProperty().subtract(10));

                // Add the lines to the pane
                this.getChildren().addAll(line1, line2);
            } else if (token == 'O') {
                Ellipse ellipse = new Ellipse(this.getWidth() / 2, this.getHeight() / 2, this.getWidth() / 2 - 10,
                        this.getHeight() / 2 - 10);
                ellipse.centerXProperty().bind(this.widthProperty().divide(2));
                ellipse.centerYProperty().bind(this.heightProperty().divide(2));
                ellipse.radiusXProperty().bind(this.widthProperty().divide(2).subtract(10));
                ellipse.radiusYProperty().bind(this.heightProperty().divide(2).subtract(10));
                ellipse.setStroke(Color.BLACK);
                ellipse.setFill(Color.WHITE);

                getChildren().add(ellipse); // Add the ellipse to the pane
            }
        }

        /* Handle a mouse click event */
        public void handleButtonClick(int r, int c) {
            // define which row and column
            row = r;
            column = c;
            // If cell is not occupied and the player has the turn
            if (token == ' ' && myTurn) {
                setToken(myToken); // Set the player's token in the cell
                myTurn = false;
                rowSelected = row;
                columnSelected = column;
                lblStatus.setText("Waiting for the other player to move");
                waiting = false; // Just completed a successful move
            }
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