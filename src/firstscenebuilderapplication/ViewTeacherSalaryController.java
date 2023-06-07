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
import java.io.OutputStream;
import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Calendar;
import java.util.ResourceBundle;
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
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;
import javafx.scene.transform.Scale;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author Bakht Zada
 */
public class ViewTeacherSalaryController implements Initializable {
   @FXML
    private TextField id;

    @FXML
    private TextField id1;

    @FXML
    private TextField name;

    @FXML
    private TextField father_name;

    @FXML
    private TextField nic;

    @FXML
    private TextField address;

    @FXML
    private TextField dob;

    @FXML
    private TextField designation;

    @FXML
    private Rectangle picture;

    @FXML
    private TextField pay_jan;

    @FXML
    private TextField pay_feb;

    @FXML
    private TextField pay_mar;

    @FXML
    private TextField pay_apr;

    @FXML
    private TextField pay_may;

    @FXML
    private TextField pay_jun;

    @FXML
    private TextField pay_jul;

    @FXML
    private TextField pay_aug;

    @FXML
    private TextField pay_sep;

    @FXML
    private TextField pay_oct;

    @FXML
    private TextField pay_nov;

    @FXML
    private TextField pay_dec;

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
    private Label total_salary;

    @FXML
    private Label total_paid_salary;

    @FXML
    private Label total_remaining;

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
    private TextField session;
    
    @FXML
    private Button search;
    
    @FXML
    private Button edit;
    
    @FXML
    private Button print;
    
    @FXML
    private Button save;
   
    @FXML
    private AnchorPane salary_window;
    
    @FXML
    private AnchorPane sign_area;
    
    PreparedStatement prestate;
    ResultSet rs;
    int totalSalary;
    int teacherId;
    int year;
    String month [] = new String[]{"January","February","March","April","May","June","July","August","September","October","November","December"};
    public void loadTeacherSalary() {
            try {
        Stage stage = new Stage();
        stage.setTitle("");
        //stage.initStyle(StageStyle.TRANSPARENT);
       // stage.setResizable(false);
       stage.initOwner(FXMLDocumentController.getPrimaryStage());
        Parent root = FXMLLoader.load(getClass().getResource("ViewTeacherSalary.fxml"));
        stage.getIcons().add(new Image(getClass().getResourceAsStream("Icons/brick.png")));
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    } catch (IOException ex) {
        ExceptionHandling.showException(ex);
    }
    }
    public void searchTeacher() {

        new MySQLConnection();
        try {
            if(!(id.getText().isEmpty()||session.getText().isEmpty())) {
                teacherId = Integer.parseInt(id.getText());
                int totalPaidSalary = 0;
                year = Integer.parseInt(session.getText());
                
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
                
                pay_jan.setText("");
                pay_feb.setText("");
                pay_mar.setText("");
                pay_apr.setText("");
                pay_may.setText("");
                pay_jun.setText("");
                pay_jul.setText("");
                pay_aug.setText("");
                pay_sep.setText("");
                pay_oct.setText("");
                pay_nov.setText("");
                pay_dec.setText("");
                
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
                int thisMonth = Calendar.getInstance().get(Calendar.MONTH);
                
                String query = "select * from teacher where teacher_id ="+teacherId+"";
                prestate = databaseconnection.MySQLConnection.conn.prepareStatement(query); 
                rs = prestate.executeQuery();
                if(rs.next()) {
                    name.setText(rs.getString("teacher_name"));
                    father_name.setText(rs.getString("father_name"));
                    nic.setText(rs.getString("nic"));
                    address.setText(rs.getString("address"));
                    dob.setText(rs.getString("dob"));
                    designation.setText(rs.getString("designation"));
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
                    picture.setFill(new ImagePattern(image));
                    }
                rs.close();
                prestate.close();
                String selectSalary = "select *from teacher_salary where teacher_id = "+teacherId+"";
                prestate = databaseconnection.MySQLConnection.conn.prepareStatement(selectSalary);
                rs = prestate.executeQuery();
                if(rs.next()){
                    total_jan.setText(rs.getString("salary"));
                    total_feb.setText(rs.getString("salary"));
                    total_mar.setText(rs.getString("salary"));
                    total_apr.setText(rs.getString("salary"));
                    total_may.setText(rs.getString("salary"));
                    total_jun.setText(rs.getString("salary"));
                    total_jul.setText(rs.getString("salary"));
                    total_aug.setText(rs.getString("salary"));
                    total_sep.setText(rs.getString("salary"));
                    total_oct.setText(rs.getString("salary"));
                    total_nov.setText(rs.getString("salary"));
                    total_dec.setText(rs.getString("salary"));
                    if(year == Calendar.getInstance().get(Calendar.YEAR)) {
                       
                        totalSalary = Integer.parseInt(rs.getString("salary"))*thisMonth;
                        total_salary.setText(String.valueOf(totalSalary));
                    }
                    else {
                        totalSalary = Integer.parseInt(rs.getString("salary"))*12;
                        total_salary.setText(String.valueOf(totalSalary));
                    }    
                }
                rs.close();
                prestate.close();
                prestate = databaseconnection.MySQLConnection.conn.prepareStatement("select *from pay_salary where teacher_id = "+teacherId+""
                        + " and month = '"+month[0]+"' and year = "+year+"");
                rs = prestate.executeQuery();
                if(rs.next()) {
                    pay_jan.setText(rs.getString("salary"));
                    date_jan.setText(rs.getString("date"));
                    remain_jan.setText(String.valueOf(Integer.parseInt(total_jan.getText())-Integer.parseInt(pay_jan.getText())));
                    totalPaidSalary += Integer.valueOf(rs.getString("salary"));
                }
                else if(1 <= thisMonth) {
                    pay_jan.setText("0");
                    remain_jan.setText(String.valueOf(Integer.parseInt(total_jan.getText())-Integer.parseInt(pay_jan.getText())));
                }
                rs.close();
                prestate.close();
                prestate = databaseconnection.MySQLConnection.conn.prepareStatement("select *from pay_salary where teacher_id = "+teacherId+""
                        + " and month = '"+month[1]+"' and year = "+year+"");
                rs = prestate.executeQuery();
                if(rs.next()) {
                    pay_feb.setText(rs.getString("salary"));
                    date_feb.setText(rs.getString("date"));
                    remain_feb.setText(String.valueOf(Integer.parseInt(total_feb.getText())-Integer.parseInt(pay_feb.getText())));
                    totalPaidSalary += Integer.valueOf(rs.getString("salary"));
                }
                else if(2 <= thisMonth) {
                    pay_feb.setText("0");
                    remain_feb.setText(String.valueOf(Integer.parseInt(total_feb.getText())-Integer.parseInt(pay_feb.getText())));
                }
                rs.close();
                prestate.close();
                prestate = databaseconnection.MySQLConnection.conn.prepareStatement("select *from pay_salary where teacher_id = "+teacherId+""
                        + " and month = '"+month[2]+"' and year = "+year+"");
                rs = prestate.executeQuery();
                if(rs.next()) {
                    pay_mar.setText(rs.getString("salary"));
                    date_mar.setText(rs.getString("date"));
                    remain_mar.setText(String.valueOf(Integer.parseInt(total_mar.getText())-Integer.parseInt(pay_mar.getText())));
                    totalPaidSalary += Integer.valueOf(rs.getString("salary"));
                }
                else if(3 <= thisMonth) {
                    pay_mar.setText("0");
                    remain_mar.setText(String.valueOf(Integer.parseInt(total_mar.getText())-Integer.parseInt(pay_mar.getText())));
                }
                rs.close();
                prestate.close();
                prestate = databaseconnection.MySQLConnection.conn.prepareStatement("select *from pay_salary where teacher_id = "+teacherId+""
                        + " and month = '"+month[3]+"' and year = "+year+"");
                rs = prestate.executeQuery();
                if(rs.next()) {
                    pay_apr.setText(rs.getString("salary"));
                    date_apr.setText(rs.getString("date"));
                    remain_apr.setText(String.valueOf(Integer.parseInt(total_apr.getText())-Integer.parseInt(pay_apr.getText())));
                    totalPaidSalary += Integer.valueOf(rs.getString("salary"));
                }
                else if(4 <= thisMonth) {
                    pay_apr.setText("0");
                    remain_apr.setText(String.valueOf(Integer.parseInt(total_apr.getText())-Integer.parseInt(pay_apr.getText())));
                }
                rs.close();
                prestate.close();
                prestate = databaseconnection.MySQLConnection.conn.prepareStatement("select *from pay_salary where teacher_id = "+teacherId+""
                        + " and month = '"+month[4]+"' and year = "+year+"");
                rs = prestate.executeQuery();
                if(rs.next()) {
                    pay_may.setText(rs.getString("salary"));
                    date_may.setText(rs.getString("date"));
                    remain_may.setText(String.valueOf(Integer.parseInt(total_may.getText())-Integer.parseInt(pay_may.getText())));
                    totalPaidSalary += Integer.valueOf(rs.getString("salary"));
                }
                else if(5 <= thisMonth) {
                    pay_may.setText("0");
                    remain_may.setText(String.valueOf(Integer.parseInt(total_may.getText())-Integer.parseInt(pay_may.getText())));
                }
                rs.close();
                prestate.close();
                prestate = databaseconnection.MySQLConnection.conn.prepareStatement("select *from pay_salary where teacher_id = "+teacherId+""
                        + " and month = '"+month[5]+"' and year = "+year+"");
                rs = prestate.executeQuery();
                if(rs.next()) {
                    pay_jun.setText(rs.getString("salary"));
                    date_jun.setText(rs.getString("date"));
                    remain_jun.setText(String.valueOf(Integer.parseInt(total_jun.getText())-Integer.parseInt(pay_jun.getText())));
                    totalPaidSalary += Integer.valueOf(rs.getString("salary"));
                }
                else if(6 <= thisMonth) {
                    pay_jun.setText("0");
                    remain_jun.setText(String.valueOf(Integer.parseInt(total_jun.getText())-Integer.parseInt(pay_jun.getText())));
                }
                rs.close();
                prestate.close();
                prestate = databaseconnection.MySQLConnection.conn.prepareStatement("select *from pay_salary where teacher_id = "+teacherId+""
                        + " and month = '"+month[6]+"' and year = "+year+"");
                rs = prestate.executeQuery();
                if(rs.next()) {
                    pay_jul.setText(rs.getString("salary"));
                    date_jul.setText(rs.getString("date"));
                    remain_jul.setText(String.valueOf(Integer.parseInt(total_jul.getText())-Integer.parseInt(pay_jul.getText())));
                    totalPaidSalary += Integer.valueOf(rs.getString("salary"));
                }
                else if(7 <= thisMonth) {
                    pay_jul.setText("0");
                    remain_jul.setText(String.valueOf(Integer.parseInt(total_jul.getText())-Integer.parseInt(pay_jul.getText())));
                }
                rs.close();
                prestate.close();
                prestate = databaseconnection.MySQLConnection.conn.prepareStatement("select *from pay_salary where teacher_id = "+teacherId+""
                        + " and month = '"+month[7]+"' and year = "+year+"");
                rs = prestate.executeQuery();
                if(rs.next()) {
                    pay_aug.setText(rs.getString("salary"));
                    date_aug.setText(rs.getString("date"));
                    remain_aug.setText(String.valueOf(Integer.parseInt(total_aug.getText())-Integer.parseInt(pay_aug.getText())));
                    totalPaidSalary += Integer.valueOf(rs.getString("salary"));
                }
                else if(8 <= thisMonth) {
                    pay_aug.setText("0");
                    remain_aug.setText(String.valueOf(Integer.parseInt(total_aug.getText())-Integer.parseInt(pay_aug.getText())));
                }
                rs.close();
                prestate.close();
                prestate = databaseconnection.MySQLConnection.conn.prepareStatement("select *from pay_salary where teacher_id = "+teacherId+""
                        + " and month = '"+month[8]+"' and year = "+year+"");
                rs = prestate.executeQuery();
                if(rs.next()) {
                    pay_sep.setText(rs.getString("salary"));
                    date_sep.setText(rs.getString("date"));
                    remain_sep.setText(String.valueOf(Integer.parseInt(total_sep.getText())-Integer.parseInt(pay_sep.getText())));
                    totalPaidSalary += Integer.valueOf(rs.getString("salary"));
                }
                else if(9 <= thisMonth) {
                    pay_sep.setText("0");
                    remain_sep.setText(String.valueOf(Integer.parseInt(total_sep.getText())-Integer.parseInt(pay_sep.getText())));
                }
                rs.close();
                prestate.close();
                prestate = databaseconnection.MySQLConnection.conn.prepareStatement("select *from pay_salary where teacher_id = "+teacherId+""
                        + " and month = '"+month[9]+"' and year = "+year+"");
                rs = prestate.executeQuery();
                if(rs.next()) {
                    pay_oct.setText(rs.getString("salary"));
                    date_oct.setText(rs.getString("date"));
                    remain_oct.setText(String.valueOf(Integer.parseInt(total_oct.getText())-Integer.parseInt(pay_oct.getText())));
                    totalPaidSalary += Integer.valueOf(rs.getString("salary"));
                }
                else if(10 <= thisMonth) {
                    pay_oct.setText("0");
                    remain_oct.setText(String.valueOf(Integer.parseInt(total_oct.getText())-Integer.parseInt(pay_oct.getText())));
                }
                rs.close();
                prestate.close();
                prestate = databaseconnection.MySQLConnection.conn.prepareStatement("select *from pay_salary where teacher_id = "+teacherId+""
                        + " and month = '"+month[10]+"' and year = "+year+"");
                rs = prestate.executeQuery();
                if(rs.next()) {
                    pay_nov.setText(rs.getString("salary"));
                    date_nov.setText(rs.getString("date"));
                    remain_nov.setText(String.valueOf(Integer.parseInt(total_nov.getText())-Integer.parseInt(pay_nov.getText())));
                    totalPaidSalary += Integer.valueOf(rs.getString("salary"));
                }
                else if(11 <= thisMonth) {
                    pay_nov.setText("0");
                    remain_nov.setText(String.valueOf(Integer.parseInt(total_nov.getText())-Integer.parseInt(pay_nov.getText())));
                }
                else if(12 <= thisMonth) {
                    pay_dec.setText("0");
                    remain_dec.setText(String.valueOf(Integer.parseInt(total_dec.getText())-Integer.parseInt(pay_dec.getText())));
                }
                rs.close();
                prestate.close();
                prestate = databaseconnection.MySQLConnection.conn.prepareStatement("select *from pay_salary where teacher_id = "+teacherId+""
                        + " and month = '"+month[11]+"' and year = "+year+"");
                rs = prestate.executeQuery();
                if(rs.next()) {
                    pay_dec.setText(rs.getString("salary"));
                    date_dec.setText(rs.getString("date"));
                    remain_dec.setText(String.valueOf(Integer.parseInt(total_dec.getText())-Integer.parseInt(pay_dec.getText())));
                    totalPaidSalary += Integer.valueOf(rs.getString("salary"));
                }
                rs.close();
                prestate.close();
                databaseconnection.MySQLConnection.conn.close();
                total_paid_salary.setText(String.valueOf(totalPaidSalary));
                total_remaining.setText(String.valueOf(totalSalary - totalPaidSalary));
                }
            else {
              Alert alert = new Alert(Alert.AlertType.ERROR);
              alert.setTitle("Error Occured");
              alert.setContentText("Please Enter Teacher Id and Year, then press search button");
              alert.show();
            }
            
    } catch (SQLException ex) {
            ExceptionHandling.showException(ex);
        }   catch (FileNotFoundException ex) {
                ExceptionHandling.showException(ex);
            } catch (IOException ex) {
           ExceptionHandling.showException(ex);
       } catch (Exception ex) {
            ExceptionHandling.showException(ex);
       }
    }
    public void editRecord() {
        pay_jan.setEditable(true);
        pay_feb.setEditable(true);
        pay_mar.setEditable(true);
        pay_apr.setEditable(true);
        pay_may.setEditable(true);
        pay_jun.setEditable(true);
        pay_jul.setEditable(true);
        pay_aug.setEditable(true);
        pay_sep.setEditable(true);
        pay_oct.setEditable(true);
        pay_nov.setEditable(true);
        pay_dec.setEditable(true);
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle("Editable is on");
        alert.setContentText("Pay salary text boxes are editable now. Just click on the box which you want to edit and then press save for saving the changes");
        alert.show();          
    }
    public void printRecord() {
        search.setVisible(false);
        save.setVisible(false);
        print.setVisible(false);
        edit.setVisible(false);
        sign_area.setVisible(true);
        Node node =  salary_window;;
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
    search.setVisible(true);
    save.setVisible(true);
    print.setVisible(true);
    edit.setVisible(true);
    sign_area.setVisible(false);
    }
    public void saveRecord() {
        try {
            new MySQLConnection();
            if(!(pay_jan.getText().isEmpty())) {
                prestate = databaseconnection.MySQLConnection.conn.prepareStatement("update pay_salary set salary = "+Integer.parseInt(pay_jan.getText())+""
                        + " where teacher_id = "+teacherId+" and month = '"+month[0]+"' and year = "+year+"");
                prestate.execute();
            } 
            if(!(pay_feb.getText().isEmpty())) {
                prestate = databaseconnection.MySQLConnection.conn.prepareStatement("update pay_salary set salary = "+Integer.parseInt(pay_feb.getText())+""
                        + " where teacher_id = "+teacherId+" and month = '"+month[1]+"' and year = "+year+"");
                prestate.execute();
            } 
            if(!(pay_mar.getText().isEmpty())) {
                prestate = databaseconnection.MySQLConnection.conn.prepareStatement("update pay_salary set salary = "+Integer.parseInt(pay_mar.getText())+""
                        + " where teacher_id = "+teacherId+" and  month = '"+month[2]+"' and year = "+year+"");
                prestate.execute();
            } 
            if(!(pay_apr.getText().isEmpty())) {
                prestate = databaseconnection.MySQLConnection.conn.prepareStatement("update pay_salary set salary = "+Integer.parseInt(pay_apr.getText())+""
                        + " where teacher_id = "+teacherId+" and  month = '"+month[3]+"' and year = "+year+"");
                prestate.execute();
            } 
            if(!(pay_may.getText().isEmpty())) {
                prestate = databaseconnection.MySQLConnection.conn.prepareStatement("update pay_salary set salary = "+Integer.parseInt(pay_may.getText())+""
                        + " where teacher_id = "+teacherId+" and month = '"+month[4]+"' and year = "+year+"");
                prestate.execute();
            } 
            if(!(pay_jun.getText().isEmpty())) {
                prestate = databaseconnection.MySQLConnection.conn.prepareStatement("update pay_salary set salary = "+Integer.parseInt(pay_jun.getText())+""
                        + " where teacher_id = "+teacherId+" and month = '"+month[5]+"' and year = "+year+"");
                prestate.execute();
            } 
            if(!(pay_jul.getText().isEmpty())) {
                prestate = databaseconnection.MySQLConnection.conn.prepareStatement("update pay_salary set salary = "+Integer.parseInt(pay_jul.getText())+""
                        + " where teacher_id = "+teacherId+" and month = '"+month[6]+"' and year = "+year+"");
                prestate.execute();
            } 
            if(!(pay_aug.getText().isEmpty())) {
                prestate = databaseconnection.MySQLConnection.conn.prepareStatement("update pay_salary set salary = "+Integer.parseInt(pay_aug.getText())+""
                        + " where teacher_id = "+teacherId+" and month = '"+month[7]+"' and year = "+year+"");
                prestate.execute();
            } 
            if(!(pay_sep.getText().isEmpty())) {
                prestate = databaseconnection.MySQLConnection.conn.prepareStatement("update pay_salary set salary = "+Integer.parseInt(pay_sep.getText())+""
                        + " where teacher_id = "+teacherId+" and month = '"+month[8]+"' and year = "+year+"");
                prestate.execute();
            } 
            if(!(pay_oct.getText().isEmpty())) {
                prestate = databaseconnection.MySQLConnection.conn.prepareStatement("update pay_salary set salary = "+Integer.parseInt(pay_oct.getText())+""
                        + " where teacher_id = "+teacherId+"and month = '"+month[9]+"' and year = "+year+"");
                prestate.execute();
            } 
            if(!(pay_nov.getText().isEmpty())) {
                prestate = databaseconnection.MySQLConnection.conn.prepareStatement("update pay_salary set salary = "+Integer.parseInt(pay_nov.getText())+""
                        + " where teacher_id = "+teacherId+" and month = '"+month[10]+"' and year = "+year+"");
                prestate.execute();
            } 
            if(!(pay_dec.getText().isEmpty())) {
                prestate = databaseconnection.MySQLConnection.conn.prepareStatement("update pay_salary set salary = "+Integer.parseInt(pay_dec.getText())+" "
                        + "where teacher_id = "+teacherId+" and month = '"+month[11]+"' and year = "+year+"");
                prestate.execute();
            } 
            searchTeacher();
            MySQLConnection.conn.close();
        } catch (SQLException ex) {
               ExceptionHandling.showException(ex);
            }
    }
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    
    
}
