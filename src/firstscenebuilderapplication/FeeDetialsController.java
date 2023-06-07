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
import javafx.beans.value.ObservableValue;
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
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;
import javafx.scene.transform.Scale;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author Bakht Zada
 */
public class FeeDetialsController implements Initializable {
    @FXML
    private AnchorPane fee_details_window;
    
    @FXML
    private Button search_btn;

    @FXML
    private Button save_btn;

    @FXML
    private Button print_btn;

    @FXML
    private TextField std_id;

    @FXML
    private TextField year;

    @FXML
    private Label name;

    @FXML
    private Label fatherName;

    @FXML
    private Label adress;

    @FXML
    private Label total_jan;

    @FXML
    private Label total_feb;

    @FXML
    private Label total_mar;

    @FXML
    private Label total_apr;

    @FXML
    private Label total_may;

    @FXML
    private Label total_jun;

    @FXML
    private Label total_jul;

    @FXML
    private Label total_aug;

    @FXML
    private Label total_sep;

    @FXML
    private Label total_oct;

    @FXML
    private Label total_nov;

    @FXML
    private Label total_dec;

    @FXML
    private TextField paid_jan;

    @FXML
    private TextField paid_feb;

    @FXML
    private TextField paid_mar;

    @FXML
    private TextField paid_apr;

    @FXML
    private TextField paid_may;

    @FXML
    private TextField paid_jun;

    @FXML
    private TextField paid_jul;

    @FXML
    private TextField paid_aug;

    @FXML
    private TextField paid_sep;

    @FXML
    private TextField paid_oct;

    @FXML
    private TextField paid_nov;

    @FXML
    private TextField paid_dec;

    @FXML
    private Label remain_jan;

    @FXML
    private Label remain_feb;

    @FXML
    private Label remain_mar;

    @FXML
    private Label remain_apr;

    @FXML
    private Label remain_may;

    @FXML
    private Label remain_jun;

    @FXML
    private Label remain_jul;

    @FXML
    private Label remain_aug;

    @FXML
    private Label remain_sep;

    @FXML
    private Label remain_oct;

    @FXML
    private Label remain_nov;

    @FXML
    private Label remain_dec;

    @FXML
    private Text tota_paid_fee;

    @FXML
    private Text total_remain_fee;

    @FXML
    private Text total_fee;

    @FXML
    private Label total_promotion;

    @FXML
    private TextField paid_promotion;

    @FXML
    private Label remaining_promotion;
    
    @FXML
    private Label date_jan;

    @FXML
    private Label date_feb;

    @FXML
    private Label date_mar;

    @FXML
    private Label date_apr;

    @FXML
    private Label date_may;

    @FXML
    private Label date_jun;

    @FXML
    private Label date_jul;

    @FXML
    private Label date_aug;

    @FXML
    private Label date_sep;

    @FXML
    private Label date_oct;

    @FXML
    private Label date_nov;

    @FXML
    private Label date_dec;
    
    @FXML
    private Label promotion_date;
    
    @FXML
    private AnchorPane sign_area;
    
    private int studentId;
    private int session;
    private int classId;
    private int totalFee;
    private int totalPaidFee;
    private int sumOfAllFee;
    private int thisMonth;
    private PreparedStatement prestate;
    private ResultSet rs;
    private final String month[]; 
    private int totalMonthFee;
    private int totalProFee;
    private int paidPromotion;
    public FeeDetialsController() {
        this.month = new String[]{"January","February","March","April","May","June","July","August","September","October","November","December"};
    }
    public void openFeeDetails() {
        try {
            Stage stage = new Stage();
            //stage.setResizable(false);
            stage.initOwner(FXMLDocumentController.getPrimaryStage());
            Parent root = FXMLLoader.load(getClass().getResource("FeeDetials.fxml"));
            stage.getIcons().add(new Image(getClass().getResourceAsStream("Icons/brick.png")));
            Scene myScene = new Scene(root);
            stage.setScene(myScene);
            stage.show();
        } catch (IOException ex) {
            ExceptionHandling.showException(ex);
        }
    }
    public void editRecord() {
        paid_jan.setEditable(true);
        paid_feb.setEditable(true);
        paid_mar.setEditable(true);
        paid_apr.setEditable(true);
        paid_may.setEditable(true);
        paid_jun.setEditable(true);
        paid_jul.setEditable(true);
        paid_aug.setEditable(true);
        paid_sep.setEditable(true);
        paid_oct.setEditable(true);
        paid_nov.setEditable(true);
        paid_dec.setEditable(true);
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle("Edit is enabled");
        alert.setContentText("Now you can edit the paid fee boxes by click on it. After editing, click on save button in order to save your record");
        alert.show();
        
    }
    public void searchStudent() {
        new MySQLConnection();
        if(std_id.getText().isEmpty() || year.getText().isEmpty()){
            Alert alert = new Alert(AlertType.INFORMATION);
            alert.setTitle("Fill the form");
            alert.setContentText("Please enter student Id and year then click the search button");
            alert.show();
        }
        else {
            try {
                //let Me clear all the label for the new entry
                total_jan.setText("");
                total_feb.setText("");
                total_mar.setText("");
                total_apr.setText("");
                total_may.setText("");
                total_jun.setText("");
                total_jul.setText("");
                total_aug.setText("");
                total_sep.setText("");
                total_oct.setText("");
                total_nov.setText("");
                total_dec.setText("");
                
                paid_jan.setText("");
                paid_feb.setText("");
                paid_mar.setText("");
                paid_apr.setText("");
                paid_may.setText("");
                paid_jun.setText("");
                paid_jul.setText("");
                paid_aug.setText("");
                paid_sep.setText("");
                paid_oct.setText("");
                paid_nov.setText("");
                paid_dec.setText("");
                
                remain_jan.setText("");
                remain_feb.setText("");
                remain_mar.setText("");
                remain_apr.setText("");
                remain_may.setText("");
                remain_jun.setText("");
                remain_jul.setText("");
                remain_aug.setText("");
                remain_sep.setText("");
                remain_oct.setText("");
                remain_nov.setText("");
                remain_dec.setText("");
                
                date_jan.setText("");
                date_feb.setText("");
                date_mar.setText("");
                date_apr.setText("");
                date_may.setText("");
                date_jun.setText("");
                date_jul.setText("");
                date_aug.setText("");
                date_sep.setText("");
                date_oct.setText("");
                date_nov.setText("");
                date_dec.setText("");
                
                total_promotion.setText("");
                paid_promotion.setText("");
                remaining_promotion.setText("");
                promotion_date.setText("");

                studentId = Integer.parseInt(std_id.getText());
                session = Integer.parseInt(year.getText());
                String query = "SELECT student_name, std_father_name, address, class_id FROM student where student_id = "+studentId+"";
                String currentClassQuery = "select promotedclass.class_id from promotedclass, student"
                        + " where student.student_id = "+studentId+" and student.student_id = promotedclass.student_id";
                prestate = databaseconnection.MySQLConnection.conn.prepareStatement(query);
                rs = prestate.executeQuery();
                if(rs.next()){
                    name.setText(rs.getString("student_name"));
                    fatherName.setText(rs.getString("std_father_name"));
                    adress.setText(rs.getString("address"));
                    prestate = databaseconnection.MySQLConnection.conn.prepareStatement(currentClassQuery);
                    rs = prestate.executeQuery();
                    if(rs.next()){
                       classId = rs.getInt("class_id");
                    }
                    prestate = databaseconnection.MySQLConnection.conn.prepareStatement("select * from fee where class_id = "+classId+"");
                    rs = prestate.executeQuery();
                    if(rs.next()) {
                        totalFee = rs.getInt("monthly_fee");
                        thisMonth = Calendar.getInstance().get(Calendar.MONTH);
                        total_jan.setText(String.valueOf(rs.getInt("monthly_fee")));
                        total_feb.setText(String.valueOf(rs.getInt("monthly_fee")));
                        total_mar.setText(String.valueOf(rs.getInt("monthly_fee")));
                        total_apr.setText(String.valueOf(rs.getInt("monthly_fee")));
                        total_may.setText(String.valueOf(rs.getInt("monthly_fee")));
                        total_jun.setText(String.valueOf(rs.getInt("monthly_fee")));
                        total_jul.setText(String.valueOf(rs.getInt("monthly_fee")));
                        total_aug.setText(String.valueOf(rs.getInt("monthly_fee")));
                        total_sep.setText(String.valueOf(rs.getInt("monthly_fee")));
                        total_oct.setText(String.valueOf(rs.getInt("monthly_fee")));
                        total_nov.setText(String.valueOf(rs.getInt("monthly_fee")));
                        total_dec.setText(String.valueOf(rs.getInt("monthly_fee")));
                        totalProFee = rs.getInt("promotion_fee");
                        total_promotion.setText(String.valueOf(totalProFee)); 
                        if(session == Calendar.getInstance().get(Calendar.YEAR)){
                            totalMonthFee = rs.getInt("monthly_fee")*thisMonth;
                            total_fee.setText(String.valueOf(totalMonthFee+totalProFee));
                            sumOfAllFee = (rs.getInt("monthly_fee")*thisMonth);
                        }
                        else {
                            totalMonthFee = rs.getInt("monthly_fee")*12;
                            total_fee.setText(String.valueOf(totalMonthFee+totalProFee));
                            sumOfAllFee = (rs.getInt("monthly_fee")*12);
                        }
                    }
                    else {
                        total_promotion.setText("0");
                    }
                    prestate = databaseconnection.MySQLConnection.conn.prepareStatement("select *from fee_payment where student_id ="+studentId+" and month='"+month[0]+"' and year = "+session+"");
                    rs = prestate.executeQuery();
                    if(rs.next()){
                        paid_jan.setText(String.valueOf(rs.getInt("fee_paid")));
                        remain_jan.setText(String.valueOf(totalFee - rs.getInt("fee_paid")));
                        date_jan.setText(rs.getString("date"));
                    }
                    prestate = databaseconnection.MySQLConnection.conn.prepareStatement("select *from fee_payment where student_id ="+studentId+" and month='"+month[1]+"' and year = "+session+"");
                    rs = prestate.executeQuery();
                    if(rs.next()){
                        paid_feb.setText(String.valueOf(rs.getInt("fee_paid")));
                        remain_feb.setText(String.valueOf(totalFee - rs.getInt("fee_paid")));
                        date_feb.setText(rs.getString("date"));
                    }
                    prestate = databaseconnection.MySQLConnection.conn.prepareStatement("select *from fee_payment where student_id ="+studentId+" and month='"+month[2]+"' and year = "+session+"");
                    rs = prestate.executeQuery();
                    if(rs.next()){
                        paid_mar.setText(String.valueOf(rs.getInt("fee_paid")));
                        remain_mar.setText(String.valueOf(totalFee - rs.getInt("fee_paid")));
                        date_mar.setText(rs.getString("date"));
                    }
                    prestate = databaseconnection.MySQLConnection.conn.prepareStatement("select *from fee_payment where student_id ="+studentId+" and month='"+month[3]+"' and year = "+session+"");
                    rs = prestate.executeQuery();
                    if(rs.next()){
                        paid_apr.setText(String.valueOf(rs.getInt("fee_paid")));
                        remain_apr.setText(String.valueOf(totalFee - rs.getInt("fee_paid")));
                        date_apr.setText(rs.getString("date"));
                    }
                    prestate = databaseconnection.MySQLConnection.conn.prepareStatement("select *from fee_payment where student_id ="+studentId+" and month='"+month[4]+"' and year = "+session+"");
                    rs = prestate.executeQuery();
                    if(rs.next()){
                        paid_may.setText(String.valueOf(rs.getInt("fee_paid")));
                        remain_may.setText(String.valueOf(totalFee - rs.getInt("fee_paid")));
                        date_may.setText(rs.getString("date"));
                    }
                    prestate = databaseconnection.MySQLConnection.conn.prepareStatement("select *from fee_payment where student_id ="+studentId+" and month='"+month[5]+"' and year = "+session+"");
                    rs = prestate.executeQuery();
                    if(rs.next()){
                        paid_jun.setText(String.valueOf(rs.getInt("fee_paid")));
                        remain_jun.setText(String.valueOf(totalFee - rs.getInt("fee_paid")));
                        date_jun.setText(rs.getString("date"));
                    }
                    prestate = databaseconnection.MySQLConnection.conn.prepareStatement("select *from fee_payment where student_id ="+studentId+" and month='"+month[6]+"' and year = "+session+"");
                     rs = prestate.executeQuery();
                    if(rs.next()){
                        paid_jul.setText(String.valueOf(rs.getInt("fee_paid")));
                        remain_jul.setText(String.valueOf(totalFee - rs.getInt("fee_paid")));
                        date_jul.setText(rs.getString("date"));
                    }
                    prestate = databaseconnection.MySQLConnection.conn.prepareStatement("select *from fee_payment where student_id ="+studentId+" and month='"+month[7]+"' and year = "+session+"");
                    rs = prestate.executeQuery();
                    if(rs.next()){
                        paid_aug.setText(String.valueOf(rs.getInt("fee_paid")));
                        remain_aug.setText(String.valueOf(totalFee - rs.getInt("fee_paid")));
                        date_aug.setText(rs.getString("date"));
                    }
                    prestate = databaseconnection.MySQLConnection.conn.prepareStatement("select *from fee_payment where student_id ="+studentId+" and month='"+month[8]+"' and year = "+session+"");
                    rs = prestate.executeQuery();
                    if(rs.next()){
                        paid_sep.setText(String.valueOf(rs.getInt("fee_paid")));
                        remain_sep.setText(String.valueOf(totalFee - rs.getInt("fee_paid")));
                        date_sep.setText(rs.getString("date"));
                    }
                    prestate = databaseconnection.MySQLConnection.conn.prepareStatement("select *from fee_payment where student_id ="+studentId+" and month='"+month[9]+"' and year = "+session+"");
                    rs = prestate.executeQuery();
                    if(rs.next()){
                        paid_oct.setText(String.valueOf(rs.getInt("fee_paid")));
                        remain_oct.setText(String.valueOf(totalFee - rs.getInt("fee_paid")));
                        date_oct.setText(rs.getString("date"));
                    }
                    prestate = databaseconnection.MySQLConnection.conn.prepareStatement("select *from fee_payment where student_id ="+studentId+" and month='"+month[10]+"' and year = "+session+"");
                    rs = prestate.executeQuery();
                    if(rs.next()){
                        paid_nov.setText(String.valueOf(rs.getInt("fee_paid")));
                        remain_nov.setText(String.valueOf(totalFee - rs.getInt("fee_paid")));
                        date_nov.setText(rs.getString("date"));
                    }
                    prestate = databaseconnection.MySQLConnection.conn.prepareStatement("select *from fee_payment where student_id ="+studentId+" and month='"+month[11]+"' and year = "+session+"");
                    rs = prestate.executeQuery();
                    if(rs.next()){
                        paid_dec.setText(String.valueOf(rs.getInt("fee_paid")));
                        remain_dec.setText(String.valueOf(totalFee - rs.getInt("fee_paid")));
                        date_dec.setText(rs.getString("date"));
                    }
                    prestate = databaseconnection.MySQLConnection.conn.prepareStatement("select *from pay_promotion_fee where student_id ="+studentId+" and session = "+session+"");
                    rs = prestate.executeQuery();
                    if(rs.next()){
                        paidPromotion = rs.getInt("fee_paid");
                        paid_promotion.setText(String.valueOf(paidPromotion));
                        promotion_date.setText(rs.getString("date"));
                        remaining_promotion.setText(String.valueOf(Integer.parseInt(total_promotion.getText())-rs.getInt("fee_paid")));
                    }
                    else {
                        paidPromotion = 0;
                        paid_promotion.setText("0");
                        remaining_promotion.setText(String.valueOf(Integer.parseInt(total_promotion.getText())- Integer.parseInt(paid_promotion.getText())));
                    }
                    prestate = databaseconnection.MySQLConnection.conn.prepareStatement("select sum(fee_paid) as s from fee_payment where student_id = "+studentId+" and year = "+session+"" );
                    rs = prestate.executeQuery();
                    if(rs.next()){
                        tota_paid_fee.setText(String.valueOf(rs.getInt("s")+paidPromotion));
                        totalPaidFee = rs.getInt("s");
                        total_remain_fee.setText(String.valueOf((sumOfAllFee - totalPaidFee) + Integer.parseInt(remaining_promotion.getText())));
                    }
                    rs.close();
                    prestate.close();
                }
                else {
                    Alert alert = new Alert(AlertType.INFORMATION);
                    alert.setTitle("Record not found");
                    alert.setContentText("Sorry! record not found");
                    alert.show();  
                }
                rs.close();
                prestate.close();
                MySQLConnection.conn.close();
            } catch (SQLException ex) {
                ExceptionHandling.showException(ex);
            }    
        }
    }
    public void saveFee() {
        new MySQLConnection();
        try{
        if(!(paid_jan.getText().isEmpty())){
                String updateQuery = "update fee_payment set fee_paid = "+Integer.parseInt(paid_jan.getText())+" "
                        + "where student_id = "+studentId+" and month = '"+month[0]+"' and year = '"+session+"'";
                prestate = databaseconnection.MySQLConnection.conn.prepareStatement(updateQuery);
                prestate.execute();
        }
        if(!(paid_feb.getText().isEmpty())){
            String updateQuery = "update fee_payment set fee_paid = "+Integer.parseInt(paid_feb.getText())+" "
                    + "where student_id = "+studentId+" and month = '"+month[1]+"' and year = "+session+"";
            prestate = databaseconnection.MySQLConnection.conn.prepareStatement(updateQuery);
            prestate.execute();
        }
        if(!(paid_mar.getText().isEmpty())){
            String updateQuery = "update fee_payment set fee_paid = "+Integer.parseInt(paid_mar.getText())+" "
                    + "where student_id = "+studentId+" and month = '"+month[2]+"' and year = "+session+"";
            prestate = databaseconnection.MySQLConnection.conn.prepareStatement(updateQuery);
            prestate.execute();
        }
        if(!(paid_apr.getText().isEmpty())){
            String updateQuery = "update fee_payment set fee_paid = "+Integer.parseInt(paid_apr.getText())+" "
                    + "where student_id = "+studentId+" and month = '"+month[3]+"' and year = "+session+"";
            prestate = databaseconnection.MySQLConnection.conn.prepareStatement(updateQuery);
            prestate.execute();
        }
        if(!(paid_may.getText().isEmpty())){
            String updateQuery = "update fee_payment set fee_paid = "+Integer.parseInt(paid_may.getText())+" "
                    + "where student_id = "+studentId+" and month = '"+month[4]+"' and year = "+session+"";
            prestate = databaseconnection.MySQLConnection.conn.prepareStatement(updateQuery);
            prestate.execute();
        }
        if(!(paid_jun.getText().isEmpty())){
            String updateQuery = "update fee_payment set fee_paid = "+Integer.parseInt(paid_jun.getText())+" "
                    + "where student_id = "+studentId+" and month = '"+month[5]+"' and year = "+session+"";
            prestate = databaseconnection.MySQLConnection.conn.prepareStatement(updateQuery);
            prestate.execute();
        }
        if(!(paid_jul.getText().isEmpty())){
            String updateQuery = "update fee_payment set fee_paid = "+Integer.parseInt(paid_jul.getText())+" "
                    + "where student_id = "+studentId+" and month = '"+month[6]+"' and year = "+session+"";
            prestate = databaseconnection.MySQLConnection.conn.prepareStatement(updateQuery);
            prestate.execute();
        }
        if(!(paid_aug.getText().isEmpty())){
            String updateQuery = "update fee_payment set fee_paid = "+Integer.parseInt(paid_aug.getText())+" "
                    + "where student_id = "+studentId+" and month = '"+month[7]+"' and year = "+session+"";
            prestate = databaseconnection.MySQLConnection.conn.prepareStatement(updateQuery);
            prestate.execute();
        }
        if(!(paid_sep.getText().isEmpty())){
            String updateQuery = "update fee_payment set fee_paid = "+Integer.parseInt(paid_sep.getText())+" "
                    + "where student_id = "+studentId+" and month = '"+month[8]+"' and year = "+session+"";
            prestate = databaseconnection.MySQLConnection.conn.prepareStatement(updateQuery);
            prestate.execute();
        }
        if(!(paid_oct.getText().isEmpty())){
            String updateQuery = "update fee_payment set fee_paid = "+Integer.parseInt(paid_oct.getText())+" "
                    + "where student_id = "+studentId+" and month = '"+month[9]+"' and year = "+session+"";
            prestate = databaseconnection.MySQLConnection.conn.prepareStatement(updateQuery);
            prestate.execute();
        }
        if(!(paid_nov.getText().isEmpty())){
            String updateQuery = "update fee_payment set fee_paid = "+Integer.parseInt(paid_nov.getText())+" "
                    + "where student_id = "+studentId+" and month = '"+month[10]+"' and year = "+session+"";
            prestate = databaseconnection.MySQLConnection.conn.prepareStatement(updateQuery);
            prestate.execute();
        }
        if(!(paid_dec.getText().isEmpty())){
            String updateQuery = "update fee_payment set fee_paid = "+Integer.parseInt(paid_dec.getText())+" "
                    + "where student_id = "+studentId+" and month = '"+month[11]+"' and year = "+session+"";
            prestate = databaseconnection.MySQLConnection.conn.prepareStatement(updateQuery);
            prestate.execute();
        }
        if(!(paid_promotion.getText().isEmpty())){
            String updateQuery = "update pay_promotion_fee set fee_paid = "+Integer.parseInt(paid_promotion.getText())+" "
                    + "where student_id = "+studentId+" and session = "+session+"";
            prestate = databaseconnection.MySQLConnection.conn.prepareStatement(updateQuery);
            prestate.execute();
        }
        prestate.close();
        MySQLConnection.conn.close();
        //reload the data
        searchStudent();
        } catch (SQLException ex) {
                ExceptionHandling.showException(ex);
            }
    }
    public void printFee() {
        search_btn.setVisible(false);
        save_btn.setVisible(false);
        print_btn.setVisible(false);
        sign_area.setVisible(true);
        Node node = fee_details_window;
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
    search_btn.setVisible(true);
    save_btn.setVisible(true);
    print_btn.setVisible(true);
    sign_area.setVisible(false);
    }
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        std_id.textProperty().addListener((ObservableValue<? extends String> observable, String oldValue, String newValue) -> { 
            if (!newValue.matches("\\d{0,50}")) {
                std_id.setText(oldValue);
            }
        });
        year.textProperty().addListener((ObservableValue<? extends String> observable, String oldValue, String newValue) -> { 
            if (!newValue.matches("\\d{0,4}")) {
                year.setText(oldValue);
            }
        });
    }    
    
}
