package lk.todolist.controller;

import com.jfoenix.controls.JFXRadioButton;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;

public class SettingFormController {
    public JFXRadioButton rtbBlack;
    public AnchorPane root;
    public static String colour;

    public void rtbBlackOnAction ( ActionEvent actionEvent ) {

        colour = "-fx-background-color: black";

        root.setStyle ( "-fx-background-color: black" );
    }

    public void btnbackOnAction ( ActionEvent actionEvent ) throws IOException {
        Parent root = FXMLLoader.load ( this.getClass ().getResource ( "../view/LoginForm.fxml" ) );
        Scene scene = new Scene ( root );
        Stage primaryStage = (Stage) this.root.getScene ().getWindow ();
        primaryStage.setScene ( scene );
        primaryStage.show ();
    }
}
