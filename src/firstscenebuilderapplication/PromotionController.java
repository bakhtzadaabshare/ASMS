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
import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.ResourceBundle;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;
import java.io.OutputStream;
import java.util.Optional;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.layout.AnchorPane;

/**
 * FXML Controller class
 *
 * @author Bakht Zada
 */

public class PromotionController implements Initializable {
    
    @FXML
    private AnchorPane promotion_window;

    @FXML
    private TextField student_id;

    @FXML
    private Label name;

    @FXML
    private Label father_name;

    @FXML
    private Label father_nic;

    @FXML
    private Label address;

    @FXML
    private Label dob;

    @FXML
    private Label mobile_number;

    @FXML
    private Label current_class;

    @FXML
    private ComboBox class_of_promotion;
    
    @FXML
    private ComboBox class_of_study;
    
    @FXML
    private ComboBox promoted_class;

    @FXML
    private Label date_of_admission;
    
    @FXML
    private Label class_of_admission;

    @FXML
    private Circle student_image;

    @FXML
    private TextField date_of_promotion;
    
    @FXML
    private AnchorPane promote_whole_class;
    
    @FXML
    private AnchorPane promote_a_student;

    private PreparedStatement prestate;
    private ResultSet rs;
    private int classid;
    private int promotedclass;
    private String currentclass;
    private int classInWhichStudy;
    private int classToWhichPromoted;
    private String c_class;
    private String p_class;
    // This function load the promotion from 
    public void openPromotion() throws IOException {
        Stage stage = new Stage();
        stage.setResizable(false);
        stage.initOwner(FXMLDocumentController.getPrimaryStage());
        Parent root = FXMLLoader.load(getClass().getResource("Promotion.fxml"));
        stage.getIcons().add(new Image(getClass().getResourceAsStream("Icons/brick.png")));
        Scene myScene = new Scene(root);
        stage.setScene(myScene);
        stage.show();
    }
    public void searchStudentDetails() throws FileNotFoundException, IOException {
        if(student_id.getText().isEmpty()) {
            Alert alert = new Alert(AlertType.INFORMATION);
            alert.setTitle("Student Id cann't be empty");
            alert.setContentText("Enter the student Id and press the search button again");
            alert.show();
        }
        else {
            try {
                int studentId = Integer.parseInt(student_id.getText());
                String query = "select * from student where student_id = "+studentId+"";
                //new MySQLConnection();
                prestate = databaseconnection.MySQLConnection.conn.prepareStatement(query);
                rs = prestate.executeQuery();
                if(rs.next()) {
                    name.setText(rs.getString("student_name"));
                    father_name.setText(rs.getString("std_father_name"));
                    father_nic.setText(rs.getString("std_father_nic"));
                    address.setText(rs.getString("address"));
                    dob.setText(rs.getString("dob"));
                    mobile_number.setText(rs.getString("cell"));
                    classid= rs.getInt("class_id");
                    date_of_admission.setText(rs.getString("date_of_admission"));
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
                   student_image.setFill(new ImagePattern(image));
                   rs.close();
                    prestate.close();
                }
                else {
                    Alert alert = new Alert(AlertType.INFORMATION);
                    alert.setTitle("Record not Found");
                    alert.setContentText("No record found for your entered student id. Please try againg");
                    alert.show();
                }
                //these lines of code take the corresponding class name from the class table 
                prestate = databaseconnection.MySQLConnection.conn.prepareStatement("select class_name from class where class_id = "+classid+"");
                rs = prestate.executeQuery();
                if(rs.next()){
                    currentclass = rs.getString("class_name");
                    class_of_admission.setText(currentclass);
                }
                //These lines of code retrive the class of promotion if exist
                String myquery = "select class_name from class, promotedclass"
                        + " where student_id = "+studentId+" and class.class_id = promotedclass.class_id";
                prestate = databaseconnection.MySQLConnection.conn.prepareStatement(myquery);
                rs = prestate.executeQuery();
                if(rs.next()) {
                    current_class.setText(rs.getString("class_name"));
                }
                else {
                   current_class.setText(currentclass); 
                }
                prestate.close();
                rs.close();
            } catch (SQLException ex) {
               ExceptionHandling.showException(ex);
            }

        }
    }
    public void studentPromotion(){
        new MySQLConnection();
        String promotiondate = date_of_promotion.getText();
        int studentId = Integer.parseInt(student_id.getText());
        if(student_id.getText().isEmpty()){
            Alert alert = new Alert(AlertType.INFORMATION);
            alert.setTitle("Student Id cann't be empty");
            alert.setContentText("Enter the student Id and press the search button again");
            alert.show();
        }
        else{
            try {
                String updateQuery = "update promotedclass set class_id = ?, promotion_date = ?"
                        + " where student_id = "+studentId+"";
                prestate = databaseconnection.MySQLConnection.conn.prepareStatement(updateQuery);
                prestate.setInt(1, promotedclass);
                prestate.setString(2, promotiondate);
                prestate.executeUpdate();
                Alert alert = new Alert(AlertType.INFORMATION);
                alert.setTitle("Your record has been saved");
                alert.setContentText("Your record is successfully saved.");
                alert.show();
                prestate.close();
            } catch (SQLException ex) {
              ExceptionHandling.showException(ex);
            }
        }
    }
    public void promoteStudent() {
        promote_whole_class.setVisible(false);
        promote_a_student.setVisible(true);
    }
    public void promoteClass() {
        promote_a_student.setVisible(false);
        promote_whole_class.setVisible(true);
    }
    public void saveClassPromotion() {
        try {
            new MySQLConnection();
            if(classToWhichPromoted !=0 && classInWhichStudy  != 0){
                Alert alert = new Alert(AlertType.CONFIRMATION);
                alert.setTitle("Confirm it");
                alert.setContentText("Do you want to promote class "+c_class+" students to "+p_class+"? Are you sure about it?");
                Optional<ButtonType> option = alert.showAndWait();
                if(option.isPresent() && option.get() == ButtonType.OK){
                    String promoteAClass = "update promotedclass set class_id = "+classToWhichPromoted+" where class_id = "+classInWhichStudy+" ";
                    prestate = databaseconnection.MySQLConnection.conn.prepareStatement(promoteAClass);
                    prestate.execute();
                    Alert alert1 = new Alert(AlertType.INFORMATION);
                    alert1.setTitle("Promotion done successfully");
                    alert1.setContentText("All the students of class "+c_class+" is successfully promoted to class "+p_class+"");
                    alert1.show();
                }
            }
            else {
                Alert alert = new Alert(AlertType.INFORMATION);
                alert.setTitle("Please selection first");
                alert.setContentText("Please select current and promoted class first then click on the save button");
                alert.show();
            }
            prestate.close();
   
        } catch (SQLException ex) {
            ExceptionHandling.showException(ex);
        }
    }
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        new MySQLConnection();
        DateFormat dateformat=new SimpleDateFormat("yyyy-MM-dd");
        Date date=new Date();
        date_of_promotion.setText(dateformat.format(date));
        try {
            //let take datafrom database and assign value to combobx
            String query = "select class_name from class";
            prestate = databaseconnection.MySQLConnection.conn.prepareStatement(query);
            rs = prestate.executeQuery();
            while(rs.next()) {
                class_of_promotion.getItems().add(rs.getString("class_name"));
                promoted_class.getItems().add(rs.getString("class_name"));
                class_of_study.getItems().add(rs.getString("class_name"));
            }
            rs.close();
            prestate.close();
        } catch (SQLException ex) {
            ExceptionHandling.showException(ex);
        }
        class_of_promotion.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>(){
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                try {
                    String qurey = "select class_id from class where class_name = '"+newValue+"'";
                    prestate = databaseconnection.MySQLConnection.conn.prepareStatement(qurey);
                    rs = prestate.executeQuery();
                    if(rs.next()){
                        promotedclass = rs.getInt("class_id");
                    }
                    rs.close();
                    prestate.close();
                } catch (SQLException ex) {
                    ExceptionHandling.showException(ex);
                }
            }
        });
        promoted_class.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>(){
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                try {
                    p_class = newValue;
                    String qurey = "select class_id from class where class_name = '"+newValue+"'";
                    prestate = databaseconnection.MySQLConnection.conn.prepareStatement(qurey);
                    rs = prestate.executeQuery();
                    if(rs.next()){
                        classToWhichPromoted = rs.getInt("class_id");
                    }
                    rs.close();
                    prestate.close();
                } catch (SQLException ex) {
                    ExceptionHandling.showException(ex);
                }
            }
        });
        class_of_study.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>(){
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                try {
                    c_class = newValue;
                    String qurey = "select class_id from class where class_name = '"+newValue+"'"; 
                    prestate = databaseconnection.MySQLConnection.conn.prepareStatement(qurey);
                    rs = prestate.executeQuery();
                    if(rs.next()){
                        classInWhichStudy = rs.getInt("class_id");
                    }
                    rs.close();
                    prestate.close();
                } catch (SQLException ex) {
                    ExceptionHandling.showException(ex);
                }
            }
        });
    }    
    
}
