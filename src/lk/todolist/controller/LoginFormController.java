package lk.todolist.controller;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import jdk.nashorn.internal.ir.annotations.Ignore;
import lk.todolist.db.DBConnection;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class LoginFormController {
    public AnchorPane root;
    public JFXTextField txtUserName;
    public JFXPasswordField txtPassword;
    public JFXButton btnLogin;
    public static String userID;
    public static String name;

    public void initialize(){
        /*String col= SettingFormController.colour;

        root.setStyle ( col );*/
    }

    public void lblCreateNewAccOnAction ( MouseEvent mouseEvent ) throws IOException {
        Parent root = FXMLLoader.load ( this.getClass ().getResource ( "../view/CreateNewAccountForm.fxml" ) );
        Scene scene = new Scene ( root );
        Stage primaryStage = (Stage) this.root.getScene ().getWindow ();
        primaryStage.setScene ( scene );
        primaryStage.centerOnScreen ();

    }

    public void login() throws SQLException {
        String userName = txtUserName.getText ( );
        String password = txtPassword.getText ( );

        Connection connection = DBConnection.getInstance ( ).getConnection ( );

        PreparedStatement preparedStatement = connection.prepareStatement ( "select * from user where name = ? and password = ?" );
        preparedStatement.setObject ( 1,userName );
        preparedStatement.setObject ( 2,password );

        ResultSet resultSet = preparedStatement.executeQuery ( );

        if ( resultSet.next () ){

            userID = resultSet.getString ( 1 );
            name = resultSet.getString ( 2 );

            try {
                Alert alert = new Alert ( Alert.AlertType.CONFIRMATION, "Login", ButtonType.YES, ButtonType.CANCEL );
                alert.showAndWait ();

                Parent root = FXMLLoader.load ( this.getClass ().getResource ( "../view/ToDoForm.fxml" ) );
                Scene scene = new Scene ( root );
                Stage primaryStage = (Stage) this.root.getScene ().getWindow ();
                primaryStage.setScene ( scene );
                primaryStage.setTitle ( "ToDo LIST" );
                primaryStage.show ();
            } catch (IOException e) {
                e.printStackTrace ( );
            }

        }else {
            new Alert ( Alert.AlertType.ERROR,"name ekai password chek karapan collo" ).showAndWait ();
            txtUserName.clear ();
            txtPassword.clear ();

            txtUserName.requestFocus ();
        }

    }

    public void btnLogin ( ActionEvent actionEvent ) throws SQLException {
        login ();
    }

    public void txtPasswordOnAction ( ActionEvent actionEvent ) throws SQLException {
        login ();
    }

    public void btnSettionOnAction ( ActionEvent actionEvent ) throws IOException {
        Parent root = FXMLLoader.load ( this.getClass ().getResource ( "../view/SettingForm.fxml" ) );
        Scene scene = new Scene ( root );
        Stage primaryStage = (Stage) this.root.getScene ().getWindow ();
        primaryStage.setScene ( scene );
        primaryStage.show ();
    }
}