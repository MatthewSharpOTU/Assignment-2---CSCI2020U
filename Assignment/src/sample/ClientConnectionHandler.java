package sample;
import java.io.*;
import java.net.*;
import java.util.*;

public class ClientConnectionHandler extends Thread {
    protected Socket socket = null;
    protected PrintWriter out = null;
    protected BufferedReader in = null;
    protected ArrayList<File> files = null;

    public ClientConnectionHandler(Socket socket, ArrayList<File> files) {
        super();
        this.socket = socket;
        this.files = files;
        try {
            out = new PrintWriter(socket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        } catch (IOException e) {
            System.err.println("IOException while opening a read/write connection");
        }
    }

    public void run() {
        // initialize interaction
        Boolean endOfConnection = false;
        while (!endOfConnection) {
            endOfConnection = processCommand();
        }
        try {
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

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

    protected Boolean processCommand(String command, String args) {
        if (command.equalsIgnoreCase("DIR")){
            int id = (new Integer(args)).intValue();
            if (id < files.size()){
                String sharedFile = files.get(id).getName();
                out.println(sharedFile);
            } else {
                out.println("400 Message Does Not Exist");
            }
            return true;
        } else if (command.equalsIgnoreCase("UPLOAD")) {
            out.println("200 Message Sent");
            return false;
        } else if (command.equalsIgnoreCase("DOWNLOAD")) {
            out.println("200 Message Sent");
            return false;
        } else if (command.equalsIgnoreCase("LOGOUT")){
            out.println("200 Client Logged Out");
            return true;
        } else if (command.equalsIgnoreCase("LEN")){
            out.println((files.size()-1));
            return false;
        }
        else {
            out.println("400 Unrecognized Command: "+command);
            return false;
        }
    }
}