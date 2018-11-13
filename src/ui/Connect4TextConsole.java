package ui;

import java.util.Random;
import java.util.Scanner;

import core.Connect4;
import core.Connect4ComputerPlayer;
import javafx.application.Application;

/**
 * Driver class to test Connect 4 game implementation and to play the game in
 * console.
 * 
 * @author Xiaolou Huang (Lor)
 * @version 9/29/2018
 *
 */
public class Connect4TextConsole {

    @SuppressWarnings("resource")
    public static void main(String[] args) {

        // Variable
        String input; // store user's input.
        char playerX = 'X'; // player X.
        char playerO = 'O'; // player O.
        int inputX; // input from X player.
        int inputO; // input from O player.

        // Scanner, Random
        Scanner scan = new Scanner(System.in);
        Random rand = new Random();

        System.out.println("Enter 0 to play the game in GUI, or enter 1 for text console");
        input = scan.nextLine();

        // Go to GUI
        if (input.equals("0")) {
            
            Application.launch(Connect4GUI.class,args);
            
        } else {

            // beginning of the game.
            System.out.println(
                    "Begin Game, Enter ‘P’ if you want to play against another player; enter ‘C’ to play against computer.");
            input = scan.nextLine();

            // exception handling
            try {
                if (input.equals("C") || input.equals("P")) {
                } else {
                    throw new NumberFormatException();
                }
            } catch (Exception e) {
                System.out.println("Exception is caught, incorrect character input, should be C or P");
                System.exit(0);
            }

            // if play with computer
            if (input.charAt(0) == 'C') {

                Connect4ComputerPlayer game = new Connect4ComputerPlayer();
                Connect4ComputerPlayer.gameState state;
                game.printGrid(); // print the game.
                System.out.println("Start game against computer");

                do {

                    // player X
                    System.out.println("It's your turn. Choose a column number from 1 - 7.");
                    input = scan.nextLine();

                    // exception handling
                    try {
                        Integer.parseInt(input);
                    } catch (NumberFormatException e) {
                        System.out.println("Exception is caught, incorrect integer input, should be number 1 to 7");
                        System.exit(0);
                    }

                    inputX = Integer.parseInt(input);
                    state = game.updateGrid(inputX, playerX);

                    // if the inputX is invalid, ask again.
                    while (state == Connect4ComputerPlayer.gameState.INVALID
                            || state == Connect4ComputerPlayer.gameState.FULL) {
                        if (state == Connect4ComputerPlayer.gameState.INVALID) {
                            System.out.println("Invalid input, PlayerX please choose a column number from 1 - 7.");
                        } else {
                            System.out.println("Column is full, PlayerX please choose another column.");
                        }
                        input = scan.nextLine();

                        // exception handling
                        try {
                            inputX = Integer.parseInt(input);
                        } catch (NumberFormatException e) {
                            System.out.println("Exception is caught, incorrect input.");
                            System.exit(0);
                        }

                        state = game.updateGrid(inputX, playerX);
                    }

                    game.printGrid(); // print the game.

                    // check the state
                    if (state == Connect4ComputerPlayer.gameState.XWin) {
                        System.out.println("Player X won the game.");
                        break;
                    } else if (state == Connect4ComputerPlayer.gameState.OWin) {
                        System.out.println("Player O won the game.");
                        break;
                    } else if (state == Connect4ComputerPlayer.gameState.DRAW) {
                        System.out.println("It is draw, end of the game.");
                        break;
                    }

                    // player O, computer player
                    System.out.println("Computer has made a move.");
                    inputO = rand.nextInt() % 7 + 1;
                    state = game.updateGrid(inputO, playerO);

                    // if the inputO is invalid, ask again.
                    while (state == Connect4ComputerPlayer.gameState.INVALID) {
                        inputO = rand.nextInt() % 7 + 1;
                        state = game.updateGrid(inputO, playerO);
                    }

                    game.printGrid();

                    // check the state
                    if (state == Connect4ComputerPlayer.gameState.XWin) {
                        System.out.println("Player X won the game.");
                        break;
                    } else if (state == Connect4ComputerPlayer.gameState.OWin) {
                        System.out.println("Computer won the game.");
                        break;
                    } else if (state == Connect4ComputerPlayer.gameState.DRAW) {
                        System.out.println("It is draw, end of the game.");
                        break;
                    }

                } while (state == Connect4ComputerPlayer.gameState.CONTINUE);

            }

            // ----------------------------------------------------------------------------------------------------
            // play with other player
            else {

                Connect4 game = new Connect4();
                Connect4.gameState state;
                game.printGrid(); // print the game.

                do {

                    // player X
                    System.out.println("PlayerX - your turn. Choose a column number from 1 - 7.");
                    input = scan.nextLine();

                    // exception handling
                    try {
                        Integer.parseInt(input);
                    } catch (NumberFormatException e) {
                        System.out.println("Exception is caught, incorrect input.");
                        System.exit(0);
                    }

                    inputX = Integer.parseInt(input);
                    state = game.updateGrid(inputX, playerX);

                    // if the inputX is invalid, ask again.
                    while (state == Connect4.gameState.INVALID || state == Connect4.gameState.FULL) {
                        if (state == Connect4.gameState.INVALID) {
                            System.out.println("Invalid input, PlayerX please choose a column number from 1 - 7.");
                        } else {
                            System.out.println("Column is full, PlayerX please choose another column.");
                        }
                        input = scan.nextLine();

                        // exception handling
                        try {
                            Integer.parseInt(input);
                        } catch (NumberFormatException e) {
                            System.out.println("Exception is caught, incorrect input.");
                            System.exit(0);
                        }

                        inputX = Integer.parseInt(input);
                        state = game.updateGrid(inputX, playerX);
                    }

                    game.printGrid(); // print the game.

                    // check the state
                    if (state == Connect4.gameState.XWin) {
                        System.out.println("Player X won the game.");
                        break;
                    } else if (state == Connect4.gameState.OWin) {
                        System.out.println("Player O won the game.");
                        break;
                    } else if (state == Connect4.gameState.DRAW) {
                        System.out.println("It is draw, end of the game.");
                        break;
                    }

                    // player O
                    System.out.println("PlayerO - your turn. Choose a column number from 1 - 7.");
                    input = scan.nextLine();

                    // exception handling
                    try {
                        Integer.parseInt(input);
                    } catch (NumberFormatException e) {
                        System.out.println("Exception is caught, incorrect input.");
                        System.exit(0);
                    }

                    inputO = Integer.parseInt(input);
                    state = game.updateGrid(inputO, playerO);

                    // if the inputO is invalid, ask again.
                    while (state == Connect4.gameState.INVALID) {
                        System.out.println("Invalid input, PlayerO please choose a column number from 1 - 7.");
                        input = scan.nextLine();

                        // exception handling
                        try {
                            Integer.parseInt(input);
                        } catch (NumberFormatException e) {
                            System.out.println("Exception is caught, incorrect input.");
                            System.exit(0);
                        }

                        inputO = Integer.parseInt(input);
                        state = game.updateGrid(inputO, playerO);
                    }

                    game.printGrid();

                    // check the state
                    if (state == Connect4.gameState.XWin) {
                        System.out.println("Player X won the game.");
                        break;
                    } else if (state == Connect4.gameState.OWin) {
                        System.out.println("Player O won the game.");
                        break;
                    } else if (state == Connect4.gameState.DRAW) {
                        System.out.println("It is draw, end of the game.");
                        break;
                    }

                } while (state == Connect4.gameState.CONTINUE);

            }
        }
        scan.close();
    }

}
