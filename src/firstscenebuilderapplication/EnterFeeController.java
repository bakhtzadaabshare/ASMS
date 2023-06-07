/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package firstscenebuilderapplication;

import databaseconnection.MySQLConnection;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.ResourceBundle;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author Bakht Zada
 */
public class EnterFeeController implements Initializable {
    // variable and basic objects declarition
     @FXML
    private TextField student_id;

    @FXML
    private Label name;

    @FXML
    private Label father_name;

    @FXML
    private Label address;

    @FXML
    private Label current_class;

    @FXML
    private Circle student_image;

    @FXML
    private TextField date;

    @FXML
    private ComboBox month;

    @FXML
    private TextField fee;

    @FXML
    private TextField total_fee;

    @FXML
    private TextField remaining_fee;
    private String feeMonth;
    private PreparedStatement prestate;
    private ResultSet rs;
    private int classId;
    private int totalMonthlyFee;
    private String studentName;
    private String fatherName;
    private String phone;
    private String Status;
    private URL url;
    private String port;
    private String username;
    private String ipaddress;
    private String password;
    private String checkBoxValue;
    private String message;
    public void enterFee() {
        try {
            Stage stage = new Stage();
            stage.setResizable(false);
            stage.initOwner(FXMLDocumentController.getPrimaryStage());
            Parent root = FXMLLoader.load(getClass().getResource("EnterFee.fxml"));
            stage.getIcons().add(new Image(getClass().getResourceAsStream("Icons/brick.png")));
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        } catch (IOException ex) {
            ExceptionHandling.showException(ex);
        }
    }
    public void searchStudent(){
        new MySQLConnection();
        if(student_id.getText().isEmpty()){
            Alert alert = new Alert(AlertType.INFORMATION);
            alert.setTitle("Enter Student Id");
            alert.setContentText("Enter student Id then press search button!");
            alert.show();
        }
        else {
            int std_id = Integer.parseInt(student_id.getText());
            String query = "select * from student where student_id = "+std_id+"";
            String classQuery = "select class.class_id, class_name from class, promotedclass"
                + " where promotedclass.class_id = class.class_id" +
                  " and promotedclass.student_id = "+std_id+"";
            String selectClass = "SELECT class.class_id, class.class_name from class, student where student_id = "+std_id+" "
                    + "and student.class_id = class.class_id";
            try {
                prestate = databaseconnection.MySQLConnection.conn.prepareStatement(query);
                rs = prestate.executeQuery();
                if(rs.next()){
                    studentName =rs.getString("student_name");
                    name.setText(studentName);
                    fatherName = rs.getString("std_father_name");
                    father_name.setText(fatherName);
                    phone = rs.getString("cell");
                    address.setText(rs.getString("address"));
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
                    prestate = databaseconnection.MySQLConnection.conn.prepareStatement(classQuery);
                    rs = prestate.executeQuery();
                    if(rs.next()) {
                        current_class.setText(rs.getString("class_name"));
                        classId = rs.getInt("class_id");
                        String myquery = "select monthly_fee from fee where class_id ="+classId+"";
                        prestate = databaseconnection.MySQLConnection.conn.prepareStatement(myquery);
                        rs = prestate.executeQuery();
                        if(rs.next()) {
                            totalMonthlyFee = Integer.parseInt(rs.getString("monthly_fee"));
                            total_fee.setText(String.valueOf(totalMonthlyFee));
                        }
                        rs.close();
                        prestate.close();
                    }
                    else {
                        prestate = databaseconnection.MySQLConnection.conn.prepareStatement(selectClass);
                        rs = prestate.executeQuery();
                        if(rs.next()){
                            current_class.setText(rs.getString("class_name"));
                            classId = rs.getInt("class_id");
                            String myquery = "select monthly_fee from fee where class_id ="+classId+"";
                            prestate = databaseconnection.MySQLConnection.conn.prepareStatement(myquery);
                            rs = prestate.executeQuery();
                            if(rs.next()) {
                                totalMonthlyFee = Integer.parseInt(rs.getString("monthly_fee"));
                                total_fee.setText(String.valueOf(totalMonthlyFee));
                            }
                        }
                    }
                }
                else {
                    Alert alert = new Alert(AlertType.INFORMATION);
                    alert.setTitle("Record Not Found");
                    alert.setContentText("Sorry! Record Not found");
                    alert.show();
                }
                rs.close();
                prestate.close(); 
                MySQLConnection.conn.close();
            } catch (SQLException | FileNotFoundException ex ) {
                ExceptionHandling.showException(ex);
            } catch (IOException ex) {
                ExceptionHandling.showException(ex);
            } 
        }
    }
    public void saveFee() {
        new MySQLConnection();
        if(student_id.getText().isEmpty() ||fee.getText().isEmpty() || date.getText().isEmpty() 
                || total_fee.getText().isEmpty() || remaining_fee.getText().isEmpty()) {
            Alert alert = new Alert(AlertType.INFORMATION);
            alert.setTitle("Completely fill the form");
            alert.setContentText("All the text fields are mandatory. Fill all of them and click save button again.");
            alert.show();
        }
        else {
            try {
                int std_id = Integer.parseInt(student_id.getText());
                int paid_fee = Integer.parseInt(fee.getText());
                String query = "insert into fee_payment (student_id, date, fee_paid, month, year) "
                        + "values(?,?,?,?,?)";
                prestate = databaseconnection.MySQLConnection.conn.prepareStatement(query);
                prestate.setInt(1, std_id);
                prestate.setString(2, date.getText());
                prestate.setInt(3, paid_fee);
                prestate.setString(4, feeMonth);
                prestate.setInt(5, Calendar.getInstance().get(Calendar.YEAR));
                prestate.execute();
                //display the message record is successfully saved.
                Alert alert = new Alert(AlertType.INFORMATION);
                alert.setTitle("Your record has been saved");
                alert.setContentText("Your record is successfully saved and "+ sendMessage());
                alert.show();
                rs.close();
                prestate.close();
                MySQLConnection.conn.close();
            } catch (SQLException ex) {
                ExceptionHandling.showException(ex);
            }
        }
    }
    private String sendMessage(){    
        new MySQLConnection();
        try {
            try {
             prestate = MySQLConnection.conn.prepareStatement("select *from school");
             rs = prestate.executeQuery();
             if(rs.next()){
                 message = ""+studentName+" Son of "+fatherName+" have paid "+fee.getText()+" monthly fee from out of "+total_fee.getText()+""
                    + " total fee for the month of "+feeMonth+" on "+date.getText()+"\n"
                    + "Regards \n "+rs.getString("name")+"";
             }
            //retreiving the save setting
            prestate = databaseconnection.MySQLConnection.conn.prepareStatement("select *from setting");
            rs = prestate.executeQuery();
            if(rs.next()){
                username = rs.getString("username");
                ipaddress = rs.getString("ipaddress");
                port = rs.getString("port");
                password = rs.getString("password");
                checkBoxValue = rs.getString("checkbox");
            }
            prestate.close();
            rs.close();
            MySQLConnection.conn.close();
            } catch (SQLException ex) {
            ExceptionHandling.showException(ex);
            }
            if(checkBoxValue.equals("true")) {
                url = new URL(ipaddress+":"+port+ "/SendSMS?username="+username+"&password="+password+"&phone="+phone+"&message="+URLEncoder.encode(message, "UTF-8"));
                URLConnection connection = url.openConnection();
                BufferedReader bufferReader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                String inputline;
                while((inputline = bufferReader.readLine()) != null){
                    Status = inputline;
                }
                bufferReader.close();
            }
        } catch (MalformedURLException ex) {
            ExceptionHandling.showException(ex);
        } catch (UnsupportedEncodingException ex) {
            ExceptionHandling.showException(ex);
        } catch (IOException ex) {
            ExceptionHandling.showException(ex);
        }
        return Status;
    }
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        new MySQLConnection();
        //let call the constructor of the MySQLConnection in order to initialize the basic parameters
        //Picking date from computer and setting it to the date of admission.
        DateFormat dateformat=new SimpleDateFormat("yyyy-MM-dd");
        Date mydate=new Date();
        date.setText(dateformat.format(mydate));
        //Let restrict the search bar from taking the characters as input
        student_id.textProperty().addListener(new ChangeListener<String>() {
        @Override
        public void changed(ObservableValue<? extends String> observable, String oldValue, 
            String newValue) { //15303-7056230-7
            if (!newValue.matches("\\d{0,10}")) {
                student_id.setText(oldValue);
            }
            }
        });
        //Let automatically calculate the remaning fee 
        fee.textProperty().addListener(new ChangeListener<String>(){
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                if (!newValue.matches("\\d{0,10}")){
                    fee.setText(oldValue);
                }
                if(!(total_fee.getText().isEmpty() || fee.getText().isEmpty())){
                    int remainingFee = (totalMonthlyFee - Integer.parseInt(newValue));
                    remaining_fee.setText(String.valueOf(remainingFee));
                    if(remainingFee < 0){
                        fee.setText(oldValue);
                    }
                }
            }
        });
        //lets add items to the month
        month.getItems().addAll("January", "February", "March", "April",
                "May", "June", "July", "August", "September", "October", "November", "December");
        //this function take values from the combo box and assign it to the classofadmission variable
        month.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>(){
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                feeMonth = newValue; 
            }
        });
    }     
}
