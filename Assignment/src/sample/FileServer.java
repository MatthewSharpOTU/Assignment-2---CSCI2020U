package sample;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

/**
 * FileServer class sets up the server
 */
public class FileServer{
    protected Socket clientSocket           = null; // used to store client socket
    protected ServerSocket serverSocket     = null; // used to store server socket
    protected int numClients                = 0; // used to keep track of number of clients
    protected ClientConnectionHandler[] threads = null; // used for each new connection
    protected ArrayList<File> commands     = new ArrayList<File>(); // used to pass for the handler

    public static int SERVER_PORT = 16789; // server port
    public static int MAX_CLIENTS = 50; // max clients at once


    public FileServer(){
        try{
            serverSocket = new ServerSocket(SERVER_PORT); // sets up server socket
            System.out.println("---------------------------");
            System.out.println("File Server Application is running");
            System.out.println("---------------------------");
            System.out.println("Listening to port: "+SERVER_PORT);
            threads = new ClientConnectionHandler[MAX_CLIENTS]; // sets up threads
            while(true) {
                clientSocket = serverSocket.accept(); // sets up client socket
                System.out.println("File Sharing Client connected.");
                threads[numClients] = new ClientConnectionHandler(clientSocket, commands); // creates new handler connection
                threads[numClients].start(); // starts new handler connection
                numClients++; // increments number of clients
            }
        } catch (IOException e) {
            System.err.println("IOException while creating server connection");
        }
    }

    public static void main(String[] args) {
        FileServer app = new FileServer();
    }
}