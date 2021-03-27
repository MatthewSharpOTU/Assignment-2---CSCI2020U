package sample;
import java.io.*;
import java.net.*;
import java.util.*;

public class ClientConnectionHandler extends Thread{
    protected Socket socket       = null;
    protected PrintWriter out     = null;
    protected BufferedReader in   = null;

    public ClientConnectionHandler(Socket socket, )

    public void run(){
        // initialize interaction
        out.println("Connected to Chat Server");
        out.println("200 Ready For Chat");

        boolean endOfSession = false;
        while(!endOfSession) {
            endOfSession = processCommand();
        }
        try {
            socket.close();
        } catch(IOException e) {
            e.printStackTrace();
        }
    }
}
