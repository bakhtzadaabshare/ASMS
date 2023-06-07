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
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.text.Text;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author Bakht Zada
 */
public class StudentInfoController implements Initializable {
    @FXML
    public TableView<StudentInfoTable> std_table;

    @FXML
    private TableColumn<StudentInfoTable, String> id;

    @FXML
    private TableColumn<StudentInfoTable, String> name;

    @FXML
    private TableColumn<StudentInfoTable, String> father_name;

    @FXML
    private TableColumn<StudentInfoTable, String> father_nic;

    @FXML
    private TableColumn<StudentInfoTable, String> address;

    @FXML
    private TableColumn<StudentInfoTable, String> dob;

    @FXML
    private TableColumn<StudentInfoTable, String> mobile;

    @FXML
    private TableColumn<StudentInfoTable, String> class_of_admission;

    @FXML
    private TableColumn<StudentInfoTable, String> date_of_admission;

    @FXML
    private TableColumn<StudentInfoTable, String> cast;
    
    @FXML
    private TableColumn<StudentInfoTable,String> class_of_study;
    
    @FXML
    private TextField search_student;
    
    @FXML
    private Text total_students;
    
    private PreparedStatement prestate;
    private ResultSet rs;
    private String className;
    private String currentClass;
    private DeleteLoginController deleteLogin;
    public void loadStudentInfo() {
        try {
            Stage stage = new Stage();
            //stage.setResizable(false);
            stage.initOwner(FXMLDocumentController.getPrimaryStage());
            Parent root = FXMLLoader.load(getClass().getResource("StudentInfo.fxml"));
            stage.getIcons().add(new Image(getClass().getResourceAsStream("Icons/brick.png")));
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        } catch (IOException ex) {
            ExceptionHandling.showException(ex);
        }
    }
    private void loadTableData() {
       // try {
            //new MySQLConnection();
            ObservableList<StudentInfoTable> tabledata = FXCollections.observableArrayList();
            try {
                String query = "Select * from student";
                prestate = databaseconnection.MySQLConnection.conn.prepareStatement(query);
                rs = prestate.executeQuery();
                ResultSet rs2;
                ResultSet rs3;
                while(rs.next()){
                    String classQuery = "select class_name from class where class_id ="+rs.getInt("class_id")+"";
                    PreparedStatement prestate2 = databaseconnection.MySQLConnection.conn.prepareStatement(classQuery);
                    rs2 = prestate2.executeQuery();
                    if(rs2.next()){
                        className = rs2.getString("class_name");
                    }
                    String currentClassQuery= "select class_name from promotedclass, class"
                            + " where promotedclass.class_id = class.class_id and student_id ="+rs.getInt("student_id")+"";
                    PreparedStatement prestate3 = databaseconnection.MySQLConnection.conn.prepareStatement(currentClassQuery);
                    rs3 = prestate3.executeQuery();
                    if(rs3.next()){
                        currentClass = rs3.getString("class_name");
                    }
                    else {
                        currentClass = rs2.getString("class_name");
                    }
                    tabledata.add(new StudentInfoTable(rs.getString("student_id"), rs.getString("student_name")
                            ,rs.getString("std_father_name"), rs.getString("std_father_nic")
                            ,rs.getString("address"), rs.getString("dob"), rs.getString("cell")
                            ,className, rs.getString("date_of_admission"), rs.getString("caste"), currentClass));
                    rs2.close();
                    prestate2.close();
                    rs3.close();
                    prestate3.close();
                }
                rs.close();
                prestate.close();
            } catch (SQLException ex) {
                ExceptionHandling.showException(ex);
            }
            id.setCellValueFactory(new PropertyValueFactory<> ("id"));
            name.setCellValueFactory(new PropertyValueFactory<> ("name"));
            father_name.setCellValueFactory(new PropertyValueFactory<> ("fatherName"));
            father_nic.setCellValueFactory(new PropertyValueFactory<> ("fatherNic"));
            address.setCellValueFactory(new PropertyValueFactory<> ("Address"));
            dob.setCellValueFactory(new PropertyValueFactory<> ("dob"));
            mobile.setCellValueFactory(new PropertyValueFactory<> ("mobile"));
            class_of_admission.setCellValueFactory(new PropertyValueFactory<> ("classOfAdmissin"));
            date_of_admission.setCellValueFactory(new PropertyValueFactory<> ("dateOfAdmission"));
            cast.setCellValueFactory(new PropertyValueFactory<> ("caste"));
            class_of_study.setCellValueFactory(new PropertyValueFactory<> ("currentClass"));
            std_table.setItems(tabledata);
            //let set filter on the student name and father name
            FilteredList<StudentInfoTable> filterStudent = new FilteredList<> (tabledata, p -> true);
            search_student.textProperty().addListener((observable, oldValue, newValue) -> {
                filterStudent.setPredicate(student -> {
                    // If filter text is empty, display all persons.
                    if (newValue == null || newValue.isEmpty()) {
                        return true;
                    }
                    // Compare the name of the every student with filter text.
                    String lowerCaseFilter = newValue.toLowerCase();
                    if (student.getName().toLowerCase().contains(lowerCaseFilter)) {
                        return true; // Filter matches student name.
                    }
                    return false; // Does not match.
                });
            });
            // 3. Wrap the FilteredList in a SortedList.
            SortedList<StudentInfoTable> sortedData = new SortedList<>(filterStudent);
            // 4. Bind the SortedList comparator to the TableView comparator.
            sortedData.comparatorProperty().bind(std_table.comparatorProperty());
            // 5. Add sorted (and filtered) data to the table.
            std_table.setItems(sortedData);
            //getting total number of student from table and display
            int totalStudent = std_table.getItems().size();
            total_students.setText(String.valueOf(totalStudent));
           // MySQLConnection.conn.close();
            
       // } catch (SQLException ex) {
          //  ExceptionHandling.showException(ex);
       // }    
    }
    public void deleteSelectedRow() {
        try {
            deleteLogin = new DeleteLoginController();
            new MySQLConnection();
            if(std_table.getSelectionModel().getSelectedItem() == null){
                Alert message = new Alert(AlertType.INFORMATION);
                message.setTitle("Select the row first");
                message.setContentText("Non of the row is selected. Please select the row first and then hit the delete button");
                message.show();
            }
            else {
                //getting the select item
                StudentInfoTable selectedStudent = std_table.getSelectionModel().getSelectedItem();
                String deleteQuery = "delete from student where student_id ="+selectedStudent.getId()+"";
                //Confirm the user to delete the record
                Alert confirm = new Alert(AlertType.CONFIRMATION);
                confirm.setTitle("Alert! The selected record will be deleted");
                confirm.setContentText("Are you sure to delete "+selectedStudent.getName()+" from table?");
                Optional<ButtonType> result = confirm.showAndWait();
                if(result.isPresent() && result.get() == ButtonType.OK) {
                    DeleteLoginController.deleteQuery = deleteQuery;
                    deleteLogin.loaDeleteLogin();
                }
            }
            MySQLConnection.conn.close();
        } catch (SQLException ex) { 
            ExceptionHandling.showException(ex);
        } catch (IOException ex) {
            Logger.getLogger(StudentInfoController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    public void deleteAllRecords() throws IOException {
        try {
            new MySQLConnection();
            deleteLogin = new DeleteLoginController();
            String deleteQuery = "delete from student";
            //Confirm the user to delete the record
            Alert confirm = new Alert(AlertType.CONFIRMATION);
            confirm.setTitle("Alert! All the records will be deleted");
            confirm.setContentText("Are you sure to delete all record from the table?");
            Optional<ButtonType> result = confirm.showAndWait();
            if(result.isPresent() && result.get() == ButtonType.OK) {
                DeleteLoginController.deleteQuery = deleteQuery;
                deleteLogin.loaDeleteLogin();
            }
            MySQLConnection.conn.close();
        } catch (SQLException ex) {
            ExceptionHandling.showException(ex);
        }
    }
    public void editRecord() {
        if(std_table.getSelectionModel().getSelectedItem() == null) {
            Alert alert = new Alert(AlertType.INFORMATION);
            alert.setTitle("Select student");
            alert.setContentText("Non of the record is selected. Please select the record first and then click on edit button");
            alert.show();
        }
        else {
            StudentInfoTable selectedStudent = std_table.getSelectionModel().getSelectedItem();
            EditStudentInfoController.id = Integer.parseInt(selectedStudent.getId());
            EditStudentInfoController student = new EditStudentInfoController();
            student.openEditeStuentInfo();
        }
    }
    public void refreshAction(){
        new MySQLConnection();
        loadTableData();
    }
    @Override
    public void initialize(URL url, ResourceBundle rb) {
       new MySQLConnection();
       loadTableData();

    }    
    
}
