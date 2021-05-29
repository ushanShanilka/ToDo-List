package lk.todolist.controller;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import lk.todolist.db.DBConnection;
import lk.todolist.tm.ToDo;

import java.io.IOException;
import java.sql.*;
import java.util.Optional;

public class ToDoFormController {
    public AnchorPane root;
    public Label lblTitle;
    public Label lblUserId;
    public JFXButton btnAddNewToDo;
    public Pane paneAddToDo;
    public JFXButton btnUpdate;
    public JFXButton btnDelete;
    public JFXButton btnLogOut;
    public JFXTextField txtNewToDo;
    public JFXButton btnAddToList;
    public ListView<ToDo> lstToDos;
    public JFXTextField txtExecuteToDos;
    public String id;


    public void initialize(){

        String userId =  LoginFormController.userID;
        String userName =  LoginFormController.name;

        lblTitle.setText ( "Hi "+userName+"  Welcome to To-Do List" );
        lblUserId.setText ( userId );
        paneAddToDo.setVisible ( false );
        btnDelete.setDisable ( true );
        btnUpdate.setDisable ( true );


        loadAllToDo();

        lstToDos.getSelectionModel ().selectedItemProperty ().addListener ( new ChangeListener<ToDo> ( ) {
            @Override
            public void changed ( ObservableValue<? extends ToDo> observable, ToDo oldValue, ToDo newValue ) {
                ToDo selectedItems = lstToDos.getSelectionModel ( ).getSelectedItem ( );

                btnUpdate.setDisable ( false );
                btnDelete.setDisable ( false );

                if ( selectedItems==null ){
                    return;
                }

                txtExecuteToDos.setText ( selectedItems.getDescription () );
                id = selectedItems.getId ();
            }
        } );

    }

    public void loadAllToDo(){
        ObservableList<ToDo> items = lstToDos.getItems ( );
        items.clear ();

        Connection connection = DBConnection.getInstance ( ).getConnection ( );
        try {
            PreparedStatement preparedStatement = connection.prepareStatement ( "select * from todo where user_id = ?" );

            preparedStatement.setObject ( 1,LoginFormController.userID );

            ResultSet resultSet = preparedStatement.executeQuery ( );

            while (resultSet.next ()){
                String id = resultSet.getString ( 1 );
                String description = resultSet.getString ( 2 );
                String use_id = resultSet.getString ( 3 );

                ToDo object = new ToDo ( id,description,use_id );

                items.add ( object );

            }

        } catch (SQLException throwables) {
            throwables.printStackTrace ( );
        }
    }

    public String autoGenerator(){
        Connection connection = DBConnection.getInstance ( ).getConnection ( );

        String newID = null;
        try {
            Statement statement = connection.createStatement ( );
            ResultSet resultSet = statement.executeQuery ( "select id from todo order by id desc limit 1" );

            if ( resultSet.next () ){
                String oldId = resultSet.getString ( 1 );

                oldId = oldId.substring ( 1, 4 );

                int intId = Integer.parseInt ( oldId );

                intId = intId + 1;

                if ( intId < 10 ){
                    newID = "T00" + intId;
                }else if ( intId < 100 ){
                    newID = "T0" + intId;
                }else if ( intId < 1000 ){
                    newID = "T" + intId;
                }

            }else {
                newID = "T001";
            }

        } catch (SQLException throwables) {
            throwables.printStackTrace ( );
        }
        System.out.println (newID );

        return newID;
    }

    public void btnAddNewToDoOnAction ( ActionEvent actionEvent ) {
        paneAddToDo.setVisible ( true );
        txtNewToDo.requestFocus ();
        autoGenerator ();
        loadAllToDo ();
    }

    public void btnUpdateOnAction ( ActionEvent actionEvent ) {
        Connection connection = DBConnection.getInstance ( ).getConnection ( );

        String description = txtExecuteToDos.getText ( );

        try {
            PreparedStatement preparedStatement = connection.prepareStatement ( "update todo set description=? where id=?;" );
            preparedStatement.setObject ( 1, description);
            preparedStatement.setObject ( 2, id);

            preparedStatement.executeUpdate ();

            loadAllToDo ();

            btnDelete.setDisable ( true );
            btnUpdate.setDisable ( true );
            txtExecuteToDos.clear ();

            new Alert ( Alert.AlertType.CONFIRMATION,"Wede Goda collo",ButtonType.OK ).showAndWait ();

        } catch (SQLException throwables) {
            throwables.printStackTrace ( );
        }
    }

    public void btnDeleteOnAction ( ActionEvent actionEvent ) {
        Alert alert = new Alert ( Alert.AlertType.CONFIRMATION, "sure", ButtonType.YES, ButtonType.CANCEL );
        Optional<ButtonType> buttonType = alert.showAndWait ( );

        if ( buttonType.get ().equals ( ButtonType.YES ) ){
            Connection connection = DBConnection.getInstance ( ).getConnection ( );
            try {
                PreparedStatement preparedStatement = connection.prepareStatement ( "delete from todo where id = ?" );
                preparedStatement.setObject ( 1,id );
                preparedStatement.executeUpdate ();
                loadAllToDo ();
                btnDelete.setDisable ( true );
                btnUpdate.setDisable ( true );
                txtExecuteToDos.clear ();

            } catch (SQLException throwables) {
                throwables.printStackTrace ( );
            }
        }
    }

    public void btnLogOutOnAction ( ActionEvent actionEvent ) throws IOException {
        Alert alert = new Alert ( Alert.AlertType.WARNING, "Sure da collo", ButtonType.YES, ButtonType.NO );
        Optional<ButtonType> buttonType = alert.showAndWait ( );

        if ( buttonType.get().equals(ButtonType.YES) ){
            Parent root = FXMLLoader.load ( this.getClass ().getResource ( "../view/LoginForm.fxml" ) );
            Scene scene = new Scene ( root );
            Stage primaryStage = (Stage) this.root.getScene ().getWindow ();
            primaryStage.setScene ( scene );
            primaryStage.setTitle ( "Login Form" );
            primaryStage.centerOnScreen ();
        }
    }

    public void addToDo(){
        Connection connection = DBConnection.getInstance ( ).getConnection ( );

        String id = autoGenerator ();
        String description = txtNewToDo.getText ();
        String userId = lblUserId.getText ();

        try {
            PreparedStatement preparedStatement = connection.prepareStatement ( "insert into todo values (?,?,?)" );
            preparedStatement.setObject ( 1,id );
            preparedStatement.setObject ( 2,description );
            preparedStatement.setObject ( 3,userId );

            preparedStatement.executeUpdate ();


            paneAddToDo.setVisible ( false );
        } catch (SQLException throwables) {
            throwables.printStackTrace ( );
        }
    }

    public void btnAddToListOnAction ( ActionEvent actionEvent ) {
        addToDo ();
        loadAllToDo ();
    }
}
