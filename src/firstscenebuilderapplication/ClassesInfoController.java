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
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.LineChart;
import javafx.scene.image.Image;
import javafx.scene.text.Text;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author Bakht Zada
 */
public class ClassesInfoController implements Initializable {
    @FXML
    private LineChart<String, Number> students_chart;

    @FXML
    private Text kg_male;

    @FXML
    private Text kg_female;

    @FXML
    private Text kg_total;

    @FXML
    private Text first_male;

    @FXML
    private Text first_female;

    @FXML
    private Text first_total;

    @FXML
    private Text second_male;

    @FXML
    private Text second_female;

    @FXML
    private Text second_tatal;

    @FXML
    private Text third_male;

    @FXML
    private Text third_female;

    @FXML
    private Text third_total;

    @FXML
    private Text fourth_male;

    @FXML
    private Text fourth_female;

    @FXML
    private Text fourth_total;

    @FXML
    private Text fifth_male;

    @FXML
    private Text fifth_female;

    @FXML
    private Text fifth_total;

    @FXML
    private Text sixth_male;

    @FXML
    private Text sixth_female;

    @FXML
    private Text sixth_total;

    @FXML
    private Text seventh_male;

    @FXML
    private Text seventh_female;

    @FXML
    private Text seventh_total;

    @FXML
    private Text eighth_male;

    @FXML
    private Text eight_female;

    @FXML
    private Text eighth_total;

    @FXML
    private Text nineth_male;

    @FXML
    private Text nineth_female;

    @FXML
    private Text nineth_total;

    @FXML
    private Text tenth_male;

    @FXML
    private Text tenth_female;

    @FXML
    private Text tenth_total;
    
    private PreparedStatement prestate;
    private ResultSet rs;
    private final String[] maleLable = new String[]{"kg_male","first_male","second_male","third_male","fourth_male","fifth_male"};


    public void openClassesInfo() {
        try {
            Stage stage = new Stage();
            //stage.setResizable(false);
            stage.initOwner(FXMLDocumentController.getPrimaryStage());
            Parent root = FXMLLoader.load(getClass().getResource("classesInfo.fxml"));
            stage.getIcons().add(new Image(getClass().getResourceAsStream("Icons/brick.png")));
            Scene myScene = new Scene(root);
            stage.setScene(myScene);
            stage.show();
        } catch (IOException ex) {
            ExceptionHandling.showException(ex);
        }
    }
    private void loadDataToChat(){
        try {
            students_chart.getData().clear();
            LineChart.Series total_male_students  = new LineChart.Series<>();
            LineChart.Series total_female_students  = new LineChart.Series<>();
            String query = "select *from class";
            prestate = databaseconnection.MySQLConnection.conn.prepareStatement(query);
            rs = prestate.executeQuery();
            while(rs.next()) {
                String male_query = "select count(*) as studentNo from "
                        + "promotedclass, student where promotedclass.class_id = "+rs.getInt("class_id")+" "
                        + "and student.gender = 'Male' and student.student_id = promotedclass.student_id";
                String female_query = "select count(promotedclass.student_id) as studentNo from "
                        + "promotedclass, student where promotedclass.class_id = "+rs.getInt("class_id")+" "
                        + "and student.gender = 'Female' and student.student_id = promotedclass.student_id";
                prestate = databaseconnection.MySQLConnection.conn.prepareStatement(male_query);
                ResultSet rs1 = prestate.executeQuery();
                prestate = databaseconnection.MySQLConnection.conn.prepareStatement(female_query);
                ResultSet rs2 = prestate.executeQuery();
                if(rs1.next()){
                    String className = rs.getString("class_name");
                    total_male_students.getData().add(new LineChart.Data<>(className,rs1.getInt("studentNo")));
                    if(className.toLowerCase().equals("kg")) {
                        kg_male.setText(String.valueOf(rs1.getInt("studentNo")));
                    }
                    if(className.toLowerCase().equals("first")) {
                        first_male.setText(String.valueOf(rs1.getInt("studentNo")));
                        
                    }
                    if(className.toLowerCase().equals("second")) {
                        second_male.setText(String.valueOf(rs1.getInt("studentNo")));
                    }
                    if(className.toLowerCase().equals("third")) {
                        third_male.setText(String.valueOf(rs1.getInt("studentNo")));
                    }
                    if(className.toLowerCase().equals("fourth")) {
                        fourth_male.setText(String.valueOf(rs1.getInt("studentNo")));
                    }
                    if(className.toLowerCase().equals("fifth")) {
                        fifth_male.setText(String.valueOf(rs1.getInt("studentNo")));
                    }
                    if(className.toLowerCase().equals("sixth")) {
                        sixth_male.setText(String.valueOf(rs1.getInt("studentNo")));
                    }
                    if(className.toLowerCase().equals("seventh")) {
                        seventh_male.setText(String.valueOf(rs1.getInt("studentNo")));
                    }
                    if(className.toLowerCase().equals("eighth")) {
                        eighth_male.setText(String.valueOf(rs1.getInt("studentNo")));
                    }
                    if(className.toLowerCase().equals("ninth")) {
                        nineth_male.setText(String.valueOf(rs1.getInt("studentNo")));
                    }
                    if(className.toLowerCase().equals("tenth")) {
                        tenth_male.setText(String.valueOf(rs1.getInt("studentNo")));
                    }
                }
                if(rs2.next()) {
                    String className = rs.getString("class_name");
                    total_female_students.getData().add(new LineChart.Data<>(className,rs2.getInt("studentNo")));
                    if(className.toLowerCase().equals("kg")) {
                        kg_female.setText(String.valueOf(rs2.getInt("studentNo")));
                    }
                    if(className.toLowerCase().equals("first")) {
                        first_female.setText(String.valueOf(rs2.getInt("studentNo")));
                    }
                    if(className.toLowerCase().equals("second")) {
                        second_female.setText(String.valueOf(rs2.getInt("studentNo")));
                    }
                    if(className.toLowerCase().equals("third")) {
                        third_female.setText(String.valueOf(rs2.getInt("studentNo")));
                    }
                    if(className.toLowerCase().equals("fourth")) {
                        fourth_female.setText(String.valueOf(rs2.getInt("studentNo")));
                    }
                    if(className.toLowerCase().equals("fifth")) {
                        fifth_female.setText(String.valueOf(rs2.getInt("studentNo")));
                    }
                    if(className.toLowerCase().equals("sixth")) {
                        sixth_female.setText(String.valueOf(rs2.getInt("studentNo")));
                    }
                    if(className.toLowerCase().equals("seventh")) {
                        seventh_female.setText(String.valueOf(rs2.getInt("studentNo")));
                    }
                    if(className.toLowerCase().equals("eighth")) {
                        eight_female.setText(String.valueOf(rs2.getInt("studentNo")));
                    }
                    if(className.toLowerCase().equals("ninth")) {
                        nineth_female.setText(String.valueOf(rs2.getInt("studentNo")));
                    }
                    if(className.toLowerCase().equals("tenth")) {
                        tenth_female.setText(String.valueOf(rs2.getInt("studentNo")));
                    }
                }
            }
            kg_total.setText(String.valueOf(Integer.parseInt(kg_male.getText())+ Integer.parseInt(kg_female.getText())));
            first_total.setText(String.valueOf(Integer.parseInt(first_male.getText())+ Integer.parseInt(first_female.getText())));
            second_tatal.setText(String.valueOf(Integer.parseInt(second_male.getText())+ Integer.parseInt(second_female.getText())));
            third_total.setText(String.valueOf(Integer.parseInt(third_male.getText())+ Integer.parseInt(third_female.getText())));
            fourth_total.setText(String.valueOf(Integer.parseInt(fourth_male.getText())+ Integer.parseInt(fourth_female.getText())));
            fifth_total.setText(String.valueOf(Integer.parseInt(fifth_male.getText())+ Integer.parseInt(fifth_female.getText())));
            sixth_total.setText(String.valueOf(Integer.parseInt(sixth_male.getText())+ Integer.parseInt(sixth_female.getText())));
            seventh_total.setText(String.valueOf(Integer.parseInt(seventh_male.getText())+ Integer.parseInt(seventh_female.getText())));
            eighth_total.setText(String.valueOf(Integer.parseInt(eighth_male.getText())+ Integer.parseInt(eight_female.getText())));
            nineth_total.setText(String.valueOf(Integer.parseInt(nineth_male.getText())+ Integer.parseInt(nineth_female.getText())));
            tenth_total.setText(String.valueOf(Integer.parseInt(tenth_male.getText())+ Integer.parseInt(tenth_female.getText())));

            students_chart.getData().add(total_male_students);
            total_male_students.setName("Male Students");
            students_chart.getData().add(total_female_students);
            total_female_students.setName("Female Students");
            rs.close();
            prestate.close();
        } catch (SQLException ex) {
            Logger.getLogger(ClassesInfoController.class.getName()).log(Level.SEVERE, null, ex);
        }    
    }
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        new MySQLConnection();
        loadDataToChat();       
    }     
}
