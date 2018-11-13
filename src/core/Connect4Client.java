package core;

import java.io.IOException;
import java.net.UnknownHostException;

import javafx.application.Application;
import ui.Connect4GUI;

/**
 * This class calls Connect4GUI to interact with Connect4Server.
 * @author Xiaolou Huang (Lor)
 * @version 11/12/2018
 */
public class Connect4Client {

    public static void main(String[] args) throws UnknownHostException, IOException {
        Application.launch(Connect4GUI.class, args);
    }
}
