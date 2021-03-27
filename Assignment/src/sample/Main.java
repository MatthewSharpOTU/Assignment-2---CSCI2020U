package sample;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.net.ServerSocket;
import java.net.Socket;
import java.util.Vector;

public class Main extends Application {
    protected Socket clientSocket           = null;
    protected ServerSocket serverSocket     = null;
    protected int numClients                = 0;
    protected Vector messages               = new Vector();

    public static int SERVER_PORT = 16789;
    public static int MAX_CLIENTS = 25;


    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("sample.fxml"));
        primaryStage.setTitle("Assignment 2: File Sharing System");
        Scene scene = new Scene(new VBox());
        primaryStage.setWidth(615);
        primaryStage.setHeight(500);



        SplitPane pane = new SplitPane();
        TableView leftTable = new TableView();
        leftTable.setEditable(true);
        TableColumn firstCol = new TableColumn();
        firstCol.setMinWidth(300);
        leftTable.getColumns().addAll(firstCol);
        TableView rightTable = new TableView();
        leftTable.setEditable(true);
        TableColumn secondCol = new TableColumn();
        firstCol.setMinWidth(300);
        rightTable.getColumns().add(secondCol);
        pane.getItems().addAll(leftTable, rightTable);

        Button btn = new Button("Download");
        Button btn2 = new Button("Upload");
        HBox hbBtn = new HBox(10);
        hbBtn.getChildren().addAll(btn, btn2);

        ((VBox) scene.getRoot()).getChildren().add(hbBtn);
        ((VBox) scene.getRoot()).getChildren().add(pane);

        primaryStage.setScene(scene);
        primaryStage.show();



    }


    public static void main(String[] args) {
        launch(args);
    }
}
