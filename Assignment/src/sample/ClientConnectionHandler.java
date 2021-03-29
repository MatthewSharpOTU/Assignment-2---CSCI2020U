package sample;
import java.io.*;
import java.net.*;
import java.util.*;

public class ClientConnectionHandler extends Thread {
    protected Socket socket = null;
    protected PrintWriter out = null;
    protected BufferedReader in = null;
    protected ArrayList<File> commands = null;

    public ClientConnectionHandler(Socket socket, ArrayList<File> commands) {
        super();
        this.socket = socket;
        this.commands = commands;
        try {
            out = new PrintWriter(socket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        } catch (IOException e) {
            System.err.println("IOException while opening a read/write connection");
        }
    }

    public void run() {
        // initialize interaction
        out.println("Connected to File Server");

        boolean endOfSession = false;
        while (!endOfSession) {
            System.out.println("Hi");
            endOfSession = true;
        }
        try {
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}