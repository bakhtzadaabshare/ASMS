/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package firstscenebuilderapplication;

import databaseconnection.MySQLConnection;
import java.io.IOException;
import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author DIBB E6430
 */
public class SchoolNameController implements Initializable {
        @FXML
    private Pane enteryform;

    @FXML
    private Button save_btn1;

    @FXML
    private TextField userName;

    @FXML
    private PasswordField password;

    @FXML
    private Pane loginform;

    @FXML
    private TextField schoolname;

    /**
     * Initializes the controller class.
     */
    PreparedStatement prestate;
    ResultSet rs;
    public void loadSchoolName() {
        try {
            Stage stage = new Stage();
            stage.setResizable(false);
            stage.initOwner(FXMLDocumentController.getPrimaryStage());
            Parent root = FXMLLoader.load(getClass().getResource("SchoolName.fxml"));
            stage.getIcons().add(new Image(getClass().getResourceAsStream("Icons/brick.png")));
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
            
        } catch (IOException ex) {
            ExceptionHandling.showException(ex);
        }
    }
    public void insertName() {
        new MySQLConnection();
        if(schoolname.getText().isEmpty()) {
            Alert message = new Alert(Alert.AlertType.WARNING);
            message.setTitle("Form is not completed!");
            message.setContentText("Enter the name of the school and try again");
            message.show();
        }
        else {
            try {
                String query = "update school set name = '"+schoolname.getText()+"' where id = 1";
                prestate = databaseconnection.MySQLConnection.conn.prepareStatement(query);
                prestate.execute();
                //Call the tableViewForClass() function in order to reload the data 
                prestate.close();
                if(!MySQLConnection.conn.isClosed()) {
                    MySQLConnection.conn.close();
                }
                Alert message = new Alert(Alert.AlertType.INFORMATION);
                message.setTitle("Your Record Is saved");
                message.setContentText("Your Record is Successfully saved");
                message.show();
                schoolname.clear();
            } catch (SQLException ex) {
                ExceptionHandling.showException(ex);
            }
        }
    }
    public void enterCredential() {
        new MySQLConnection();
        if(userName.getText().isEmpty()||password.getText().isEmpty()) {
            Alert message = new Alert(Alert.AlertType.WARNING);
            message.setTitle("Form is not completed!");
            message.setContentText("Enter the owner credentials and try again");
            message.show();
        }
        else {
            try {
                String query = "select *from owner";
                prestate = databaseconnection.MySQLConnection.conn.prepareStatement(query);
                rs = prestate.executeQuery();
                if(rs.next()){
                    String user = userName.getText();
                    String pass = password.getText();
                    if(user.equals(rs.getString("name"))&& pass.equals(rs.getString("password"))){
                        loginform.setVisible(false);
                        enteryform.setVisible(true);
                    }
                    else {
                        Alert message = new Alert(Alert.AlertType.INFORMATION);
                        message.setTitle("Sorry! Login field");
                        message.setContentText("The password or user name is wrong");
                        message.show();
                        
                    }
                }
                //Call the tableViewForClass() function in order to reload the data 
                prestate.close();
                if(!MySQLConnection.conn.isClosed()) {
                    MySQLConnection.conn.close();
                }
               schoolname.clear();
            } catch (SQLException ex) {
                ExceptionHandling.showException(ex);
            }
        }
    }
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    
    
}
