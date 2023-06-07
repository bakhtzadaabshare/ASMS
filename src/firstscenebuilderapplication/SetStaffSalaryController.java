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
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author Bakht Zada
 */
public class SetStaffSalaryController implements Initializable {
    @FXML
    private TextField teacher_id;

    @FXML
    private TextField salary;

    @FXML
    private Button save_btn;

    @FXML
    private Button insert_btn;

    @FXML
    private Button delete_btn;

    @FXML
    private TableView<SetStaffSalaryTable> fee_details;

    @FXML
    private TableColumn<SetStaffSalaryTable, String> id_col;

    @FXML
    private TableColumn<SetStaffSalaryTable, String> name_col;

    @FXML
    private TableColumn<SetStaffSalaryTable, String> father_name_col;

    @FXML
    private TableColumn<SetStaffSalaryTable, String> designation_col;

    @FXML
    private TableColumn<SetStaffSalaryTable, String> salary_col;
    
    ObservableList<SetStaffSalaryTable> tabledata;
    private PreparedStatement prestate;
    private ResultSet rs;
    private String teacherId;
    String mysalary;
    
    
    public void loadSetSalaryForm() {
        try {
            Stage stage = new Stage();
            stage.setResizable(false);
            stage.initOwner(FXMLDocumentController.getPrimaryStage());
            Parent root = FXMLLoader.load(getClass().getResource("setStaffSalary.fxml"));
            stage.getIcons().add(new Image(getClass().getResourceAsStream("Icons/brick.png")));
            Scene myScene = new Scene(root);
            stage.setScene(myScene);
            stage.show();
        } catch (IOException ex) {
            ExceptionHandling.showException(ex);
        }
    }
    public void updateSalary() {
        try {
            new MySQLConnection();
            if(teacher_id.getText().isEmpty() || salary.getText().isEmpty())
            { 
                Alert alert = new Alert(AlertType.INFORMATION);
                alert.setTitle("Empty textfield found");
                alert.setContentText("Teacher id and salary can't be left empty. Please fill it first then click on update button");
                alert.show();
            }
            else {
                try {
                    String updateQuery = "update teacher_salary set salary = ? where teacher_id ="+Integer.parseInt(teacher_id.getText())+"";
                    prestate = databaseconnection.MySQLConnection.conn.prepareStatement(updateQuery);
                    prestate.setInt(1, Integer.parseInt(salary.getText()));
                    prestate.executeUpdate();
                    Alert alert = new Alert(AlertType.INFORMATION);
                    alert.setTitle("Update Succeded");
                    alert.setContentText("Salary is successfully updated for id "+teacher_id.getText()+". You can check it in the table");
                    alert.show();
                    loadTableData();
                    prestate.close();
                } catch (SQLException ex) {
                    ExceptionHandling.showException(ex);
                }
            }
            MySQLConnection.conn.close();
        } catch (SQLException ex) {
            ExceptionHandling.showException(ex);
        }
    }
    public void insertSalary() {
        try {
            new MySQLConnection();
            if(teacher_id.getText().isEmpty() || salary.getText().isEmpty())
            {
                Alert alert = new Alert(AlertType.INFORMATION);
                alert.setTitle("Empty textfield found");
                alert.setContentText("Teacher id and salary can't be left empty. Please fill it first then click on save button");
                alert.show();
            }
            else {
                try {
                    String insertQuery = "Insert into teacher_salary(teacher_id, salary) values (?,?)";
                    prestate = databaseconnection.MySQLConnection.conn.prepareStatement(insertQuery);
                    prestate.setInt(1, Integer.parseInt(teacher_id.getText()));
                    prestate.setInt(2, Integer.parseInt(salary.getText()));
                    prestate.execute();
                    prestate.close();
                    loadTableData();
                } catch (SQLException ex) {
                    Alert alert  = new Alert(AlertType.ERROR);
                    alert.setTitle("Error Occurred");
                    alert.setContentText(ex.toString() +". Do you want to make changes in salary? if yes then click on update button instead of save");
                    alert.show();
                }
            }
            MySQLConnection.conn.close();
        } catch (SQLException ex) {
            ExceptionHandling.showException(ex);
        }
    }
    private void loadTableData() {
        try {
            new MySQLConnection();
            tabledata = FXCollections.observableArrayList();
            try {
                String queryFee = "select * from teacher";
                prestate = databaseconnection.MySQLConnection.conn.prepareStatement(queryFee);
                rs = prestate.executeQuery();
                while(rs.next()) {
                    teacherId = rs.getString("teacher_id");
                    String salaryQuery = "select salary from teacher_salary where teacher_id = "+teacherId+"";
                    PreparedStatement prestate2 = databaseconnection.MySQLConnection.conn.prepareStatement(salaryQuery);
                    ResultSet rs2 = prestate2.executeQuery();
                    if(rs2.next()){
                        mysalary = rs2.getString("salary");
                    }
                    
                    else {
                        mysalary = "";
                    }
                    rs2.close();
                    prestate2.close();
                    tabledata.add(new SetStaffSalaryTable(teacherId, rs.getString("teacher_name"), rs.getString("father_name"), rs.getString("designation")
                            ,mysalary));
                }
                rs.close();
                prestate.close();
            } catch (SQLException ex) {
                ExceptionHandling.showException(ex);
            }
            id_col.setCellValueFactory(new PropertyValueFactory<> ("id"));
            name_col.setCellValueFactory(new PropertyValueFactory<> ("name")); 
            father_name_col.setCellValueFactory(new PropertyValueFactory<> ("father_name"));
            designation_col.setCellValueFactory(new PropertyValueFactory<>("designation"));
            salary_col.setCellValueFactory(new PropertyValueFactory<>("salary"));
            fee_details.setItems(tabledata);
            FilteredList<SetStaffSalaryTable> filterStudent = new FilteredList<> (tabledata, p -> true);
            teacher_id.textProperty().addListener((observable, oldValue, newValue) -> {
                filterStudent.setPredicate(student -> {
                    // If filter text is empty, display all persons.
                    if (newValue == null || newValue.isEmpty()) {
                        return true;
                    }
                    // Compare the name of the every student with filter text.
                    if (student.getId().toLowerCase().contains(newValue)) {
                        return true; // Filter matches student name.
                    }
                    return false; // Does not match.
                });
            });
            // 3. Wrap the FilteredList in a SortedList.
            SortedList<SetStaffSalaryTable> sortedData = new SortedList<>(filterStudent);
            // 4. Bind the SortedList comparator to the TableView comparator.
            sortedData.comparatorProperty().bind(fee_details.comparatorProperty());
            // 5. Add sorted (and filtered) data to the table.
            fee_details.setItems(sortedData);
            MySQLConnection.conn.close();
        } catch (SQLException ex) {
            ExceptionHandling.showException(ex);
        }
    }
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        loadTableData();
        //prevent salary field to take stirng
        salary.textProperty().addListener(new ChangeListener<String>() {
        @Override
        public void changed(ObservableValue<? extends String> observable, String oldValue, 
            String newValue) { 
            if (!newValue.matches("\\d{0,50}")) {
                salary.setText(oldValue);
            }
            }
        });
        teacher_id.textProperty().addListener(new ChangeListener<String>() {
        @Override
        public void changed(ObservableValue<? extends String> observable, String oldValue, 
            String newValue) { 
            if (!newValue.matches("\\d{0,50}")) {
                teacher_id.setText(oldValue);
            }
            }
        });
    }      
}
