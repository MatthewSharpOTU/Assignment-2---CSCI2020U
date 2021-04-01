package sample;

import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Arrays;

//this is the controller class that is connected to the main ui with the upload and download
public class MainClientUiController {
    @FXML Button uploadButton;
    @FXML Button downloadButton;
    @FXML ListView<String> clientListView; //you can change the type from String to anything you need
    @FXML ListView<String> serverListView;

    File clientFile;

    //method used for passing client file dir
    public void initData(File clientFile){
        this.clientFile = clientFile;
        File[] content = clientFile.listFiles(); // Stores the Files within the local directory

        FileServerClient client = new FileServerClient();
        String[] serverFilesList = client.getDIR(); // Stores the Files within the shared directory
        client.logout();

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

    //methods are set up to be called when ever the buttons are pressed
    public void uploadFile() {
        String selectedString = clientListView.getSelectionModel().getSelectedItem();
        if (selectedString == null){
            warnUser();
            return;
        }

        File[] content = clientFile.listFiles();
        FileServerClient client = new FileServerClient();
        String[] serverFilesList = null;
        if (content!=null) {
            for (File files : content) {
                if (files.getName().equalsIgnoreCase(selectedString)) {
                    client.fileUpload(files); // uploads files
                    serverFilesList = client.getDIR();
                    client.logout();
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
    public void downloadFile(){
        String selectedString = serverListView.getSelectionModel().getSelectedItem();
        if (selectedString == null){
            warnUser();
            return;
        }
        FileServerClient client = new FileServerClient();
        String[] downloadedFileText = null;
        try {
            downloadedFileText = client.downloadFile(selectedString); // retrieves file text
        } catch (IOException e) {
            e.printStackTrace();
        }
        client.logout();
        ObservableList<String> clientList = clientListView.getItems();

        try {
            File downloadedFile = new File(clientFile+"/"+selectedString); // sets up file in current directory
            downloadedFile.createNewFile();
            PrintWriter fileOutput = new PrintWriter(downloadedFile);
            for (String line : downloadedFileText){
                fileOutput.println(line); // writes into the new downloaded file
            }
            fileOutput.close();
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
}
