package sample;

import java.io.*;
import java.net.*;

/**
 * Sends commands to socket via writing and then reads following output from the handler
 */
public class FileServerClient {
    private Socket socket = null; // used to store client socket
    private PrintWriter networkOut = null; // used to write to socket
    private BufferedReader networkIn = null; // used to read from socket

    //we can read this from the user too
    public static String SERVER_ADDRESS = "localhost"; // server address
    public static int    SERVER_PORT = 16789; // server port

    /**
     * FileServerClient constructor to set up socket and capabilities to write to and read from the socket
     */
    public FileServerClient(){
        try{
            socket = new Socket(SERVER_ADDRESS, SERVER_PORT); // sets up socket
        } catch (UnknownHostException e) {
            System.err.println("Unknown host: "+SERVER_ADDRESS);
        } catch (IOException e) {
            System.err.println("IOException while connecting to server: "+SERVER_ADDRESS);
        }
        try {
            networkOut = new PrintWriter(socket.getOutputStream(), true); // sends up writer
            networkIn = new BufferedReader(new InputStreamReader(socket.getInputStream())); // sets up reader
        } catch (IOException e) {
            System.err.println("IOException while opening a read/write connection");
        }
    }

    /**
     * used to retrieve the shared directory files and return the file names as an array of strings
     * @return - string of arrays that consist of all shared directory files
     */
    protected String[] getDIR() {
        String length = null; // used to get number of files in the shared directory
        int id = -1; // used to store the number of files
        networkOut.println("LEN"); // sends the command LEN
        try {
            length = networkIn.readLine(); // reads the number of files
        } catch (IOException e) {
            e.printStackTrace();
        }
        id = (new Integer(length)).intValue(); // sets the number of files as a integer
        String[] serverFiles = new String[id]; // sets the length of the serverFiles array
        int count = 0; // used to increment each of the files in the shared directory
        while (count < id){
            networkOut.println("DIR "+count); // sends DIR command with the file we want
            try {
                length = networkIn.readLine(); // reads the file name
            } catch (IOException e) {
                System.err.println("Error reading from socket.");
            }
            serverFiles[count] = length; // add the read file name to the array
            count++; // increment count
        }
        return serverFiles; // return array of file names
    }

    /**
     * Used to logout of the connection
     */
    protected void logout(){
        networkOut.println("LOGOUT"); // sends LOGOUT command
        try {
            socket.close(); // closes socket
            System.out.println("Connection has been closed");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Used to upload the new file into the shared directory
     * @param upload - file wanting to be uploaded
     */
    protected void fileUpload(File upload){
        String line = null; // line to read from the socket
        networkOut.println("UPLOAD "+upload); // writes to the socket with command UPLOAD
        try {
            line = networkIn.readLine(); // reads from the socket
        } catch (IOException e) {
            System.err.println("Error reading from socket.");
        }
    }

    /**
     * Used to download the specified file and retrieve the text from the file
     * @param selectedFile - the selected file to download
     * @return - returns an array of strings which contain the lines of the text file
     * @throws IOException - incase there is an error with the file
     */
    protected String[] downloadFile(String selectedFile) throws IOException {
        String message = null; // used to read the number of lines in the file
        String[] line = null; // used to store the lines of the text file
        int id = -1; // used to represent the number of lines in the file as an integer
        networkOut.println("FILELEN "+ selectedFile); // writes to the socket with the command FILELEN and argument of the file
        try {
            message = networkIn.readLine(); // reads from the socket
        } catch (IOException e) {
            System.err.println("Error reading from socket.");
        }
        id = (new Integer(message)).intValue(); // converts read message to an integer
        line = new String[id]; // creates length of array
        for (int i = 0; i<id; i++){ // for loop - the length of the rows in the text file
            networkOut.println("DOWNLOAD " + i); // writes to the socket with the command DOWNLOAD and argument of the integer
            try {
                line[i] = networkIn.readLine(); // reads the line of the text file
            } catch (IOException e) {
                System.err.println("Error reading from socket.");
            }
        }
        return line; // returns the array of text file lines of text
    }

    public static void main(String[] args){
        ClientUiOpener.launchWithArgs(args); //opens the ui in static class
    }
}
