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

public class FileServer{
    protected Socket clientSocket           = null;
    protected ServerSocket serverSocket     = null;
    protected int numClients                = 0;
    protected ClientConnectionHandler[] threads = null;
    protected ArrayList<File> commands     = new ArrayList<File>();

    public static int SERVER_PORT = 16789;
    public static int MAX_CLIENTS = 50;


    public FileServer(){
        try{
            serverSocket = new ServerSocket(SERVER_PORT);
            System.out.println("---------------------------");
            System.out.println("File Server Application is running");
            System.out.println("---------------------------");
            System.out.println("Listening to port: "+SERVER_PORT);
            threads = new ClientConnectionHandler[MAX_CLIENTS];
            while(true) {
                clientSocket = serverSocket.accept();
                System.out.println("File Sharing Client connected.");
                threads[numClients] = new ClientConnectionHandler(clientSocket, commands);
                threads[numClients].start();
                numClients++;
            }
        } catch (IOException e) {
            System.err.println("IOException while creating server connection");
        }
    }

    public static void main(String[] args) {
        FileServer app = new FileServer();
    }
}