//package sample;

import java.io.*;
import java.net.*;
import java.util.*;
import java.awt.event.*;
import java.awt.*;

public class FileServerClient extends Frame {
    private Socket socket = null;
    private BufferedReader in = null;
    private PrintWriter networkOut = null;
    private BufferedReader networkIn = null;

    //we can read this from the user too
    public  static String SERVER_ADDRESS = "localhost";
    public  static int    SERVER_PORT = 16789;

    public FileServerClient() {
        try {
            socket = new Socket(SERVER_ADDRESS, SERVER_PORT);
        } catch (UnknownHostException e) {
            System.err.println("Unknown host: "+SERVER_ADDRESS);
        } catch (IOException e) {
            System.err.println("IOEXception while connecting to server: "+SERVER_ADDRESS);
        }
        if (socket == null) {
            System.err.println("socket is null");
        }
        try {
            networkOut = new PrintWriter(socket.getOutputStream(), true);
            networkIn = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        } catch (IOException e) {
            System.err.println("IOEXception while opening a read/write connection");
        }

        in = new BufferedReader(new InputStreamReader(System.in));

        // force the user to type in a username and password
        boolean ok = login();

        if (!ok) {
            System.exit(0);
        }

        ok = true;
        while(ok) {
            ok = processUserInput();
        }

        try {
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    // Logcin function
    protected boolean login() {
        String input = null;
        File files = null;
        String message = null;
        int errorCode = 0;

        try {
            message = networkIn.readLine(); //Welcome to chat
            System.out.println(message);
            message = networkIn.readLine(); //200 Message serves is ready
            System.out.println(message);
        } catch (IOException e) {
            System.err.println("Error reading initial greeting from socket.");
        }


        while(errorCode != 200) {
            // get userID
            System.out.print("Type your computer name (quit to exit): ");
            try {
                input = in.readLine();
            } catch (IOException e) {
                e.printStackTrace();
            }
            if (input.equalsIgnoreCase("quit")) {
                return false;
            }
            networkOut.println("CN "+input);
            try {
                message = networkIn.readLine();
            } catch (IOException e) {
                System.err.println("Error reading response to Computer Name.");
            }

            // get password
            System.out.print("Folder : ");
            try {
                input = in.readLine();
                files = new File(input);
            } catch (IOException e) {
                e.printStackTrace();
            }
            networkOut.println("FDR "+files.getName());
            try {
                message = networkIn.readLine();
            } catch (IOException e) {
                System.err.println("Error reading response to Computer Name.");
            }

            errorCode = getErrorCode(message);
            if (errorCode != 200) {
                System.out.println("Login unsuccessful: "+getErrorMessage(message));
                return false;
            }
        }
        return true;
    }

    // Display Menu  of actions
    // Alternatively you can always be in "reading mode" whatever is typed gets send to the server/other clients without they having to "List all messages"
    // -- This would work 100x better and easier if you make at least the client a JavaFX application, the user can type in a textbox, when pressed <enter> you send the message
    // --- Every time the server gets a message they send to all the other clients who get their UI refreshed with the most recent messages, etc.
    protected boolean processUserInput() {
        String input = null;

        // print the menu
        System.out.println("Commands: ");
        System.out.println("1 - DIR");
        System.out.println("2 - UPLOAD");
        System.out.println("3 - DOWNLOAD");
        System.out.print("Command> ");

        try {
            input = in.readLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (input.equals("1")) {
            listAllMessages();
        } else if (input.equals("2")) {
            addNewMessage();
        } else if (input.equals("3")) {
            downloadFile();
		}
        return false;
    }

    //Helper function
    protected int getErrorCode(String message) {
        StringTokenizer st = new StringTokenizer(message);
        String code = st.nextToken();
        return (new Integer(code)).intValue();
    }

    //Helper function
    protected String getErrorMessage(String message) {
        StringTokenizer st = new StringTokenizer(message);
        String code = st.nextToken();
        String errorMessage = null;
        if (st.hasMoreTokens()) {
            errorMessage = message.substring(code.length()+1, message.length());
        }
        return errorMessage;
    }

    // menu option 2
    public void addNewMessage() {
        String message = null;
        String input = null;
        File files = null;

        System.out.print("Please type file name: ");
        try {
            input = in.readLine();
            files = new File(input);
        } catch (IOException e) {
            e.printStackTrace();
        }
        networkOut.println("ADDMSG "+input);

        // read and ignore response
        try {
            message = networkIn.readLine();
        } catch (IOException e) {
            System.err.println("Error reading from socket.");
        }
    }

    // menu option 1
    public void listAllMessages() {
        String message = null;

        networkOut.println("DIR");

        // read response, store id
        int id = -1;
        try {
            message = networkIn.readLine();
        } catch (IOException e) {
            System.err.println("Error reading from socket.");
        }
        String strID = message.substring(message.indexOf(':')+1);
        id = (new Integer(strID.trim())).intValue();
        for (int i = 0; i <= id; i++) {
            networkOut.println("GETMSG "+i);
            try {
                message = networkIn.readLine();
            } catch (IOException e) {
                System.err.println("Error reading from socket.");
            }
            int index = message.indexOf(':')+1;
            String msg = message.substring(index);
            System.out.println(msg);
        }
    }

    public void downloadFile(){
        
	}
    public static void main(String[] args) {
        FileServerClient client = new FileServerClient();
    }
}