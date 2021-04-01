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
        //System.out.println(serverFilesList);

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
        System.out.println("hi - upload");
        String selectedString = clientListView.getSelectionModel().getSelectedItem();
        if (selectedString == null){
            warnUser();
            return;
        }


    }
    public void downloadFile(){
        System.out.println("hi - download");
        String selectedString = serverListView.getSelectionModel().getSelectedItem();
        if (selectedString == null){
            warnUser();
            return;
        }

        System.out.println("Just here to silent warnings");
    }
}
