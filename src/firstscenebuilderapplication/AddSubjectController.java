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
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.stage.Stage;


/**
 * FXML Controller class
 *
 * @author DIBB E6430
 */
public class AddSubjectController implements Initializable {
       @FXML
    private TextField subjectId;

    @FXML
    private TextField subjectName;
    
    @FXML
    private TextField total_marks;
    
    @FXML
    private ComboBox selectSubject;

    @FXML
    private ComboBox selectClass;
    
    @FXML
    private TableView<AssignSubjectModelClass> assignSubject;

    @FXML
    private TableColumn<AssignSubjectModelClass, String> class_clm;

    @FXML
    private TableColumn<AssignSubjectModelClass, String> subject_clm;

    @FXML
    private TableView<AddSubjectModelClass> existed_classes;

    @FXML
    private TableColumn<AddSubjectModelClass, String> subject_id;
    
    @FXML
    private TableColumn<AddSubjectModelClass, String> total_marks_clm;

    @FXML
    private TableColumn<AddSubjectModelClass, String> subject_name;
    private PreparedStatement prestate;
    private ResultSet rs;
    private DeleteLoginController deleteLogin;
    private int subjectID =0;
    private int classID =0;
    public void openAddSubject() throws IOException {
        Stage stage = new Stage();
        stage.setResizable(false);
        stage.initOwner(FXMLDocumentController.getPrimaryStage());
        Parent root = FXMLLoader.load(getClass().getResource("addSubject.fxml"));
        stage.getIcons().add(new Image(getClass().getResourceAsStream("Icons/brick.png")));
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }
    private void tableViewForSubject() {
         //object of the MySQLConnection in order to initialize conn object;
        new MySQLConnection();
        ObservableList<AddSubjectModelClass> tabledata = FXCollections.observableArrayList();
        try {
            String query = "select * from subject";
            prestate = databaseconnection.MySQLConnection.conn.prepareStatement(query);
            ResultSet rs = prestate.executeQuery();
            while(rs.next()) {
                tabledata.add(new AddSubjectModelClass(rs.getString("subject_id"), rs.getString("subject_name"), rs.getString("subject_marks")));
            }
            subject_id.setCellValueFactory(new PropertyValueFactory<> ("subject_id"));
            subject_name.setCellValueFactory(new PropertyValueFactory<> ("subject_name"));
            total_marks_clm.setCellValueFactory(new PropertyValueFactory<> ("marks"));
            existed_classes.setItems(tabledata);
            rs.close();
            prestate.close();
            if(!MySQLConnection.conn.isClosed()) {
            MySQLConnection.conn.close();
            }
        } catch (SQLException ex) {
            ExceptionHandling.showException(ex);
        }

    }
    private void AssignViewForSubject() {
         //object of the MySQLConnection in order to initialize conn object;
        new MySQLConnection();
        ObservableList<AssignSubjectModelClass> tabledata = FXCollections.observableArrayList();
        try {
            String query = "select class.class_name, subject.subject_name from class, assignsubject, subject where class.class_id = assignsubject.class_id and subject.subject_id = assignsubject.subject_id";
            prestate = databaseconnection.MySQLConnection.conn.prepareStatement(query);
            ResultSet rs = prestate.executeQuery();
            while(rs.next()) {
                tabledata.add(new AssignSubjectModelClass(rs.getString("class.class_name"), rs.getString("subject.subject_name")));
            }
            class_clm.setCellValueFactory(new PropertyValueFactory<> ("class_name"));
            subject_clm.setCellValueFactory(new PropertyValueFactory<> ("subject"));
            assignSubject.setItems(tabledata);
            rs.close();
            prestate.close();
            if(!MySQLConnection.conn.isClosed()) {
            MySQLConnection.conn.close();
            }
        } catch (SQLException ex) {
            ExceptionHandling.showException(ex);
        }

    }
    public void insertSubject() {
        new MySQLConnection();
        if(subjectId.getText().isEmpty() || subjectName.getText().isEmpty()|| total_marks.getText().isEmpty()) {
            Alert message = new Alert(Alert.AlertType.WARNING);
            message.setTitle("Form is not completed!");
            message.setContentText("The Subject id and name cann't be empty. Please fill all the fields");
            message.show();
        }
        else {
            try {
                int classId = Integer.parseInt(subjectId.getText());
                String query = "insert into subject(subject_id, subject_name, subject_marks) values(?,?,?)";
                prestate = databaseconnection.MySQLConnection.conn.prepareStatement(query);
                prestate.setInt(1, classId);
                prestate.setString(2, subjectName.getText());
                prestate.setString(3,total_marks.getText());
                prestate.execute();
                //Call the tableViewForClass() function in order to reload the data 
                prestate.close();
                if(!MySQLConnection.conn.isClosed()) {
                    MySQLConnection.conn.close();
                }
                tableViewForSubject();
                AssignSubject();
                subjectId.clear();
                subjectName.clear();
                total_marks.clear();
            } catch (SQLException ex) {
                ExceptionHandling.showException(ex);
            }
        }
    }
    public void deleteAll() throws Exception {
        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
        confirm.setTitle("Are you sure?");
        confirm.setContentText("All of your data will be erased. Are you sure about it?");
        Optional<ButtonType> result = confirm.showAndWait();
        if(result.isPresent() && result.get() == ButtonType.OK) {
            deleteLogin = new DeleteLoginController();
            String query = "delete from subject";
            DeleteLoginController.deleteQuery = query;
            deleteLogin.loaDeleteLogin();
        }
    }
    public void refresh() {
        tableViewForSubject();
        AssignViewForSubject();
    }
    public void insertNewRecord() {
        subjectName.clear();
        subjectId.clear();
    }
    private void AssignSubject(){
        selectClass.getItems().clear();
        selectSubject.getItems().clear();
        //object of the MySQLConnection in order to initialize conn object;
        new MySQLConnection();
        try {
            //let take datafrom database and assign value to combobx
            String query = "select class_name from class";
            prestate = databaseconnection.MySQLConnection.conn.prepareStatement(query);
            rs = prestate.executeQuery();
            while(rs.next()) {
                selectClass.getItems().add(rs.getString("class_name"));
            }
            rs.close();
            prestate.close();
        } catch (SQLException ex) {
            ExceptionHandling.showException(ex);
        }
        try {
            //let take datafrom database and assign value to combobx
            String query = "select subject_name from subject";
            prestate = databaseconnection.MySQLConnection.conn.prepareStatement(query);
            rs = prestate.executeQuery();
            while(rs.next()) {
                selectSubject.getItems().add(rs.getString("subject_name"));
            }
            rs.close();
            prestate.close();
        } catch (SQLException ex) {
            ExceptionHandling.showException(ex);
        }
        
        
        
        selectClass.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>(){
        @Override
        public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
            new MySQLConnection();
            try {
                String qurey = "select class_id from class where class_name = '"+newValue+"'";
                prestate = databaseconnection.MySQLConnection.conn.prepareStatement(qurey);
                rs = prestate.executeQuery();
                if(rs.next()){
                    classID = rs.getInt("class_id");
                }
                rs.close();
                prestate.close();
                MySQLConnection.conn.close();
            } catch (SQLException ex) {
                ExceptionHandling.showException(ex);
            }
        }
    });
    selectSubject.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>(){
        @Override
        public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
            new MySQLConnection();
            try {
                String qurey = "select subject_id from subject where subject_name = '"+newValue+"'";
                prestate = databaseconnection.MySQLConnection.conn.prepareStatement(qurey);
                rs = prestate.executeQuery();
                if(rs.next()){
                    subjectID = rs.getInt("subject_id");
                }
                rs.close();
                prestate.close();
                MySQLConnection.conn.close();
            } catch (SQLException ex) {
                ExceptionHandling.showException(ex);
            }
        }
    });
    }
    public void assignSubjectToClass() {
        new MySQLConnection();
        if(subjectID == 0 || classID == 0) {
            Alert message = new Alert(Alert.AlertType.WARNING);
            message.setTitle("Form is not completed!");
            message.setContentText("Select class and subject first");
            message.show();
        }
        else {
            try {
                String query = "insert into assignsubject(class_id,subject_id) values(?,?)";
                prestate = databaseconnection.MySQLConnection.conn.prepareStatement(query);
                prestate.setInt(1, classID);
                prestate.setInt(2, subjectID);
                prestate.execute();
                //Call the tableViewForClass() function in order to reload the data 
                prestate.close();
                if(!MySQLConnection.conn.isClosed()) {
                    MySQLConnection.conn.close();
                }
                AssignViewForSubject();
            } catch (SQLException ex) {
                ExceptionHandling.showException(ex);
            }
        }
    }
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        tableViewForSubject();
        AssignSubject();
        AssignViewForSubject();
    }    
    
}
