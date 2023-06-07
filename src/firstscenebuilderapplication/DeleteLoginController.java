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
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author Bakht Zada
 */
public class DeleteLoginController implements Initializable {
    
    @FXML
    private TextField user_name;

    @FXML
    private PasswordField password; 
    
    private static Stage stage;
    //DeleteLoginController obj;
    public static String deleteQuery;
    

    public void loaDeleteLogin() throws IOException {
        try {
            stage = new Stage();
            stage.setResizable(false);
            //stage.initOwner(FXMLDocumentController.getPrimaryStage());
            Parent root = FXMLLoader.load(getClass().getResource("DeleteLogin.fxml"));
            stage.getIcons().add(new Image(getClass().getResourceAsStream("Icons/brick.png")));
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        } catch (IOException ex) {
            ExceptionHandling.showException(ex);
        }
    }
    public void deleteLoginAction() {
        new MySQLConnection();
        try {
            String userName = FXMLDocumentController.u_name;
            String query = "Select * from login where lower(user_name) = '"+userName+"'";
            PreparedStatement prestate = databaseconnection.MySQLConnection.conn.prepareStatement(query);
            ResultSet rs = prestate.executeQuery();
            if(!(user_name.getText().isEmpty() || password.getText().isEmpty())){
                String name = user_name.getText();
                String pass = password.getText();
                if(rs.next()){
                    if(name.equals(rs.getString("user_name")) && pass.equals(rs.getString("password"))){
            try {
                prestate = databaseconnection.MySQLConnection.conn.prepareStatement(this.deleteQuery);
                prestate.execute();
                Alert message = new Alert(Alert.AlertType.INFORMATION);
                message.setTitle("Your record is deleted");
                message.setContentText("The record is successfully deleted");
                message.show();
                // Call the tableViewForClass() function in order to reload the dat
                prestate.close();
                if(!MySQLConnection.conn.isClosed()) {
                    MySQLConnection.conn.close();
                }
                } catch (SQLException ex) {
            ExceptionHandling.showException(ex);
            }
                        stage.close();
                    }
                    else {
                        Alert alert = new Alert(Alert.AlertType.ERROR);
                        alert.setTitle("Login Field");
                        alert.setContentText("User name or password is incorrect. Please try gain");
                        alert.show(); 
                    }
                }
            }
        else {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Enter Name and Password");
            alert.setContentText("Please enter user name and password then click on login button");
            alert.show();
        }
        rs.close();
        prestate.close();
        //MySQLConnection.conn.close();
        } catch (SQLException ex) {
                ExceptionHandling.showException(ex);
        }
    }
    @Override
    public void initialize(URL url, ResourceBundle rb) {
    }    
    
}
