package sample;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;

public class LoginController {
    @FXML TextField textField;

    public void LoginWithUsername() throws IOException {
        //this method is called when the login login button is pressed

        //opens up the main client ui window and closes this login window
        Stage mainStage = (Stage) textField.getScene().getWindow();
        Parent animationRoot = FXMLLoader.load(getClass().getResource("clientUi.fxml"));
        Scene currentMainScene = new Scene(animationRoot);
        mainStage.setScene(currentMainScene);

        //anything like setting up a list of files should be done in the MainClientUiController.initialize()
    }
}