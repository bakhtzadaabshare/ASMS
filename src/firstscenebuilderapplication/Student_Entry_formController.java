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
import java.net.MalformedURLException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Toggle;
import javafx.scene.control.ToggleButton;
import javafx.scene.paint.Color;
import javax.swing.filechooser.FileSystemView;

/**
 * FXML Controller class
 *
 * @author Bakht Zada
 */



public class Student_Entry_formController implements Initializable {
   
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
    private ToggleGroup gender;

    @FXML
    private TextField date_of_admission;

    @FXML
    private ComboBox class_of_admission;

    @FXML
    private Circle student_image;
    //Declaring imag_url and profile image as global variable in order to pass it to the connection function
    File image_url=null;
    Image profile_image;
    String stdgender; 
    int classofadmission;
    ResultSet rs = null;
    byte[] myimage = null;
    private PreparedStatement prestate = null;

    // This function load the student entry form 
        public void openStudentEntryRecord() throws IOException {
        Stage stage = new Stage();
        stage.setResizable(false);
        stage.initOwner(FXMLDocumentController.getPrimaryStage());
        Parent root = FXMLLoader.load(getClass().getResource("Student_Entry_form.fxml"));
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
            student_image.setFill(new ImagePattern(profile_image));
        }
    }
    // this button clean all the field in order to insert new data
    public void addNewRecord() {
        std_id.clear();
        std_name.clear();
        std_father_name.clear();
        father_nic.clear();
        std_cast.clear();
        std_address.clear();
        std_cell.clear();
        std_dob.clear();
        student_image.setFill(Color.MEDIUMSEAGREEN);
    }
   //this function store data into database
    public void savaStudentRecord() throws IOException {
        try {
            new MySQLConnection();
            String name, f_name, nic, caste, address, cell, dob, dateofadmission;
            name = std_name.getText();
            f_name = std_father_name.getText();
            nic = father_nic.getText();
            caste = std_cast.getText();
            address = std_address.getText();
            cell = std_cell.getText();
            dob = std_dob.getText();
            dateofadmission = date_of_admission.getText();
            //This conditionChek that all the fields are empty or not
            if(std_id.getText().isEmpty() || name.isEmpty() || f_name.isEmpty()
                    && nic.isEmpty() || caste.isEmpty() || address.isEmpty() || cell.isEmpty()
                    && dob.isEmpty() || dateofadmission.isEmpty() || image_url == null) {
                Alert message = new Alert(AlertType.WARNING);
                message.setTitle("Fill all the fields");
                message.setContentText("All the textfields are mendatory to be filled. Please filled all the fields and select the picture");
                message.show();
            }
            else {            
                //Defining query for the insertion statement of the database
                String insertquery = "insert into student(student_id, student_name, std_father_name, std_father_nic,"
                        + " address, dob, cell, gender, class_id, date_of_admission, caste, picture)"
                        + "values(?,?,?,?,?,?,?,?,?,?,?,?) ";
                String promotion = "insert into promotedclass(student_id, class_id, promotion_date) values(?,?,?)";
                int id;
                id = Integer.parseInt(std_id.getText());
                prestate = databaseconnection.MySQLConnection.conn.prepareStatement(insertquery);
                prestate.setInt(1, id);
                prestate.setString(2, name);
                prestate.setString(3, f_name);
                prestate.setString(4, nic);
                prestate.setString(5, address);
                prestate.setString(6, dob);
                prestate.setString(7, cell);
                prestate.setString(8, stdgender);
                prestate.setInt(9, classofadmission);
                prestate.setString(10, dateofadmission);
                prestate.setString(11, caste);
                //converting file to stream
                FileInputStream image_stream = new FileInputStream(image_url);
                byte[] buf = new byte[1024];
                ByteArrayOutputStream bos = new ByteArrayOutputStream();
                for(int readNum; (readNum = image_stream.read(buf))!=-1; ) {
                    bos.write(buf,0,readNum);
                }
                myimage = bos.toByteArray();
                prestate.setBytes(12, myimage);
                //prestate.setBinaryStream(12, image_stream);
                prestate.execute();
                image_stream.close();
                prestate.close();
                prestate = databaseconnection.MySQLConnection.conn.prepareStatement(promotion);
                prestate.setInt(1, id);
                prestate.setInt(2, classofadmission);
                prestate.setString(3, dateofadmission);
                prestate.execute();

                Alert mymessage = new Alert(Alert.AlertType.INFORMATION);
                mymessage.setTitle("Congratulation!");
                mymessage.setContentText("Your record is succefully saved!");
                mymessage.show();
            }
            MySQLConnection.conn.close();
        } catch (SQLException ex) {
            ExceptionHandling.showException(ex);
        }
    }
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        try {
            //Picking date from computer and setting it to the date of admission.
            DateFormat dateformat=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date date=new Date();
            date_of_admission.setText(dateformat.format(date));
            //object of the MySQLConnection in order to initialize conn object;
            new MySQLConnection();
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
            } catch (SQLException ex) {
                ExceptionHandling.showException(ex);
            }
            
            //this function take values from the combo box and assign it to the classofadmission variable
            class_of_admission.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>(){
                @Override
                public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                    new MySQLConnection();
                    try {
                        String qurey = "select class_id from class where class_name = '"+newValue+"'";
                        prestate = databaseconnection.MySQLConnection.conn.prepareStatement(qurey);
                        rs = prestate.executeQuery();
                        if(rs.next()){
                            classofadmission = rs.getInt("class_id");
                        }
                        rs.close();
                        prestate.close();
                        MySQLConnection.conn.close();
                    } catch (SQLException ex) {
                        ExceptionHandling.showException(ex);
                    }
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
            //handel toggel button action and assigning value to the gender
            gender.selectedToggleProperty().addListener(new ChangeListener <Toggle>() {
                @Override
                public void changed(ObservableValue<? extends Toggle> observable, Toggle oldValue, Toggle newValue) {
                    stdgender = ((ToggleButton)newValue).getText();
                }
            });
            MySQLConnection.conn.close();
        } catch (SQLException ex) {
            ExceptionHandling.showException(ex);
        }
    }    
}
