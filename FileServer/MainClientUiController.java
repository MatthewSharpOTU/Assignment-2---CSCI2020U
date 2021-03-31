import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;

//this is the controller class that is connected to the main ui with the upload and download
public class MainClientUiController {
    @FXML Button uploadButton;
    @FXML Button downloadButton;
    @FXML ListView<String> clientListView; //you can change the type from String to anything you need
    @FXML ListView<String> serverListView;

    public void initialize(){

    }

    //methods are set up to be called when ever the buttons are pressed
    public void uploadFile() {

    }
    public void downloadFile(){

    }
}
