/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package firstscenebuilderapplication;

import databaseconnection.MySQLConnection;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.Toggle;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author Bakht Zada
 */
public class EditStudentInfoController implements Initializable {
    
    @FXML
    private AnchorPane Insert_std_record;

    @FXML
    private TextField std_id;
    

    @FXML
    private TextField std_name;

    @FXML
    private TextField std_father_name;

    @FXML
    private TextField father_nic;

    @FXML
    private TextField std_cast;

    @FXML
    private TextField std_address;

    @FXML
    private TextField std_cell;

    @FXML
    private TextField std_dob;

    @FXML
    private TextField date_of_admission;

    @FXML
    private ComboBox class_of_admission;
    
    @FXML
    private Circle std_image;
    
    @FXML
    private RadioButton male;

    @FXML
    private ToggleGroup gender;

    @FXML
    private RadioButton female;

    @FXML
    private RadioButton other;
    
    private PreparedStatement prestate;
    private ResultSet rs;
    public static int id;
    private int classofadmission;
    private String studentGender;
    public void openEditeStuentInfo(){
        try {
            Stage stage = new Stage();
            stage.setResizable(false);
            stage.initOwner(FXMLDocumentController.getPrimaryStage());
            Parent root = FXMLLoader.load(getClass().getResource("EditStudentInfo.fxml"));
            stage.getIcons().add(new Image(getClass().getResourceAsStream("Icons/brick.png")));
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show(); 
        } catch (IOException ex) {
            Logger.getLogger(EditStudentInfoController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    public void loadData(){
        try {
            std_id.setText(String.valueOf(id));
            std_id.setDisable(true);
            prestate = databaseconnection.MySQLConnection.conn.prepareStatement("select * from student where student_id = "+id+"");
            rs = prestate.executeQuery();
            if(rs.next()){
                std_name.setText(rs.getString("student_name"));
                std_father_name.setText(rs.getString("std_father_name"));
                father_nic.setText(rs.getString("std_father_nic"));
                std_address.setText(rs.getString("address"));
                std_dob.setText(rs.getString("dob"));
                std_cell.setText(rs.getString("cell"));
                date_of_admission.setText(rs.getString("date_of_admission"));
                std_cast.setText(rs.getString("caste"));
                String gen = rs.getString("gender");
                if(gen.equals("Male")){
                    gender.selectToggle(male);
                    studentGender = "Male";
                }
                else if(gen.equals("Female")){
                    gender.selectToggle(female);
                    studentGender = "Female";
                }
                else {
                    gender.selectToggle(other);
                    studentGender = "Other";
                }
                InputStream is = rs.getBinaryStream("picture");
                OutputStream os = new FileOutputStream( new File("photo.jpg"));
                byte[] content = new byte[1024];
                int size = 0;
                while((size = is.read(content)) != -1){
                    os.write(content, 0, size);
                }
                os.close();
                is.close();
                Image image = new Image("file:photo.jpg");  
                std_image.setFill(new ImagePattern(image));
                int classId = rs.getInt("class_id");
                prestate = databaseconnection.MySQLConnection.conn.prepareStatement("select class_name from class where class_id = "+classId+"");
                rs = prestate.executeQuery();
                if(rs.next()){
                    class_of_admission.setPromptText(rs.getString("class_name"));
                }
            }
            rs.close();
            prestate.close();
        } catch (SQLException ex) {
            ExceptionHandling.showException(ex);
        } catch (FileNotFoundException ex) {
            ExceptionHandling.showException(ex);
        } catch (IOException ex) {
            ExceptionHandling.showException(ex);
        }
    }
    public void updateRecord() {
        try {
            new MySQLConnection();
            String updateQuery = "update student set student_name = ?, std_father_name =?"
                    + ",std_father_nic = ?, address = ?, dob = ?,cell = ?, gender = ?, class_id = ?"
                    + ", date_of_admission = ?, caste = ? "
                    + "where student_id = "+id+"";
            prestate = databaseconnection.MySQLConnection.conn.prepareStatement(updateQuery);
            prestate.setString(1, std_name.getText());
            prestate.setString(2, std_father_name.getText());
            prestate.setString(3, father_nic.getText());
            prestate.setString(4, std_address.getText());
            prestate.setString(5, std_dob.getText());
            prestate.setString(6, std_cell.getText());
            prestate.setString(7, studentGender);
            if(classofadmission == 0){
                PreparedStatement pre = databaseconnection.MySQLConnection.conn.prepareStatement("select class_id from student where student_id = "+id+"");
                rs = pre.executeQuery();
                if(rs.next()){
                     prestate.setInt(8, rs.getInt("class_id"));
                }
                pre.close();
            }
            else {
                prestate.setInt(8, classofadmission);
            }
            prestate.setString(9, date_of_admission.getText());
            prestate.setString(10, std_cast.getText());
            prestate.executeUpdate();
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Update Completed");
            alert.setContentText("Record is successfully updated.");
            alert.show();
            rs.close();
            prestate.close();
        } catch (SQLException ex) {
            ExceptionHandling.showException(ex);
        }
    }
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        new MySQLConnection();
        loadData();
        try {
            //let take datafrom database and assign value to combobx
            String query = "select class_name from class";
            prestate = databaseconnection.MySQLConnection.conn.prepareStatement(query);
            rs = prestate.executeQuery();
            while(rs.next()) {
                class_of_admission.getItems().add(rs.getString("class_name"));
            }
            rs.close();
            prestate.close();
            MySQLConnection.conn.close();
        } catch (SQLException ex) {
            ExceptionHandling.showException(ex);
        }

        //this function take values from the combo box and assign it to the classofadmission variable
        class_of_admission.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>(){
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                int classes = class_of_admission.getSelectionModel().getSelectedIndex();
                classofadmission = classes + 1;
            }
        });
        //This function impose constrain on the mobile number
        std_cell.textProperty().addListener(new ChangeListener<String>() {
        @Override
        public void changed(ObservableValue<? extends String> observable, String oldValue, 
            String newValue) {
            if (!newValue.matches("\\d{0,11}")) {
                std_cell.setText(oldValue);
            }
            }
            });
        father_nic.textProperty().addListener(new ChangeListener<String>() {
        @Override
        public void changed(ObservableValue<? extends String> observable, String oldValue, 
            String newValue) {
            if (!newValue.matches("\\d{0,13}")) {
                father_nic.setText(oldValue);
            }
            }
        });
        gender.selectedToggleProperty().addListener(new ChangeListener <Toggle>() {
        @Override
        public void changed(ObservableValue<? extends Toggle> observable, Toggle oldValue, Toggle newValue) {
            studentGender = ((ToggleButton)newValue).getText();
            }        
        }); 
    }      
}
