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
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.CheckBox;

/**
 * FXML Controller class
 *
 * @author Bakht Zada
 */
public class SettingController implements Initializable {
    @FXML
    private TextField user_name;

    @FXML
    private TextField ip_address;

    @FXML
    private PasswordField password;

    @FXML
    private TextField port_number;
    
    @FXML
    private CheckBox message_service;
    
    //private String message value 
    private PreparedStatement prestate;
    private ResultSet rs;
    private boolean isEnable;
    public void loadSetting() {
        try {
            Stage stage = new Stage();
            stage.setResizable(false);
            stage.initOwner(FXMLDocumentController.getPrimaryStage());
            Parent root = FXMLLoader.load(getClass().getResource("Setting.fxml"));
            stage.getIcons().add(new Image(getClass().getResourceAsStream("Icons/brick.png")));
            Scene myScene = new Scene(root);
            stage.setScene(myScene);
            stage.show();
        } catch (IOException ex) {
            ExceptionHandling.showException(ex);
        }
    }
    public void saveSetting() {
        try {
            new MySQLConnection();
            String query = "update setting set username = '"+user_name.getText()+"', "
                    + "ipaddress = '"+ip_address.getText()+"',port = '"+port_number.getText()+"', password = '"+password.getText()+"'"
                    + ", checkbox = '"+isEnable+"' where id = 1";
            prestate = databaseconnection.MySQLConnection.conn.prepareStatement(query);
            prestate.execute();
            Alert alert = new Alert(AlertType.CONFIRMATION);
            alert.setTitle("Setting");
            alert.setContentText("Your setting is successfully saved");  
            alert.show();
            prestate.close();
            MySQLConnection.conn.close();
        } catch (SQLException ex) {
            ExceptionHandling.showException(ex);
        }
    }
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        try {
            //retreiving the save setting
            new MySQLConnection();
            prestate = databaseconnection.MySQLConnection.conn.prepareStatement("select *from setting");
            rs = prestate.executeQuery();
            if(rs.next()){
                user_name.setText(rs.getString("username"));
                ip_address.setText(rs.getString("ipaddress"));
                port_number.setText(rs.getString("port"));
                password.setText(rs.getString("password"));
                if(rs.getString("checkbox").equals("true")){
                    message_service.selectedProperty().set(true);
                    user_name.setDisable(false);
                    ip_address.setDisable(false);
                    port_number.setDisable(false);
                    password.setDisable(false); 
                }
                if(rs.getString("checkbox").equals("false")){
                    message_service.selectedProperty().set(false);
                    user_name.setDisable(true);
                    ip_address.setDisable(true);
                    port_number.setDisable(true);
                    password.setDisable(true);
                }
            }
            prestate.close();
            rs.close();
            MySQLConnection.conn.close();
        } catch (SQLException ex) {
            ExceptionHandling.showException(ex);
        }
        message_service.selectedProperty().addListener(new ChangeListener<Boolean> () {
            @Override
            public void changed(ObservableValue<? extends Boolean> ov,
                    final Boolean value, final Boolean newValue ) {
                isEnable = newValue;
                if(isEnable == true) {
                    user_name.setDisable(false);
                    ip_address.setDisable(false);
                    port_number.setDisable(false);
                    password.setDisable(false); 
                }
                else {
                    user_name.setDisable(true);
                    ip_address.setDisable(true);
                    port_number.setDisable(true);
                    password.setDisable(true);
                }
            }
        });
    }       
}
