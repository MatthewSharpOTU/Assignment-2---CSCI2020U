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

/**
 * Used to properly display and store values from the login UI
 */
public class LoginController {
    @FXML TextField textField;
    @FXML Label clientDirDisplay;

    private File clientFile; // Used to store the client directory

    /**
     * LoginWithUsername() used to store the username field and to check if the inputted client directory is viable
     * @throws IOException - if some error occurs with the file
     */
    public void LoginWithUsername() throws IOException {
        //checking fields before continuing
        String username = textField.getText(); // gets username from the textField
        if (username.length()==0){ // conditional if the username is not inputted
            textField.setPromptText("Requires Username");
            return;
        }
        textField.clear(); // clears text field
        if (clientFile!=null && clientFile.exists() && clientFile.isFile()){ // conditional if clientFile is empty
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

    /**
     * setClientDirectory retrieves the inputted client directory from the UI
     */
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