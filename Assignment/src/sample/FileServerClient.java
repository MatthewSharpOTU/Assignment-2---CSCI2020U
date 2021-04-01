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

    protected String[] getDIR() {
        String length = null;
        int id = -1;
        networkOut.println("LEN");
        try {
            length = networkIn.readLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
        id = (new Integer(length)).intValue();
        String[] serverFiles = new String[id];
        int count = 0;
        while (count < id){
            networkOut.println("DIR "+count);
            try {
                length = networkIn.readLine();
            } catch (IOException e) {
                System.err.println("Error reading from socket.");
            }
            serverFiles[count] = length;
            count++;
        }
        return serverFiles;
    }

    protected void logout(){
        networkOut.println("LOGOUT");
        try {
            socket.close();
            System.out.println("Connection has been closed");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    protected void fileUpload(File upload){
        String line = null;
        networkOut.println("UPLOAD "+upload);
        try {
            line = networkIn.readLine();
            System.out.println(line);
        } catch (IOException e) {
            System.err.println("Error reading from socket.");
        }
    }

    protected String[] downloadFile(String selectedFile) throws IOException {
        String message = null;
        String[] line = null;
        int id = -1;
        networkOut.println("FILELEN "+ selectedFile);
        try {
            message = networkIn.readLine();
        } catch (IOException e) {
            System.err.println("Error reading from socket.");
        }
        id = (new Integer(message)).intValue();
        line = new String[id];
        for (int i = 0; i<id; i++){
            networkOut.println("DOWNLOAD " + i);
            try {
                line[i] = networkIn.readLine();
            } catch (IOException e) {
                System.err.println("Error reading from socket.");
            }
        }
        return line;
    }

    public static void main(String[] args){
        ClientUiOpener.launchWithArgs(args); //opens the ui in static class
    }
}
