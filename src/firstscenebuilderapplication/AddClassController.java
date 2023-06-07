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
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
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
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author Bakht Zada
 */
public class AddClassController implements Initializable {
        @FXML
    private TextField classid;

    @FXML
    private TextField classname;

    @FXML
    private TableView<ModelTable> existed_classes;

    @FXML
    private TableColumn<ModelTable, String> class_id;

    @FXML
    private TableColumn<ModelTable, String> class_name;
    
    private PreparedStatement prestate = null;
    DeleteLoginController deletelogin = new DeleteLoginController();
    //LoginFormController.a;
    //this class will open addClass window and add fxml
    public void openAddClass() throws IOException {
        Stage stage = new Stage();
        stage.setResizable(false);
        stage.initOwner(FXMLDocumentController.getPrimaryStage());
        Parent root = FXMLLoader.load(getClass().getResource("AddClass.fxml"));
        stage.getIcons().add(new Image(getClass().getResourceAsStream("Icons/brick.png")));
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }
    //this fucntion take data from the text boxes and pass it to mysql connection class
    public void insertClass() {
        new MySQLConnection();
        if(classid.getText().isEmpty() || classname.getText().isEmpty()) {
            Alert message = new Alert(AlertType.WARNING);
            message.setTitle("Form is not completed!");
            message.setContentText("The class id and class name cann't be empty. Please fill all the fields");
            message.show();
        }
        else {
            try {
                int classId = Integer.parseInt(classid.getText());
                String query = "insert into class(class_id, class_name) values(?,?)";
                prestate = databaseconnection.MySQLConnection.conn.prepareStatement(query);
                prestate.setInt(1, classId);
                prestate.setString(2, classname.getText());
                prestate.execute();
                //Call the tableViewForClass() function in order to reload the data 
                prestate.close();
                if(!MySQLConnection.conn.isClosed()) {
                    MySQLConnection.conn.close();
                }
                tableViewForClass();
            } catch (SQLException ex) {
                ExceptionHandling.showException(ex);
            }
        }
    }
    public void deleteAll() throws Exception {
        Alert confirm = new Alert(AlertType.CONFIRMATION);
        confirm.setTitle("Are you sure?");
        confirm.setContentText("All of your data will be erased. Are you sure about it?");
        Optional<ButtonType> result = confirm.showAndWait();
        if(result.isPresent() && result.get() == ButtonType.OK) {
            String query = "delete from class";
            DeleteLoginController.deleteQuery = query;
            deletelogin.loaDeleteLogin();
        }
    }
    public void refresh() {
        tableViewForClass();
    }
    public void insertNewRecord() {
        classname.clear();
        classid.clear();
    }
    public void tableViewForClass() {
         //object of the MySQLConnection in order to initialize conn object;
        new MySQLConnection();
        ObservableList<ModelTable> tabledata = FXCollections.observableArrayList();
        try {
            String query = "select * from class";
            prestate = databaseconnection.MySQLConnection.conn.prepareStatement(query);
            ResultSet rs = prestate.executeQuery();
            while(rs.next()) {
                tabledata.add(new ModelTable(rs.getString("class_id"), rs.getString("class_name")));
            }
            class_id.setCellValueFactory(new PropertyValueFactory<> ("id"));
            class_name.setCellValueFactory(new PropertyValueFactory<> ("c_name"));
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
    //creating observable list for holding data for the class tableview
    @Override
    public void initialize(URL url, ResourceBundle rb) {

        //Call the function in order to load data into table on the while displayed for the first time
        tableViewForClass();
    } 
}
