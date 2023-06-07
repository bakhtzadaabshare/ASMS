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
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
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
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;
import javafx.scene.transform.Scale;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author DIBB E6430
 */
public class MarksDetailsController implements Initializable {
    @FXML
    private GridPane gridpane;
    
    @FXML
    private TextField session;

    @FXML
    private TextField studentId;
    
    @FXML
    private TextField std_name;

    @FXML
    private TextField father_name;
    
    @FXML
    private TextField total_marks;

    @FXML
    private TextField obtain_marks;

    @FXML
    private Label printed_date;

    @FXML
    private TextField std_class;
    
    @FXML
    private Label sign;
    
    @FXML
    private AnchorPane bottonbox;
    
    
    @FXML
    private Button save_btn;

    @FXML
    private Button calc_btn;

    @FXML
    private Button print_btn;
    
    @FXML
    private AnchorPane dmbackground;
    
    @FXML
    private Text schoolName;
    private PreparedStatement prestate;
    private int classId;
    private ResultSet rs;
    private int counter;
    private TextField[] obtainMarks = new TextField[10];
    private TextField[]  totalMarks = new TextField[10];
    private TextField[] percentage = new TextField[10];
    private TextField subjects;
    private int i =0;
    private int k =0;
    public void openMarksDetails() throws IOException {
        Stage stage = new Stage();
        stage.setResizable(false);
        stage.initOwner(FXMLDocumentController.getPrimaryStage());
        Parent root = FXMLLoader.load(getClass().getResource("MarksDetails.fxml"));
        stage.getIcons().add(new Image(getClass().getResourceAsStream("Icons/brick.png")));
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }
    public void searchStudent(){
        new MySQLConnection();
        if(studentId.getText().isEmpty()) {
            Alert message = new Alert(Alert.AlertType.WARNING);
            message.setTitle("Form is not completed!");
            message.setContentText("Enter student Id and try again");
            message.show();
        }
        else {
            try {
                int stdId = Integer.parseInt(studentId.getText());
                String query = "select student.student_name, student.std_father_name, promotedclass.class_id, class.class_name from class, promotedclass, student "
                + "where student.student_id = promotedclass.student_id and promotedclass.class_id = class.class_id and student.student_id = "+stdId+"";
                prestate = databaseconnection.MySQLConnection.conn.prepareStatement(query);
                rs = prestate.executeQuery();
                if(rs.next()) {
                    classId = rs.getInt("class_id");
                    std_name.setText(rs.getString("student_name"));
                    father_name.setText(rs.getString("std_father_name"));
                    std_class.setText(rs.getString("class_name"));
                    PreparedStatement prestate1 = MySQLConnection.conn.prepareStatement("select subject.subject_name, subject.subject_marks from subject, assignsubject where "
                            + "assignsubject.subject_id = subject.subject_id and assignsubject.class_id = "+classId+"");
                    rs = prestate1.executeQuery();
                    counter = 2;
                    i = 0;
                    while(rs.next()) {  
                        gridpane.add(subjects = new TextField(rs.getString("subject_name")), 0, counter, 1, 1);
                        subjects.setStyle("-fx-font-size: 15px;-fx-border-color:black;");
                        subjects.setEditable(false);
                        subjects.setFocusTraversable(false);
                        obtainMarks[i] = new TextField();
                        gridpane.add(obtainMarks[i], 1, counter, 1, 1);
                        obtainMarks[i].setStyle("-fx-font-size:15px; -fx-border-color:black; -fx-alignment: center;");
                        totalMarks[i]= new TextField(rs.getString("subject_marks"));
                        gridpane.add(totalMarks[i], 2, counter, 1, 1);
                        totalMarks[i].setStyle("-fx-font-size:15px;  -fx-border-color:black; -fx-alignment: center;");
                        totalMarks[i].setFocusTraversable(false);
                        totalMarks[i].setEditable(false);
                        percentage[i] = new TextField();
                        gridpane.add(percentage[i], 3, counter, 1, 1);
                        percentage[i].setStyle("-fx-font-size:15px;  -fx-border-color:black; -fx-alignment: center;");
                        percentage[i].setFocusTraversable(false);
                        percentage[i].setEditable(false);
                        counter = counter + 1;
                        i = i +1;
                    }
                }
                else {
                    Alert message = new Alert(Alert.AlertType.INFORMATION);
                    message.setTitle("Sorry no record found");
                    message.setContentText("No record found for the given id please try again");
                    message.show();
                }
                //Call the tableViewForClass() function in order to reload the data 
                prestate.close();
                if(!MySQLConnection.conn.isClosed()) {
                    MySQLConnection.conn.close();
                }
            } catch (SQLException ex) {
                ExceptionHandling.showException(ex);
            }
        }
    }
    public void printRecord() {
        Node node =  dmbackground;
        bottonbox.setVisible(false);
        sign.setVisible(true);
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
        bottonbox.setVisible(true);
        sign.setVisible(false);
        node.getTransforms().remove(scale);
    }
    public void calculate(){
        int totalobtainedMarks = 0;
        int total_total_marks =0;
        for (int j=0; j<i;j++){
            if(!(obtainMarks[j].getText().isEmpty() || totalMarks[j].getText().isEmpty())){
                int total = Integer.parseInt(totalMarks[j].getText());
                float obt = Float.valueOf(obtainMarks[j].getText());
                float per =   (obt/total) *100;
                percentage[j].setText(String.valueOf(per));
                total_total_marks = total_total_marks + total;
                totalobtainedMarks = totalobtainedMarks + (int) obt;
                total_marks.setText(String.valueOf(total_total_marks));
                obtain_marks.setText(String.valueOf(totalobtainedMarks));
            }
    }
    }
    @Override
    public void initialize(URL url, ResourceBundle rb) {

        try {
            // TODO
            new MySQLConnection();
            prestate = MySQLConnection.conn.prepareStatement("select *from school");
            rs = prestate.executeQuery();
            if(rs.next()) {
                schoolName.setText(rs.getString("name"));
            }
            print_btn.setText("_Print");
            save_btn.setText("_Search");
            calc_btn.setText("_Calcuate");
            print_btn.setMnemonicParsing(true);
            save_btn.setMnemonicParsing(true);
            calc_btn.setMnemonicParsing(true);
            DateFormat dateformat=new SimpleDateFormat("yyyy-MM-dd");
            Date date=new Date();
            printed_date.setText(dateformat.format(date));
            session.setText(String.valueOf(Calendar.getInstance().get(Calendar.YEAR)));
        } catch (SQLException ex) {
            Logger.getLogger(MarksDetailsController.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }    
    
}
