package lk.todolist.controller;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import lk.todolist.db.DBConnection;

import java.io.IOException;
import java.sql.*;

public class CreateNewAccountFormController {

    public AnchorPane root;
    public JFXTextField txtUserName;
    public JFXTextField txtEmail;
    public JFXPasswordField txtNewPassword;
    public JFXPasswordField txtConfirmPassword;
    public JFXTextField txtUserID;
    public JFXButton btnAddNewUser;
    public JFXButton btnRegister;

    public void initialize(){
        txtUserName.setDisable ( true );
        txtEmail.setDisable ( true );
        txtNewPassword.setDisable ( true );
        txtConfirmPassword.setDisable ( true );
        btnRegister.setDisable ( true );
    }

    public void Register(){
        String newPassword = txtNewPassword.getText ( );
        String confirmPassword = txtConfirmPassword.getText ( );

        if ( newPassword.equalsIgnoreCase ( confirmPassword ) ){
            txtNewPassword.setStyle ( "-fx-border-color: transparent" );
            txtConfirmPassword.setStyle ( "-fx-border-color: transparent" );

            String id = txtUserID.getText ( );
            String name = txtUserName.getText ( );
            String email = txtEmail.getText ( );
            String password = txtConfirmPassword.getText ( );

            Connection connection = DBConnection.getInstance ( ).getConnection ( );

            try {
                PreparedStatement preparedStatement = connection.prepareStatement ( "insert into user values(?,?,?,?) " );

                preparedStatement.setObject ( 1,id );
                preparedStatement.setObject ( 2,name );
                preparedStatement.setObject ( 3,password );
                preparedStatement.setObject ( 4,email );

                int i = preparedStatement.executeUpdate ( );

                if ( i != 0 ){
                    new Alert ( Alert.AlertType.CONFIRMATION,"Wede Goda collo").showAndWait ();
                    txtUserID.clear ();
                    txtUserName.clear ();
                    txtEmail.clear ();
                    txtNewPassword.clear ();
                    txtConfirmPassword.clear ();

                    Parent root = FXMLLoader.load ( this.getClass ().getResource ( "../view/LoginForm.fxml" ) );
                    Scene scene = new Scene ( root );
                    Stage primaryStage = (Stage) this.root.getScene ().getWindow ();
                    primaryStage.setScene ( scene );
                    primaryStage.show ();
                }else {
                    new Alert ( Alert.AlertType.ERROR,"Wede Aul collo").showAndWait ();
                }

            } catch (SQLException | IOException throwables) {
                throwables.printStackTrace ( );
            }

        }else{
            txtNewPassword.setStyle ( "-fx-border-color: red" );
            txtConfirmPassword.setStyle ( "-fx-border-color: red" );
            txtNewPassword.requestFocus ();
            Alert alert = new Alert ( Alert.AlertType.ERROR, "Password Dose Not Match", ButtonType.OK );
            alert.showAndWait ();
        }
        txtUserName.requestFocus ();
    }

    public void btnRegisterOnAction ( ActionEvent actionEvent ) {
        Register ();
    }


    public void lblHaveAccOnAction ( MouseEvent mouseEvent ) throws IOException {
        Parent root = FXMLLoader.load ( this.getClass ().getResource ( "../view/LoginForm.fxml" ) );
        Scene scene = new Scene ( root );
        Stage primaryStage = (Stage) this.root.getScene ().getWindow ();
        primaryStage.setScene ( scene );
        primaryStage.centerOnScreen ();

    }

    public void autoGenerateID(){
        Connection connection = DBConnection.getInstance ( ).getConnection ( );
        try {
            Statement statement = connection.createStatement ( );
            ResultSet resultSet = statement.executeQuery ( "select id from user order by id desc limit 1" );
            boolean next = resultSet.next ( );
            if ( next ){
                String oldID = resultSet.getString ( 1 );

                String substring = oldID.substring ( 1, 4 );

                int intId = Integer.parseInt ( substring );

                intId = intId + 1;
                if ( intId <10 ){
                    txtUserID.setText ( "U00"+intId );
                }else if ( intId <100 ){
                    txtUserID.setText ( "U0"+intId );
                }else if ( intId <1000 ){
                    txtUserID.setText ( "U"+intId );
                }
            }else {
                txtUserID.setText ( "U001" );
            }

        } catch (SQLException e) {
            e.printStackTrace ( );
        }
    }

    public void btnAddNewUserOnAction ( ActionEvent actionEvent ) {
        autoGenerateID();


        txtUserName.clear ();
        txtEmail.clear ();
        txtNewPassword.clear ();
        txtConfirmPassword.clear ();

        txtUserName.setDisable ( false );
        txtEmail.setDisable ( false );
        txtNewPassword.setDisable ( false );
        txtConfirmPassword.setDisable ( false );
        btnRegister.setDisable ( false );

    }

    public void txtConfirmPasswordOnAction ( ActionEvent actionEvent ) {
        Register ();
    }
}
