package sample;
import java.io.*;
import java.net.*;
import java.util.*;

/**
 * Handler executes proper commands from the socket
 */
public class ClientConnectionHandler extends Thread {
    protected Socket socket = null; // client socket
    protected PrintWriter out = null; // used to write to the socket
    protected BufferedReader in = null; // used to read from the socket
    protected ArrayList<File> files = null; // used to store the files in the shared directory
    protected File stored = null; // used to temporarily store the chosen file to download

    public ClientConnectionHandler(Socket socket, ArrayList<File> files) {
        super();
        this.socket = socket; // sets the socket
        this.files = files; // sets the files
        try {
            out = new PrintWriter(socket.getOutputStream(), true); // sets the writer
            in = new BufferedReader(new InputStreamReader(socket.getInputStream())); // sets the reader
        } catch (IOException e) {
            System.err.println("IOException while opening a read/write connection");
        }
    }

    /**
     * Executed when a connection to server, and execute a command is requested
     */
    public void run() {
        // initialize interaction
        Boolean endOfConnection = false;
        while (!endOfConnection) { // while loop will run while a connection request is active
            endOfConnection = processCommand();
        }
        try {
            socket.close(); // closes the client socket
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * processCommand will determine if a connection to the server is requested
     * @return - Boolean statement whether a connection is still active (false) or not (true)
     */
    protected Boolean processCommand() {
        String message = null;
        try{
            message = in.readLine();
        } catch (IOException e) {
            System.err.println("Error reading command from socket.");
            return true;
        }
        if (null == message){
            return true;
        }
        StringTokenizer st = new StringTokenizer(message);
        String command = st.nextToken();
        String args = null;
        if (st.hasMoreTokens()) {
            args = message.substring(command.length()+1, message.length());
        }
        return processCommand(command, args);
    }

    /**
     * processCommand will determine if a connection to the server is requested given a command and argument
     * @param command - String statement to determine which command
     * @param args - String to determine which arguments will write to the socket
     * @return - Boolean statement whether a connection is still active (false) or not (true)
     */
    protected Boolean processCommand(String command, String args) {
        if (command.equalsIgnoreCase("DIR")){ // conditional if command requests the shared directory
            int id = (new Integer(args)).intValue(); // gets the integer representation of the index argument
            if (id < files.size()){
                String sharedFile = files.get(id).getName(); // gets the name of the desired file at the index
                out.println(sharedFile); // writes the file name
            } else {
                out.println("400 Message Does Not Exist");
            }
            return false; // returns false for the socket to remain open
        } else if (command.equalsIgnoreCase("UPLOAD")) { // conditional if command requests to upload a file to the directory
            synchronized(this){ // in case multiple clients are uploading at once
                File argument = new File(args); // sets the argument as a new file
                for (int i = 0; i<files.size(); i++){ // for loop to see if the file already exists to overwrite it
                    if (files.get(i).getName().equals(argument.getName())){
                        files.set(i, argument); // if found set the new uploaded file to overwrite it
                        out.println("200 Message Sent");
                        return false; // returns false for the socket to remain open
                    }
                }
                files.add(argument); // if the file doesnt exist yet then add to the shared directory
            }
            out.println("200 Message Sent");
            return false; // returns false for the socket to remain open
        } else if (command.equalsIgnoreCase("DOWNLOAD")) { // conditional if command requests to download a file from the directory
            int id = (new Integer(args)).intValue(); // gets the integer representation of the argument
            String line = null; // used to read the line
            try {
                FileReader fileInput = new FileReader(stored); // sets up which file to read from
                BufferedReader input = new BufferedReader(fileInput);
                for (int i = 0; i<=id; i++){ // reads the desired interger line number
                    line = input.readLine();
                }
                out.println(line); // write the specified line to the socket
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return false; // returns false for the socket to remain open
        } else if (command.equalsIgnoreCase("LOGOUT")){ //conditional if the command is LOGOUT to close the socket
            out.println("200 Client Logged Out");
            return true; // returns true for the socket to close
        } else if (command.equalsIgnoreCase("LEN")){ // conditional if command is LEN to get the size of the shared directory
            out.println((files.size())); // writes to the socket the shared directory size
            return false; // returns false for the socket to remain open
        } else if (command.equalsIgnoreCase("FILELEN")){ // conditional if command is FILELEN to get the number of lines in a file
            int id = 0; // stores the integer number of number of lines
            stored = null; // temporarily stores the downloaded file
            String line = null; // gets the line of a file
            for (int i = 0; i<files.size(); i++){ // for loop to find the desired file
                if (files.get(i).getName().equalsIgnoreCase(args)){
                    stored = files.get(i);
                }
            }
            try {
                FileReader fileInput = new FileReader(stored);
                BufferedReader input = new BufferedReader(fileInput);
                while((line = input.readLine())!=null){ // while loop that runs for the number of lines in the text file
                    id++;
                }
                out.println(id); // writes the number of lines in the file to the socket
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return false; // returns false for the socket to remain open
        }
        else { // if command is unknown
            out.println("400 Unrecognized Command: "+command);
            return false;
        }
    }
}