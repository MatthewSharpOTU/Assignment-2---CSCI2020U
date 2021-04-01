package sample;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;

public class LoginController {
    @FXML TextField textField;
    @FXML Label clientDirDisplay;

    private File clientFile;
    public void LoginWithUsername() throws IOException {
        //checking fields before continuing
        String username = textField.getText();
        if (username.length()==0){
            textField.setPromptText("Requires Username");
            return;
        }
        textField.clear();
        if (clientFile!=null && clientFile.exists() && clientFile.isFile()){
            clientDirDisplay.setText("Error");
            return;
        }

        //opens up the main client ui window and closes this login window
        Stage mainStage = (Stage) textField.getScene().getWindow();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("clientUi.fxml"));
        mainStage.setScene(new Scene(loader.load()));

        //passes client directory Mto table view
        MainClientUiController controller = loader.getController();
        controller.initData(clientFile);

        //anything like setting up a list of files should be done in the MainClientUiController.initialize()
    }

    public void setClientDirectory(){
        DirectoryChooser directoryChooser = new DirectoryChooser();
        directoryChooser.setInitialDirectory(new File("."));
        clientFile = directoryChooser.showDialog(textField.getScene().getWindow());
        if (clientFile == null){return;}
        if (clientFile.isDirectory()){
            clientDirDisplay.setText(clientFile.getName());
        }else{
            clientDirDisplay.setText("None");
        }
    }
}