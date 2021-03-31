package sample;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;

import java.io.File;

//this is the controller class that is connected to the main ui with the upload and download
public class MainClientUiController {
    @FXML Button uploadButton;
    @FXML Button downloadButton;
    @FXML ListView<String> clientListView; //you can change the type from String to anything you need
    @FXML ListView<String> serverListView;

    File clientFile;
    public void initialize(){

    }

    //method used for passing client file dir
    public void initData(File clientFile){
        this.clientFile = clientFile;
    }

    //methods are set up to be called when ever the buttons are pressed
    public void uploadFile() {

    }
    public void downloadFile(){

    }
}
