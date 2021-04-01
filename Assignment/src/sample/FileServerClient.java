package sample;

import java.io.*;
import java.net.*;

public class FileServerClient {
    private Socket socket = null;
    private BufferedReader in = null;
    private PrintWriter out = null;
    private PrintWriter networkOut = null;
    private BufferedReader networkIn = null;
    private File localFolder = null;

    //we can read this from the user too
    public static String SERVER_ADDRESS = "localhost";
    public static int    SERVER_PORT = 16789;

    public FileServerClient(){
        try{
            socket = new Socket(SERVER_ADDRESS, SERVER_PORT);
        } catch (UnknownHostException e) {
            System.err.println("Unknown host: "+SERVER_ADDRESS);
        } catch (IOException e) {
            System.err.println("IOException while connecting to server: "+SERVER_ADDRESS);
        }
        try {
            networkOut = new PrintWriter(socket.getOutputStream(), true);
            networkIn = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        } catch (IOException e) {
            System.err.println("IOException while opening a read/write connection");
        }
    }

    public String[] getDIR() {
        String[] serverFiles = null;
        String length = null;
        int id = -1;
        networkOut.println("LEN");
        try {
            length = networkIn.readLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println(length);
        id = (new Integer(length)).intValue();
        for (int i = 0; i<id; i++){
            networkOut.println("DIR " + i);
            try {
                serverFiles[i] = networkIn.readLine();
                System.out.println(serverFiles[i]);
            } catch (IOException e) {
                System.err.println("Error reading from socket.");
            }
        }
        return serverFiles;
    }

    public void logout(){
        networkOut.println("LOGOUT");
        try {
            socket.close();
            System.out.println("Connection has been closed");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args){
        /*
        FileServerClient client = new FileServerClient();
        String[] serverFilesList = client.getDIR(); // Stores the Files within the shared directory
        client.logout();
         */
        ClientUiOpener.launchWithArgs(args); //opens the ui in static class
    }
}
