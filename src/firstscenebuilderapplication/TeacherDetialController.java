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
import java.sql.Statement;
import java.util.Optional;
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
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;
import javafx.scene.transform.Scale;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author Bakht Zada
 */

public class TeacherDetialController implements Initializable {
        @FXML
    private TextField id;

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
    private TextField gender;

    @FXML
    private Rectangle picture;

    @FXML
    private TextField mobile_number;

    @FXML
    private TextField qualification;

    @FXML
    private TextField major;
    
    @FXML
    private TextField designation;
    
    @FXML
    private AnchorPane printArea;
    
    private PreparedStatement prestate;
    private ResultSet rs;
    DeleteLoginController deletelogin = new DeleteLoginController();
    @FXML
    private Pane SignArea;
    @FXML
    private VBox buttonBox;
    public void openTeacherDetial() {
        try {
            Stage stage = new Stage();
            stage.setTitle("");
            //stage.initStyle(StageStyle.TRANSPARENT);
            //stage.setResizable(false);
            Parent root = FXMLLoader.load(getClass().getResource("TeacherDetial.fxml"));
            stage.getIcons().add(new Image(getClass().getResourceAsStream("Icons/brick.png")));
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        } catch (IOException ex) {
            Logger.getLogger(TeacherDetialController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    public void searchTeacher() throws IOException  {
        new MySQLConnection();
        try {
            if(!(id.getText().isEmpty())) {
                int teacherId = Integer.parseInt(id.getText());
                String query = "select * from teacher where teacher_id ="+teacherId+"";
                prestate = databaseconnection.MySQLConnection.conn.prepareStatement(query); 
                rs = prestate.executeQuery();
                if(rs.next()) {
                    name.setText(rs.getString("teacher_name"));
                    father_name.setText(rs.getString("father_name"));
                    nic.setText(rs.getString("nic"));
                    address.setText(rs.getString("address"));
                    dob.setText(rs.getString("dob"));
                    gender.setText(rs.getString("gender"));
                    mobile_number.setText(rs.getString("cell"));
                    qualification.setText(rs.getString("qualification"));
                    major.setText(rs.getString("major"));
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
                    rs.close();
                    prestate.close();
                    Image image = new Image("file:photo.jpg");  
                    picture.setFill(new ImagePattern(image));
                    }
                else {
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Sorry");
                    alert.setContentText("Sorry! no record found for the given id");
                    alert.show();
                }
                }
            else {
              Alert alert = new Alert(Alert.AlertType.ERROR);
              alert.setTitle("Error Occured");
              alert.setContentText("Please Enter Teacher Id and then press search button");
              alert.show();
            }
            } catch (SQLException ex) {
                ExceptionHandling.showException(ex);
        }   catch (FileNotFoundException ex) {
                ExceptionHandling.showException(ex);
            }
    }
    public void nextRecord() {
        try {
            if(rs.next()) {
                id.setText(rs.getString("teacher_id"));
                name.setText(rs.getString("teacher_name"));
                father_name.setText(rs.getString("father_name"));
                nic.setText(rs.getString("nic"));
                address.setText(rs.getString("address"));
                dob.setText(rs.getString("dob"));
                gender.setText(rs.getString("gender"));
                mobile_number.setText(rs.getString("cell"));
                qualification.setText(rs.getString("qualification"));
                major.setText(rs.getString("major"));
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
            else {
                Alert alert = new Alert(AlertType.ERROR);
                alert.setTitle("No Record found");
                alert.setContentText("Sorry! there are no more records");
                alert.show();
            }
        } catch (SQLException ex) {
            ExceptionHandling.showException(ex);
        } catch (FileNotFoundException ex) {
             ExceptionHandling.showException(ex);
        } catch (IOException ex) {
             ExceptionHandling.showException(ex);
        }
    }
    public void printRecord() {
        Node node =  printArea;
        buttonBox.setVisible(false);
        SignArea.setVisible(true);
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
        buttonBox.setVisible(true);
        SignArea.setVisible(false);
        node.getTransforms().remove(scale);
    }
    public void previousRecord() {
        try {
            if(rs.previous()) {
                id.setText(rs.getString("teacher_id"));
                name.setText(rs.getString("teacher_name"));
                father_name.setText(rs.getString("father_name"));
                nic.setText(rs.getString("nic"));
                address.setText(rs.getString("address"));
                dob.setText(rs.getString("dob"));
                gender.setText(rs.getString("gender"));
                mobile_number.setText(rs.getString("cell"));
                qualification.setText(rs.getString("qualification"));
                major.setText(rs.getString("major"));
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
            else {
                Alert alert = new Alert(AlertType.ERROR);
                alert.setTitle("No Record found");
                alert.setContentText("Sorry! there are no more records");
                alert.show();
            }
        } catch (SQLException ex) {
            ExceptionHandling.showException(ex);
        } catch (FileNotFoundException ex) {
             ExceptionHandling.showException(ex);
        } catch (IOException ex) {
             ExceptionHandling.showException(ex);
        }
    }
    public void editTeacher() {
        name.setEditable(true);
        father_name.setEditable(true);
        nic.setEditable(true);
        address.setEditable(true);
        gender.setEditable(true);
        mobile_number.setEditable(true);
        qualification.setEditable(true);
        major.setEditable(true);
        designation.setEditable(true);
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Edit Enabled");
        alert.setContentText("Now you can edit record");
        alert.show();
    }
    public void delete() {
        Alert confirm = new Alert(AlertType.CONFIRMATION);
        confirm.setTitle("Are you sure?");
        confirm.setContentText("All of your data will be erased. Are you sure about it?");
        Optional<ButtonType> result = confirm.showAndWait();
        if(result.isPresent() && result.get() == ButtonType.OK) {
            try {
                int teacherId = Integer.parseInt(id.getText());
                String query = "delete from teacher where teacher_id = "+teacherId+"";
                DeleteLoginController.deleteQuery = query;
                deletelogin.loaDeleteLogin();
            } catch (IOException ex) {
                ExceptionHandling.showException(ex);
            }
        }
    }
    public void deleteAll() {
        Alert confirm = new Alert(AlertType.CONFIRMATION);
        confirm.setTitle("Are you sure?");
        confirm.setContentText("All of your data will be erased. Are you sure about it?");
        Optional<ButtonType> result = confirm.showAndWait();
        if(result.isPresent() && result.get() == ButtonType.OK) {
            try {
                String query = "delete from teacher";
                DeleteLoginController.deleteQuery = query;
                deletelogin.loaDeleteLogin();
            } catch (IOException ex) {
                ExceptionHandling.showException(ex);
            }
        }
    }
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        try {
            new MySQLConnection();
            String query = "select * from teacher ";
            Statement st = databaseconnection.MySQLConnection.conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            rs = st.executeQuery(query);
        } catch (SQLException ex) {
            ExceptionHandling.showException(ex);
        }
    }    
    
}
