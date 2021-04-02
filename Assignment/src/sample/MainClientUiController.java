package sample;

import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.Scanner;

/**
 * this is the controller class that is connected to the main ui with the upload and download
 */
public class MainClientUiController {
    @FXML ListView<String> clientListView; //you can change the type from String to anything you need
    @FXML ListView<String> serverListView;

    File clientFile; // client directory

    /**
     * method used for passing client file dir
     * @param clientFile - Client Directory
     */
    public void initData(File clientFile){
        this.clientFile = clientFile; // sets client directory
        File[] content = clientFile.listFiles(); // Stores the Files within the local directory

        FileServerClient client = new FileServerClient(); //instantiates new connection
        String[] serverFilesList = client.getDIR(); // Stores the Files within the shared directory
        client.logout(); // ends connection

        for (String line : serverFilesList){ // prints all server directory files
            System.out.println(line);
        }

        ObservableList<String> clientList = clientListView.getItems();
        if (content!=null){
            for (File file: content){
                clientList.add(file.getName());
            }
        }

        ObservableList<String> serverList = serverListView.getItems();
        if (serverFilesList!=null){
            serverList.addAll(Arrays.asList(serverFilesList));
        }
    }

    /**
     * Displays a warning if download or upload is pressed with no file selected
     */
    private void warnUser(){
        try {
            Stage secondaryStage = new Stage();
            Parent root = FXMLLoader.load(getClass().getResource("warn.fxml"));
            secondaryStage.setScene(new Scene(root));
            secondaryStage.show();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    /**
     * Method if the upload button is pressed
     */
    public void uploadFile() {
        String selectedString = clientListView.getSelectionModel().getSelectedItem(); //gets selected item
        if (selectedString == null){ // conditional if selected item is null
            warnUser();
            return;
        }

        File[] content = clientFile.listFiles(); // gets list of all client directory files
        FileServerClient client = new FileServerClient(); // instantiates new connection
        String[] serverFilesList = null; // used for the server files
        if (content!=null) {
            for (File files : content) { // for loop each file in the client directory
                if (files.getName().equalsIgnoreCase(selectedString)) { // conditional for when the desired file is found
                    client.fileUpload(files); // uploads files
                    serverFilesList = client.getDIR(); // gets server directory files
                    client.logout(); // ends connection
                }
            }
        }

        //clears and updates server view after upload
        ObservableList<String> serverList = serverListView.getItems();
        serverList.clear();
        if (serverFilesList!=null){
            serverList.addAll(Arrays.asList(serverFilesList));
        }
    }

    /**
     * downloads the specified file from the shared directory
     */
    public void downloadFile(){
        String selectedString = serverListView.getSelectionModel().getSelectedItem(); // gets selected file
        if (selectedString == null){ // conditional if the selected file is null
            warnUser();
            return;
        }
        FileServerClient client = new FileServerClient(); // instantiates new connection
        String[] downloadedFileText = null; // used for retrieving file text
        try {
            downloadedFileText = client.downloadFile(selectedString); // retrieves file text
        } catch (IOException e) {
            e.printStackTrace();
        }
        client.logout(); // ends connection
        ObservableList<String> clientList = clientListView.getItems();

        try {
            File downloadedFile = new File(clientFile+"/"+selectedString); // sets up file in current directory
            downloadedFile.createNewFile(); // creates new file
            PrintWriter fileOutput = new PrintWriter(downloadedFile);
            for (String line : downloadedFileText){
                fileOutput.println(line); // writes into the new downloaded file
            }
            fileOutput.close(); // closes file being written
        } catch (IOException e) {
            System.err.println("There was an issue reading the file");
        }

        //clears and updates client view after download
        clientList.clear();
        File[] content = clientFile.listFiles();
        if (content!=null){
            for (File file: content){
                clientList.add(file.getName());
            }
        }
    }

    /**
     * Used to viewfile selected and display it
     */
    public void viewFile(){
        String selectedString = clientListView.getSelectionModel().getSelectedItem(); // Gets selected file
        if (selectedString == null){
            warnUser();
            return;
        }

        try {
            Stage secondaryStage = new Stage();
            Parent root = FXMLLoader.load(getClass().getResource("fileViewer.fxml"));
            Scene secondaryScene = new Scene(root);
            secondaryStage.setScene(secondaryScene);
            secondaryStage.setTitle(selectedString);
            secondaryStage.show();
            VBox displayBox = (VBox) secondaryScene.lookup("#vBox");

            File selectedFile = null;
            if (displayBox == null){System.out.println("EmptyDisplayBox"); return;}
            File[] content = clientFile.listFiles();

            if (content == null){System.out.println("Content is null"); return;}
            for (File file : content) {
                if (file.getName().equalsIgnoreCase(selectedString)) {
                    selectedFile = file;
                    break;
                }
            }

            if (selectedFile == null){System.out.println("No selected file"); return;}
            Scanner scan = new Scanner(selectedFile);
            ObservableList<Node> displayList = displayBox.getChildren();
            while (scan.hasNext()){
                displayList.add(new Label(scan.nextLine()));
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
