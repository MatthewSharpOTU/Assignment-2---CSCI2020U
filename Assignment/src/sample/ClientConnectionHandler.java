package sample;
import java.io.*;
import java.net.*;
import java.util.*;

public class ClientConnectionHandler extends Thread {
    protected Socket socket = null;
    protected PrintWriter out = null;
    protected BufferedReader in = null;
    protected ArrayList<File> files = null;
    protected File stored = null;

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
            return false;
        } else if (command.equalsIgnoreCase("UPLOAD")) {
            synchronized(this){
                File argument = new File(args);
                files.add(argument);
            }
            out.println("200 Message Sent");
            return false;
        } else if (command.equalsIgnoreCase("DOWNLOAD")) {
            int id = (new Integer(args)).intValue();
            String line = null;
            try {
                FileReader fileInput = new FileReader(stored);
                BufferedReader input = new BufferedReader(fileInput);
                for (int i = 0; i<=id; i++){
                    line = input.readLine();
                }
                out.println(line);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return false;
        } else if (command.equalsIgnoreCase("LOGOUT")){
            out.println("200 Client Logged Out");
            return true;
        } else if (command.equalsIgnoreCase("LEN")){
            out.println((files.size()));
            return false;
        } else if (command.equalsIgnoreCase("FILELEN")){
            int id = 0;
            stored = null;
            String line = null;
            for (int i = 0; i<files.size(); i++){
                if (files.get(i).getName().equalsIgnoreCase(args)){
                    stored = files.get(i);
                }
            }
            try {
                FileReader fileInput = new FileReader(stored);
                BufferedReader input = new BufferedReader(fileInput);
                while((line = input.readLine())!=null){
                    id++;
                }
                out.println(id);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return false;
        }
        else {
            out.println("400 Unrecognized Command: "+command);
            return false;
        }
    }
}