/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package firstscenebuilderapplication;

import databaseconnection.MySQLConnection;
import java.io.BufferedReader;
import java.io.File;
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
public class PayTeacherSalaryController implements Initializable {
    @FXML
    private TextField teacher_id;

    @FXML
    private Label name;

    @FXML
    private Label father_name;

    @FXML
    private Label nic;

    @FXML
    private Label address;

    @FXML
    private Label designation;
    
    @FXML
    private TextField date;
    
    @FXML
    private ComboBox month;


    @FXML
    private TextField salary;

    @FXML
    private TextField remaining_Salary;

    @FXML
    private TextField total_salary;

    @FXML
    private Circle teacher_image;
    
    
    private PreparedStatement prestate;
    private ResultSet rs;
    private String salaryMonth;
    private int teacherId;
    private String teacherName;
    private String totalSalary;
    private String phone;
    private String username;
    private String ipaddress;
    private String port;
    private String password;
    private String checkBoxValue;
    private URL url;
    private String Status;
    private String message;
    
    public void openPayTeacherSalary() {
        try {
            Stage stage = new Stage();
            stage.setResizable(false);
            stage.initOwner(FXMLDocumentController.getPrimaryStage());
            Parent root = FXMLLoader.load(getClass().getResource("PayTeacherSalary.fxml"));
            stage.getIcons().add(new Image(getClass().getResourceAsStream("Icons/brick.png")));
            Scene myScene = new Scene(root);
            stage.setScene(myScene);
            stage.show();
        } catch (IOException ex) {
            ExceptionHandling.showException(ex);
        }
    }
    public void searchTeacher(){
        new MySQLConnection();
        if(teacher_id.getText().isEmpty()){
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Enter Teacher Id");
            alert.setContentText("Enter Teacher Id then press search button!");
            alert.show();
        }
        else {
            teacherId = Integer.parseInt(teacher_id.getText());
            String query = "select * from teacher where teacher_id = "+teacherId+"";
            try {
                prestate = databaseconnection.MySQLConnection.conn.prepareStatement(query);
                rs = prestate.executeQuery();
                if(rs.next()){
                    teacherName = rs.getString("teacher_name");
                    name.setText(teacherName);
                    father_name.setText(rs.getString("father_name"));
                    address.setText(rs.getString("address"));
                    nic.setText(rs.getString("nic"));
                    designation.setText(rs.getString("designation"));  
                    phone =rs.getString("cell");
                    InputStream is = rs.getBinaryStream("picture");
                    prestate = databaseconnection.MySQLConnection.conn.prepareStatement("select salary from teacher_salary where teacher_id = "+teacherId+"");
                    rs = prestate.executeQuery();
                    if(rs.next()){
                        totalSalary = rs.getString("salary"); 
                        total_salary.setText(totalSalary);
                    }
                    else {
                        total_salary.clear();
                    }
                    OutputStream os = new FileOutputStream( new File("photo.jpg"));
                    byte[] content = new byte[1024];
                    int size = 0;
                    while((size = is.read(content)) != -1){
                        os.write(content, 0, size);
                    }
                    os.close();
                    is.close();
                    Image image = new Image("file:photo.jpg");  
                    teacher_image.setFill(new ImagePattern(image));
                }
                else {
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Record Not Found");
                    alert.setContentText("Sorry! Record Not found");
                    alert.show();
                }
                rs.close();
                prestate.close();
                MySQLConnection.conn.close();
            } catch (SQLException ex) {
                ExceptionHandling.showException(ex);
            } 
             catch (IOException ex) {
                ExceptionHandling.showException(ex);
            } 
        }
    }
    public void saveTeacherSalary() {
        new MySQLConnection();
        if(teacher_id.getText().isEmpty()|| date.getText().isEmpty() 
                || salary.getText().isEmpty() || remaining_Salary.getText().isEmpty()
                || total_salary.getText().isEmpty()|| month.getItems().isEmpty()) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Fill the form completely");
        alert.setContentText("All the textfields are mandatory. Please fill all of them then click on th save button");
        alert.show();
        }
        else {
            try {
                String query = "INSERT INTO pay_salary(teacher_id, date, salary, month, year) values(?,?,?,?,?) ";
                prestate = databaseconnection.MySQLConnection.conn.prepareStatement(query);
                prestate.setInt(1, teacherId);
                prestate.setString(2, date.getText());
                prestate.setInt(3, Integer.parseInt(salary.getText()));
                prestate.setString(4, salaryMonth);
                prestate.setInt(5, Calendar.getInstance().get(Calendar.YEAR));
                prestate.execute();
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Record is saved successfully");
                alert.setContentText("Your record is succefully saved and "+ sendMessage());
                alert.show();
                rs.close();
                prestate.close();
                MySQLConnection.conn.close();
            } catch (SQLException ex) {
                ExceptionHandling.showException(ex);
            } 
        } 
    }
    private String sendMessage() throws SQLException{ 
        new MySQLConnection();
        try {
            
            prestate = MySQLConnection.conn.prepareStatement("select *from school");
            rs = prestate.executeQuery();
            if(rs.next()){
                 message = "Dear teacher "+teacherName+""
                    + " you have recieved "+salary.getText()+" salary from out of "+totalSalary+""
                    + " total salary for the month of "+salaryMonth+"\n "
                    + "Regards \n "+rs.getString("name")+"";
                
            }
            
            try {
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
        //Picking date from computer and setting it to the date of admission.
        DateFormat dateformat=new SimpleDateFormat("yyyy-MM-dd");
        Date mydate=new Date();
        date.setText(dateformat.format(mydate));
        //Let restrict the search bar from taking the characters as input
        teacher_id.textProperty().addListener(new ChangeListener<String>() {
        @Override
        public void changed(ObservableValue<? extends String> observable, String oldValue, 
            String newValue) { 
            if (!newValue.matches("\\d{0,10}")) {
                teacher_id.setText(oldValue);
            }
            }
        });
        total_salary.textProperty().addListener(new ChangeListener<String>() {
        @Override
        public void changed(ObservableValue<? extends String> observable, String oldValue, 
            String newValue) { 
            if (!newValue.matches("\\d{0,50}")) {
                total_salary.setText(oldValue);
            }
            }
        });
        //Let automatically calculate the remaning fee 
        salary.textProperty().addListener(new ChangeListener<String>(){
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                if (!newValue.matches("\\d{0,50}")){
                    salary.setText(oldValue);
                }
                if(!(total_salary.getText().isEmpty() || salary.getText().isEmpty())){
                    int totalSalary = Integer.parseInt(total_salary.getText());
                    int remainingFee = (totalSalary - Integer.parseInt(newValue));
                    remaining_Salary.setText(String.valueOf(remainingFee));
                    if(remainingFee < 0){
                        salary.setText(oldValue);
                    }
                }
            }
        });
        //lets add items to the month
        month.getItems().addAll("January", "February", "March", "April",
                "May", "June", "July", "Auguest", "September", "October", "November", "December");
        //this function take values from the combo box and assign it to the classofadmission variable
        month.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>(){
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                salaryMonth = newValue; 
            }
        });
       
    }    
    
}
