/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package firstscenebuilderapplication;

import databaseconnection.MySQLConnection;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.image.Image;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Toggle;
import javafx.scene.control.ToggleButton;
import javafx.scene.paint.Color;
import javax.swing.filechooser.FileSystemView;





/**
 * FXML Controller class
 *
 * @author Bakht Zada
 */



public class TeacherEntryRecordController implements Initializable {
   

    @FXML
    private TextField teacher_id;

    @FXML
    private TextField teacher_name;

    @FXML
    private TextField teacher_father_name;

    @FXML
    private TextField teacher_nic;

    @FXML
    private TextField designation;

    @FXML
    private TextField teacher_address;

    @FXML
    private TextField teacher_cell;

    @FXML
    private TextField teacher_dob;

    @FXML
    private TextField teacher_qualification;

    @FXML
    private TextField teacher_major;
    
    @FXML
    private ToggleGroup teacher_gender;

    @FXML
    private Circle teacher_image;
    //Declaring imag_url and profile image as global variable in order to pass it to the connection function
    File image_url=null;
    Image profile_image;
    String gender;
    private PreparedStatement prestate = null;
    // This function load the teacher entry form 
    public void openTeacherEntryRecord() throws IOException {
        Stage stage = new Stage();
        stage.setResizable(false);
        stage.initOwner(FXMLDocumentController.getPrimaryStage());
        Parent root = FXMLLoader.load(getClass().getResource("TeacherEntryRecord.fxml"));
        stage.getIcons().add(new Image(getClass().getResourceAsStream("Icons/brick.png")));
        Scene myScene = new Scene(root);
        stage.setScene(myScene);
        stage.show();
        }
    //This function choose picture path 
    public void chooseImage()  {
        FileChooser choose_myimage = new FileChooser();
        choose_myimage.setInitialDirectory(FileSystemView.getFileSystemView().getDefaultDirectory());
        //Adding a filter to file chooser that will only select image file 
        FileChooser.ExtensionFilter extentionfilter = new FileChooser.ExtensionFilter("Image File (*.jpg)","*.png","*.jpg");
        choose_myimage.setTitle("Select profile Image");
        choose_myimage.getExtensionFilters().add(extentionfilter);
         image_url = choose_myimage.showOpenDialog(new Stage());
        if (image_url != null) {
            try {
                profile_image = new Image(image_url.toURI().toURL().toExternalForm());
            } catch (MalformedURLException ex) {
                Alert mymessage = new Alert(Alert.AlertType.ERROR);
                mymessage.setTitle("Image not found");
                mymessage.setContentText(ex.toString());
                mymessage.show();
            }
            teacher_image.setFill(new ImagePattern(profile_image));
        }
    }
    // this button clean all the field in order to insert new data
    public void addNewRecord() {
        teacher_id.clear();
        teacher_name.clear();
        teacher_father_name.clear();
        teacher_nic.clear();
        designation.clear();
        teacher_address.clear();
        teacher_cell.clear();
        teacher_dob.clear();
        teacher_qualification.clear();
        teacher_major.clear();  
        teacher_image.setFill(Color.MEDIUMSEAGREEN);
    }
   //this function store data into database
    public void savaDataIntoDatabase() {
        try {
            new MySQLConnection();
            String name, f_name, nic, design, address, cell, dob, qualification, major;
            name = teacher_name.getText();
            f_name = teacher_father_name.getText();
            nic = teacher_nic.getText();
            design = designation.getText();
            address = teacher_address.getText();
            cell = teacher_cell.getText();
            dob = teacher_dob.getText();
            qualification = teacher_qualification.getText();
            major = teacher_major.getText();
            //This conditionChek that all the fields are empty or not
            if(teacher_id.getText().isEmpty() || name.isEmpty() || f_name.isEmpty()
                    && nic.isEmpty() || design.isEmpty() || address.isEmpty() || cell.isEmpty()
                    && dob.isEmpty() || qualification.isEmpty() || major.isEmpty() || image_url == null) {
                Alert message = new Alert(AlertType.WARNING);
                message.setTitle("Fill all the fields");
                message.setContentText("All the textfields are mendatory to be filled. Please filled all the fields and select the picture");
                message.show();
            }
            else {
                    //Defining query for the insertion statement of the database
                    String insertquery = "Insert into teacher(teacher_id, teacher_name, father_name, nic,"
                            + " address, dob, cell, qualification, major, designation, Gender, picture)"
                            + "values(?,?,?,?,?,?,?,?,?,?,?,?) ";
                    int id;
                    id = Integer.parseInt(teacher_id.getText());
                    prestate = databaseconnection.MySQLConnection.conn.prepareStatement(insertquery);
                    prestate.setInt(1, id);
                    prestate.setString(2, name);
                    prestate.setString(3, f_name);
                    prestate.setString(4, nic);
                    prestate.setString(5, address);
                    prestate.setString(6, dob);
                    prestate.setString(7, cell);
                    prestate.setString(8, qualification);
                    prestate.setString(9, major);
                    prestate.setString(10, design);
                    prestate.setString(11, gender);
                    //converting file to stream
                    InputStream image_stream = new FileInputStream(image_url);
                    byte[] buf = new byte[1024];
                    ByteArrayOutputStream bos = new ByteArrayOutputStream();
                    for(int readNum; (readNum = image_stream.read(buf))!=-1; ) {
                        bos.write(buf,0,readNum);
                    }
                    byte[] myimage = bos.toByteArray();
                    prestate.setBytes(12, myimage);
                    prestate.execute();
                    prestate.close();
                    image_stream.close();
                    Alert mymessage = new Alert(Alert.AlertType.INFORMATION);
                    mymessage.setTitle("Congratulation!");
                    mymessage.setContentText("Your record is succefully saved!");
                    mymessage.show();
            }   
            MySQLConnection.conn.close();
        } catch (SQLException ex) {
            ExceptionHandling.showException(ex);
        } catch (FileNotFoundException ex) {
            ExceptionHandling.showException(ex);
        } catch (IOException ex) {
            ExceptionHandling.showException(ex);
        }
    }
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        teacher_cell.textProperty().addListener(new ChangeListener<String>() {
        @Override
        public void changed(ObservableValue<? extends String> observable, String oldValue, 
            String newValue) {
            if (!newValue.matches("\\d{0,11}")) {
                teacher_cell.setText(oldValue);
            }
            }
            });
        teacher_nic.textProperty().addListener(new ChangeListener<String>() {
        @Override
        public void changed(ObservableValue<? extends String> observable, String oldValue, 
            String newValue) { //15303-7056230-7
            if (!newValue.matches("\\d{0,13}")) {
                teacher_nic.setText(oldValue);
            }
            }
        });
        //handel toggel button action and assigning value to the gender
        teacher_gender.selectedToggleProperty().addListener(new ChangeListener <Toggle>() {
        @Override
        public void changed(ObservableValue<? extends Toggle> observable, Toggle oldValue, Toggle newValue) {
            gender = ((ToggleButton)newValue).getText();
            }        
        }); 
    }    
}
