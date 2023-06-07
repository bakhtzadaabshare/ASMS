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
import java.util.Calendar;
import java.util.ResourceBundle;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.print.PageLayout;
import javafx.print.PageOrientation;
import javafx.print.Paper;
import javafx.print.Printer;
import javafx.print.PrinterJob;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.transform.Scale;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author Bakht Zada
 */
public class UnpaidFeeController implements Initializable {
        @FXML
    private ComboBox month;

    @FXML
    private ComboBox year;

    @FXML
    private TableView<UnPiadTableClass> fee_table;

    @FXML
    private TableColumn<UnPiadTableClass, String> std_id_clm;

    @FXML
    private TableColumn<UnPiadTableClass, String> name_clm;

    @FXML
    private TableColumn<UnPiadTableClass, String> father_name_clm;

    @FXML
    private TableColumn<UnPiadTableClass, String> address_clm;

    @FXML
    private TableColumn<UnPiadTableClass, String> phone_number_clm;

    @FXML
    private TableColumn<UnPiadTableClass, String> paid_fee_clm;

    @FXML
    private TableColumn<UnPiadTableClass, String> total_fee_clm;

    @FXML
    private TableColumn<UnPiadTableClass, String> month_clm;
    
    @FXML
    private AnchorPane table_print;
    private int session=0;
    private String currentMonth =null;
    private PreparedStatement prestate;
    private ResultSet rs, rs2;
    ObservableList<UnPiadTableClass> tableData;
    public void loadPaidFee() {
        try {
            Stage stage = new Stage();
            //stage.setResizable(false);
            stage.initOwner(FXMLDocumentController.getPrimaryStage());
            stage.initOwner(FXMLDocumentController.getPrimaryStage());
            Parent root = FXMLLoader.load(getClass().getResource("UnpaidFee.fxml"));
            stage.getIcons().add(new Image(getClass().getResourceAsStream("Icons/brick.png")));
            Scene myScene = new Scene(root);
            stage.setScene(myScene);
            stage.show();
        } catch (IOException ex) {
            ExceptionHandling.showException(ex);
        }
    }
    private void showAlert() {
        Alert alert = new Alert(AlertType.WARNING);
        alert.setTitle("Select First");
        alert.setContentText("First select month and year then click on the button");
        alert.show();
    }
    public void unpaidFee() {
        try {
            new MySQLConnection();
            if(session == 0 || month == null ) {
                showAlert();
            }
            else {
                tableData = FXCollections.observableArrayList();
                String query = "select student.student_id, student.student_name,student.class_id, student.std_father_name, student.address, student.cell, fee_payment.fee_paid, fee_payment.month" +
                        " from student" +
                        " left join fee_payment on student.student_id = fee_payment.student_id" +
                        " where student.student_id not in (select student_id from fee_payment where month = '"+currentMonth+"' and year = "+session+")";
                prestate =databaseconnection.MySQLConnection.conn.prepareStatement(query);
                rs = prestate.executeQuery();

                while(rs.next()){
                    prestate = databaseconnection.MySQLConnection.conn.prepareStatement("select  fee.monthly_fee,  promotedclass.student_id from fee, promotedclass where  fee.class_id = promotedclass.class_id and promotedclass.student_id = "+rs.getInt("student_id")+"");
                    rs2 = prestate.executeQuery();
                    if(rs2.next()){
                        tableData.add(new UnPiadTableClass(rs.getString("student_id"),rs.getString("student_name"),rs.getString("std_father_name"),rs.getString("address"),rs.getString("cell"), rs.getString("fee_paid"),rs2.getString("monthly_fee"),rs.getString("month")));
                    }  
                }
                std_id_clm.setCellValueFactory(new PropertyValueFactory<> ("id"));
                name_clm.setCellValueFactory(new PropertyValueFactory<> ("name"));
                father_name_clm.setCellValueFactory(new PropertyValueFactory<> ("fatherName"));
                address_clm.setCellValueFactory(new PropertyValueFactory<> ("Address"));
                phone_number_clm.setCellValueFactory(new PropertyValueFactory<> ("mobile"));
                paid_fee_clm.setCellValueFactory(new PropertyValueFactory<> ("paidFee"));
                total_fee_clm.setCellValueFactory(new PropertyValueFactory<> ("totalFee"));
                month_clm.setCellValueFactory(new PropertyValueFactory<> ("month"));
                fee_table.setItems(tableData);
            }
            
        } catch (SQLException ex) {
            ExceptionHandling.showException(ex);
        } 
    }
    public void remainingFee() {
        try {
            new MySQLConnection();
            if(session == 0 || month == null ) {
                showAlert();
            }
            else {
                tableData = FXCollections.observableArrayList();
                String query = "select student.student_id, student.student_name, student.std_father_name, student.address, "
                        + "student.cell, fee.monthly_fee, fee_payment.fee_paid, fee_payment.month from student, fee, fee_payment, "
                        + "promotedclass where student.student_id = promotedclass.student_id and "
                        + "promotedclass.class_id = fee.class_id and fee_payment.student_id = student.student_id "
                        + "and fee_payment.fee_paid < fee.monthly_fee and month = '"+currentMonth+"' and year = "+session+"";
                prestate = databaseconnection.MySQLConnection.conn.prepareStatement(query);
                rs = prestate.executeQuery();
                while(rs.next()){
                    tableData.add(new UnPiadTableClass(rs.getString("student_id"),rs.getString("student_name"),rs.getString("std_father_name"),rs.getString("address"),rs.getString("cell"), rs.getString("fee_paid"),rs.getString("monthly_fee"),rs.getString("month")));
                }
                std_id_clm.setCellValueFactory(new PropertyValueFactory<> ("id"));
                name_clm.setCellValueFactory(new PropertyValueFactory<> ("name"));
                father_name_clm.setCellValueFactory(new PropertyValueFactory<> ("fatherName"));
                address_clm.setCellValueFactory(new PropertyValueFactory<> ("Address"));
                phone_number_clm.setCellValueFactory(new PropertyValueFactory<> ("mobile"));
                paid_fee_clm.setCellValueFactory(new PropertyValueFactory<> ("paidFee"));
                total_fee_clm.setCellValueFactory(new PropertyValueFactory<> ("totalFee"));
                month_clm.setCellValueFactory(new PropertyValueFactory<> ("month"));
                fee_table.setItems(tableData);
            }
        } catch (SQLException ex) {
            ExceptionHandling.showException(ex);
        }
    }
    public void printRecord() {
        Node node =  fee_table;
        Printer printer = Printer.getDefaultPrinter();
        PageLayout pageLayout
            = printer.createPageLayout(Paper.A4, PageOrientation.LANDSCAPE, Printer.MarginType.HARDWARE_MINIMUM);
        //PrinterAttributes attr = printer.getPrinterAttributes();
        PrinterJob job = PrinterJob.createPrinterJob();
        double scaleX
            = pageLayout.getPrintableWidth() / node.getBoundsInParent().getWidth();
        double scaleY
            = pageLayout.getPrintableHeight() / node.getBoundsInParent().getHeight();
        Scale scale = new Scale(scaleX, scaleY);
        node.getTransforms().add(scale);
        if (job != null && job.showPrintDialog(node.getScene().getWindow())) {
            boolean success = job.printPage(pageLayout, node);
            if (success) {
              job.endJob();
      }
    }
    node.getTransforms().remove(scale);
    }
    public void feePaid() {
        try {
            new MySQLConnection();
            if(session == 0 || month == null ) {
                showAlert();
            }
            else {
                tableData = FXCollections.observableArrayList();
                String query = "select student.student_id, student.student_name, student.std_father_name, "
                        + "student.address, student.cell, fee.monthly_fee, fee_payment.fee_paid, "
                        + "fee_payment.month  from student, fee, fee_payment, promotedclass  "
                        + "where student.student_id = promotedclass.student_id "
                        + "and promotedclass.class_id = fee.class_id and fee_payment.student_id = student.student_id "
                        + "and fee_payment.fee_paid = fee.monthly_fee and month = '"+currentMonth+"' and year = "+session+"";
                prestate = databaseconnection.MySQLConnection.conn.prepareStatement(query);
                rs = prestate.executeQuery();
                while(rs.next()){
                    tableData.add(new UnPiadTableClass(rs.getString("student_id"),rs.getString("student_name"),rs.getString("std_father_name"),rs.getString("address"),rs.getString("cell"), rs.getString("fee_paid"),rs.getString("monthly_fee"),rs.getString("month")));
                }
                std_id_clm.setCellValueFactory(new PropertyValueFactory<> ("id"));
                name_clm.setCellValueFactory(new PropertyValueFactory<> ("name"));
                father_name_clm.setCellValueFactory(new PropertyValueFactory<> ("fatherName"));
                address_clm.setCellValueFactory(new PropertyValueFactory<> ("Address"));
                phone_number_clm.setCellValueFactory(new PropertyValueFactory<> ("mobile"));
                paid_fee_clm.setCellValueFactory(new PropertyValueFactory<> ("paidFee"));
                total_fee_clm.setCellValueFactory(new PropertyValueFactory<> ("totalFee"));
                month_clm.setCellValueFactory(new PropertyValueFactory<> ("month"));
                fee_table.setItems(tableData);
            }
        } catch (SQLException ex) {
            ExceptionHandling.showException(ex);
        }
    }
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        int currentYears = Calendar.getInstance().get(Calendar.YEAR);
        int begginingYear = currentYears-5;
        for(int years = currentYears; years >= begginingYear ; years--) {
           year.getItems().add(years); 
        }
        year.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Integer> () {
            @Override
            public void changed(ObservableValue<? extends Integer> observable, Integer oldValue, Integer newValue) {
                session = newValue;
            }
        });
        month.getItems().addAll("January", "February", "March", "April",
                "May", "June", "July", "August", "September", "October", "November", "December");
        //this function take values from the combo box and assign it to the classofadmission variable
        month.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>(){
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                currentMonth = newValue; 
            }
        });
    }    
    
}
