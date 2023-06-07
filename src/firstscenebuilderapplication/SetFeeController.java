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
import javafx.scene.control.Alert.AlertType;
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
 * @author Bakht Zada
 */
public class SetFeeController implements Initializable {
    //declaring some usefull variables
    @FXML
    private ComboBox class_id;

    @FXML
    private TextField monthly_fee;
    
    @FXML
    TableView fee_details;
    
    @FXML
    private TableColumn<ViewFeeTableModel, String> class_col;

    @FXML
    private TableColumn<ViewFeeTableModel, String> promotion_fee_col;

    @FXML
    private TableColumn<ViewFeeTableModel, String> monthly_fee_col;

    @FXML
    private TextField promotion_fee;
    PreparedStatement prestate ;
    ResultSet rs;
    int classFee;
    String classname;
    public void loadFeeForm() throws IOException {
        Stage stage = new Stage();
        stage.setResizable(false);
        stage.initOwner(FXMLDocumentController.getPrimaryStage());
        Parent root = FXMLLoader.load(getClass().getResource("SetFee.fxml"));
        stage.getIcons().add(new Image(getClass().getResourceAsStream("Icons/brick.png")));
        Scene myScene = new Scene(root);
        stage.setScene(myScene);
        stage.show();
    }
    public void clearTextBoxes() {
        promotion_fee.clear();
        monthly_fee.clear();
    }
    public void insertRecord() {    
        //new MySQLConnection();
        if(monthly_fee.getText().isEmpty() || promotion_fee.getText().isEmpty()){
            Alert alert = new Alert(AlertType.INFORMATION);
            alert.setTitle("Complete All the Fields");
            alert.setContentText("All the text fields are mandatory to be filled. Please fill the form and try again!");
            alert.show();
        }
        else 
        {
            try {
                String selectQuery ="select monthly_fee, promotion_fee from fee where class_id = "+classFee+"";
                String insertQuery = "insert into fee(class_id, monthly_fee, promotion_fee) values(?,?,?)";
                String updateQuery = "update fee set monthly_fee = ? , promotion_fee = ? where class_id = "+classFee+"";
                prestate = databaseconnection.MySQLConnection.conn.prepareStatement(selectQuery);
                rs = prestate.executeQuery();
                if(rs.next()) {
                    prestate = databaseconnection.MySQLConnection.conn.prepareStatement(updateQuery);
                    prestate.setInt(1, Integer.parseInt(monthly_fee.getText()));
                    prestate.setInt(2, Integer.parseInt(promotion_fee.getText()));
                    prestate.executeUpdate();
                }
                else {
                    prestate = databaseconnection.MySQLConnection.conn.prepareStatement(insertQuery);
                    prestate.setInt(1, classFee);
                    prestate.setInt(2, Integer.parseInt(monthly_fee.getText()));
                    prestate.setInt(3, Integer.parseInt(promotion_fee.getText()));
                    prestate.execute();
                }
                rs.close();
                prestate.close();
                viewFee();
               // MySQLConnection.conn.close();
            } catch (SQLException ex) {
               ExceptionHandling.showException(ex);
            }
        }
    }
    public void deleteAllRecords(){
        //new MySQLConnection();
        Alert confirm = new Alert(AlertType.CONFIRMATION);
        confirm.setTitle("Are you sur?");
        confirm.setContentText("All of your data will be erased. Are you sure about it?");
        Optional<ButtonType> result = confirm.showAndWait();
        if(result.isPresent() && result.get() == ButtonType.OK) {
            try {
            String query = "delete from fee";
            prestate = databaseconnection.MySQLConnection.conn.prepareStatement(query);
            prestate.execute();
            prestate.close();
            Alert message = new Alert(AlertType.INFORMATION);
            message.setTitle("Your record is deleted");
            message.setContentText("All the data from the class table has been deleted");
            message.show();
            // Call the tableViewForClass() function in order to reload the data
            viewFee();
            //MySQLConnection.conn.close();
        } catch (SQLException ex) {
            ExceptionHandling.showException(ex);
        }
        }
    }
    private void viewFee() {
        new MySQLConnection();
                ObservableList<ViewFeeTableModel> tabledata = FXCollections.observableArrayList();
        try {
            String queryFee = "select * from fee";
            prestate = databaseconnection.MySQLConnection.conn.prepareStatement(queryFee);
            rs = prestate.executeQuery();
            while(rs.next()) {
                prestate = databaseconnection.MySQLConnection.conn.prepareStatement("select class_name from class"
                        + " where class_id ="+rs.getInt("class_id")+"");
                ResultSet cs = prestate.executeQuery();
                if(cs.next()){
                    classname = cs.getString("class_name");
                }
              tabledata.add(new ViewFeeTableModel(classname, rs.getString("promotion_fee"), rs.getString("monthly_fee")));
            }
            rs.close();
            prestate.close();
            //MySQLConnection.conn.close();
        } catch (SQLException ex) {
            ExceptionHandling.showException(ex);
        }
        class_col.setCellValueFactory(new PropertyValueFactory<> ("class_name"));
        promotion_fee_col.setCellValueFactory(new PropertyValueFactory<> ("promotionFee"));
        monthly_fee_col.setCellValueFactory(new PropertyValueFactory<> ("MonthlyFee"));
        fee_details.setItems(tabledata);
    }
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        viewFee();
        try {
            new MySQLConnection();
            //let take datafrom database and assign value to combobx
            String query = "select class_name from class";
            prestate = databaseconnection.MySQLConnection.conn.prepareStatement(query);
            rs = prestate.executeQuery();
            while(rs.next()) {
                class_id.getItems().add(rs.getString("class_name"));
            }
            rs.close();
            prestate.close();
        } catch (SQLException ex) {
            ExceptionHandling.showException(ex);
        }
        //this function take values from the combo box and assign it to the classofadmission variable
        class_id.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>(){
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                try {
                    String qurey = "select class_id from class where class_name = '"+newValue+"'";
                    prestate = databaseconnection.MySQLConnection.conn.prepareStatement(qurey);
                    rs = prestate.executeQuery();
                    if(rs.next()){
                        classFee = rs.getInt("class_id");
                    }
                    rs.close();
                    prestate.close();;
                } catch (SQLException ex) {
                    ExceptionHandling.showException(ex);
                }   
            }
        });
        //this function prevent monthly fee and promotion fee feild from taking other value rather than digits
        monthly_fee.textProperty().addListener(new ChangeListener<String>() {
        @Override
        public void changed(ObservableValue<? extends String> observable, String oldValue, 
            String newValue) {
            if (!newValue.matches("\\d{0,13}")) {
                monthly_fee.setText(oldValue);
            }
            }
        });
        promotion_fee.textProperty().addListener(new ChangeListener<String>() {
        @Override
        public void changed(ObservableValue<? extends String> observable, String oldValue, 
            String newValue) {
            if (!newValue.matches("\\d{0,13}")) {
                promotion_fee.setText(oldValue);
            }
            }
        });
    }    
}
