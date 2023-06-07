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
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.PieChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;

/**
 *
 * @author Bakht Zada
 */
public class FXMLDocumentController implements Initializable {
   //top left close button that close the window
    @FXML
    private Button dashboard_btn;
    //home button
    @FXML
    private Button home_btn;
    //piechart for student fee
    @FXML
    private PieChart Fee_pieChart; 
    // piechart for salary
    @FXML
    private PieChart Salary_PieChart;
    //Home Window is a window that apear by clicking on the home button
    @FXML
    private AnchorPane HomeWindow;
    //Main window is a window that hold all the buttons and home window as well.
    @FXML
    private AnchorPane MainWindow;
    
    @FXML
    private Text total_students;
    
    @FXML
    private Text female_students;

    @FXML
    private Text male_students;

    @FXML
    private Text total_stuff;
    
    @FXML
    private Text user_name;
    
    @FXML
    private BarChart<String, Number> total_fee_details;
    
    private int totalStudents;
    
    private int currentYear;
    
    @FXML
    private Label schoolName;

    //Object of the StudentEntryForm
    Student_Entry_formController studentEntryForm = new Student_Entry_formController();
    //Object of the teacherEntryRecord
    TeacherEntryRecordController teacherEntryForm = new TeacherEntryRecordController();
    //Object of the addClass
    AddClassController addclass = new  AddClassController();
    //object of the promotioncontroller clas
    PromotionController promotion = new PromotionController();
    //object of the SetFeeController class
    SetFeeController setFee = new SetFeeController();
    //object of the EnterFeeController class
    EnterFeeController enterFee = new EnterFeeController();
    //object of the PayPromotionPayController class
    PayPromotionFeeController payPromotionFee = new PayPromotionFeeController();
    //Object of the StudentInfoController Class
    StudentInfoController studentInfo = new StudentInfoController();
    //Object of the PayTeacherSalary 
    PayTeacherSalaryController paySalary = new PayTeacherSalaryController();
    //Object of the SetStaffSalaryController
    SetStaffSalaryController setSalary = new SetStaffSalaryController();
    // Object of the feeDetialsController 
    FeeDetialsController feeDetails = new FeeDetialsController();
    // Object of the classInfoController 
    ClassesInfoController classInfo = new ClassesInfoController();
    //object of the teacherDetailController
    TeacherDetialController teacher_detail = new TeacherDetialController();
    //object of the ViewTeacherSalary
    ViewTeacherSalaryController viewTeacherDetial = new ViewTeacherSalaryController();
    //object of the unpaidFee controller
    UnpaidFeeController unpaidFee = new UnpaidFeeController();
    //object of the setting controller
    SettingController setting = new SettingController();
    //object of the AddSubjectController
    AddSubjectController addSubject = new AddSubjectController();
    MarksDetailsController marksDetails = new MarksDetailsController();
    SchoolNameController schoolNaming = new SchoolNameController();
    private PreparedStatement prestate;
    private ResultSet rs;
    
    public static String u_name;
    private static Stage primaryStage;
    private void setPrimaryStage(Stage stage){
        this.primaryStage = stage;
    }

    public static Stage getPrimaryStage() {
        return primaryStage;
    }
    public void loadHomeWindow() {
        try {
            Stage stage = new Stage();
            stage.setTitle("");
            //stage.setResizable(false);
            stage.setMaximized(true);
            Parent root = FXMLLoader.load(getClass().getResource("FXMLDocument.fxml"));
            stage.getIcons().add(new Image(getClass().getResourceAsStream("Icons/brick.png")));
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
            setPrimaryStage(stage);
        } catch (IOException ex) {
            ExceptionHandling.showException(ex);
        }
    }
    //This code Handle the action of the home's buttons
    @FXML
    private void handleButtonAction(ActionEvent event) throws IOException {
        if(event.getTarget() == dashboard_btn) {
           HomeWindow.setVisible(false);
           MainWindow.setVisible(true);
           totalStudents();
           pieChartData();
           chartData();
        }
        if(event.getTarget() == home_btn) {
            HomeWindow.setVisible(true);
        }
    }
    public void openStudentRecordForm() {
        try {
            studentEntryForm.openStudentEntryRecord();
        } catch (IOException ex) {
            ExceptionHandling.showException(ex);
        }
    }
    public void openTeacherRecordForm() {
        try {
            teacherEntryForm.openTeacherEntryRecord();
        } catch (IOException ex) {
            ExceptionHandling.showException(ex);
        }  
    }
    public void openPromotion() {
        try {
            promotion.openPromotion();
        } catch (IOException ex) {
            ExceptionHandling.showException(ex);
        }
    }
    public void openAddClassForm() {
        try {
            addclass.openAddClass();
        } catch (IOException ex) {
           ExceptionHandling.showException(ex);
        }
    }
    
    public void openSetFee(){
        try {
            setFee.loadFeeForm();
        } catch (IOException ex) {
           ExceptionHandling.showException(ex);
        }
    }
    public void openEnterFee(){
        enterFee.enterFee();
    }
    public void openPromotionFee(){
        payPromotionFee.loadPayPromotionFee();
    }
    public void openStudentInfo() {
        studentInfo.loadStudentInfo();
    }
    public void openPayTeacherSalary() {
        paySalary.openPayTeacherSalary();
    }
    public void openSetSalary(){
        setSalary.loadSetSalaryForm();
    }
    public void openFeeDetials() {
        feeDetails.openFeeDetails();
    }
    public void openClassInfo() {
        classInfo.openClassesInfo();
    }
    public void openTeacherDetail() {
        teacher_detail.openTeacherDetial();
    }
    public void openVeiwTeacherSalary() {
        viewTeacherDetial.loadTeacherSalary();
    }
    public void openUnpaidFee() {
        unpaidFee.loadPaidFee();
    }
    public void openSetting() {
        setting.loadSetting();
    }
    public void loadAddSubject() throws IOException{
        addSubject.openAddSubject();
    }
    public void loadMarksDetails() throws IOException {
        marksDetails.openMarksDetails();
    }
    public void loadSchoolName() {
        schoolNaming.loadSchoolName();
    }
    private void totalStudents() {
        new MySQLConnection();
        try {
            String totalstudentQuery = "Select count(*) AS rowCount from student";
            String totalFemalStudents = "SELECT count(*) AS rowCount from student where gender = 'Female'";
            String totalMaleStudents = "SELECT count(*) AS rowCount from student where gender = 'Male'";
            String totalStaff = "SELECT count(*) AS rowCount from teacher";
            prestate = databaseconnection.MySQLConnection.conn.prepareStatement(totalstudentQuery);
            rs = prestate.executeQuery();
            if(rs.next()){
                total_students.setText(String.valueOf(rs.getInt("rowCount")));
            }
            prestate = databaseconnection.MySQLConnection.conn.prepareStatement(totalFemalStudents);
            rs = prestate.executeQuery();
            if(rs.next()){
                female_students.setText(String.valueOf(rs.getInt("rowCount")));
            }
            prestate = databaseconnection.MySQLConnection.conn.prepareStatement(totalMaleStudents);
            rs = prestate.executeQuery();
            if(rs.next()){
                male_students.setText(String.valueOf(rs.getInt("rowCount")));
            }
            prestate = databaseconnection.MySQLConnection.conn.prepareStatement(totalStaff);
            rs = prestate.executeQuery();
            if(rs.next()){
                total_stuff.setText(String.valueOf(rs.getInt("rowCount")));
            }
            rs.close();
            prestate.close();
            MySQLConnection.conn.close();
        } catch (SQLException ex) {
            Logger.getLogger(FXMLDocumentController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    private void pieChartData() {
        new MySQLConnection();
        try {
            totalStudents = Integer.parseInt(total_students.getText());
            ObservableList<PieChart.Data> feeData = FXCollections.observableArrayList();
            String feePaidQuery = "Select count(distinct student_id) AS feepaid from fee_payment";
            prestate = databaseconnection.MySQLConnection.conn.prepareStatement(feePaidQuery);
            rs = prestate.executeQuery();
            if(rs.next()){
                feeData.add(new PieChart.Data("Paid Fee", rs.getInt("feePaid")));  
                feeData.add(new PieChart.Data("Unpaid Fee", (totalStudents - rs.getInt("feepaid")))); 
            }
            Fee_pieChart.setData(feeData);
            Fee_pieChart.setLegendVisible(false);
            Fee_pieChart.getData().stream().forEach((temp) -> {
                Tooltip tooltip = new Tooltip(String.valueOf(temp.getName()+" "+temp.getPieValue()));
                Tooltip.install(temp.getNode(), tooltip);
            });
            //Salay PieChart Data
            ObservableList<PieChart.Data> salaryData = FXCollections.observableArrayList();
            String paidSalaryQuery = "Select count(distinct teacher_id) AS paidSalary from pay_salary";
            prestate = databaseconnection.MySQLConnection.conn.prepareStatement(paidSalaryQuery);
            rs = prestate.executeQuery();
            if(rs.next()){
                salaryData.add(new PieChart.Data("Paid Salary", rs.getInt("paidSalary")));
                salaryData.add(new PieChart.Data("UnPaid Salary", (Integer.parseInt(total_stuff.getText())-rs.getInt("paidSalary"))));
            }
            Salary_PieChart.setData(salaryData);
            //Salary_PieChart.setLegendVisible(false);
            Salary_PieChart.getData().stream().forEach((temp) -> {
                Tooltip tooltip = new Tooltip(String.valueOf(temp.getName()+" "+temp.getPieValue()));
                Tooltip.install(temp.getNode(), tooltip);
            });
            rs.close();
            prestate.close();
            MySQLConnection.conn.close();
        } catch (SQLException ex) {
            ExceptionHandling.showException(ex);
        }
    }
    
    private void chartData() {
        new MySQLConnection();
        total_fee_details.getData().clear();
        XYChart.Series totalfee  = new XYChart.Series<>();
        XYChart.Series remainingfee  = new XYChart.Series<>();
        totalStudents = Integer.parseInt(total_students.getText());

        try {
            //seting data to bar chart
            String janQ = "Select count(*) As fee_collected from fee_payment where year = "+currentYear+" and  month = 'January'";
            String febQ = "Select count(*) As fee_collected from fee_payment where year = "+currentYear+" and month = 'February'";
            String marQ = "Select count(*) As fee_collected from fee_payment where year = "+currentYear+" and month = 'March'";
            String aprQ = "Select count(*) As fee_collected from fee_payment where year = "+currentYear+" and month = 'April'";
            String mayQ = "Select count(*) As fee_collected from fee_payment where year = "+currentYear+" and month = 'May'";
            String junQ = "Select count(*) As fee_collected from fee_payment where year = "+currentYear+" and month = 'June'";
            String julQ = "Select count(*) As fee_collected from fee_payment where year = "+currentYear+" and month = 'July'";
            String augQ = "Select count(*) As fee_collected from fee_payment where year = "+currentYear+" and month = 'August'";
            String sepQ = "Select count(*) As fee_collected from fee_payment where year = "+currentYear+" and month = 'September'";
            String octQ = "Select count(*) As fee_collected from fee_payment where year = "+currentYear+" and month = 'October'";
            String novQ = "Select count(*) As fee_collected from fee_payment where year = "+currentYear+" and month = 'November'";
            String decQ = "Select count(*) As fee_collected from fee_payment where year = "+currentYear+" and month = 'December'";
            prestate = databaseconnection.MySQLConnection.conn.prepareStatement(janQ);
            rs = prestate.executeQuery();
            if(rs.next()){
                totalfee.getData().add(new XYChart.Data<>("Jan",rs.getInt("fee_collected")));
                remainingfee.getData().add(new XYChart.Data<>("Jan",(totalStudents - rs.getInt("fee_collected"))));
            }
            prestate = databaseconnection.MySQLConnection.conn.prepareStatement(febQ);
            rs = prestate.executeQuery();
            if(rs.next()){
                totalfee.getData().add(new XYChart.Data<>("Feb",rs.getInt("fee_collected")));
                remainingfee.getData().add(new XYChart.Data<>("Feb",(totalStudents - rs.getInt("fee_collected"))));
            }
               prestate = databaseconnection.MySQLConnection.conn.prepareStatement(marQ);
            rs = prestate.executeQuery();
            if(rs.next()){
                totalfee.getData().add(new XYChart.Data<>("Mar",rs.getInt("fee_collected")));
                remainingfee.getData().add(new XYChart.Data<>("Mar",(totalStudents - rs.getInt("fee_collected"))));
            }
            prestate = databaseconnection.MySQLConnection.conn.prepareStatement(aprQ);
            rs = prestate.executeQuery();
            if(rs.next()){
                totalfee.getData().add(new XYChart.Data<>("Apr",rs.getInt("fee_collected")));
                remainingfee.getData().add(new XYChart.Data<>("Apr",(totalStudents - rs.getInt("fee_collected"))));
            }
            prestate = databaseconnection.MySQLConnection.conn.prepareStatement(mayQ);
            rs = prestate.executeQuery();
            if(rs.next()){
                totalfee.getData().add(new XYChart.Data<>("May",rs.getInt("fee_collected")));
                remainingfee.getData().add(new XYChart.Data<>("May",(totalStudents - rs.getInt("fee_collected"))));
            }
            prestate = databaseconnection.MySQLConnection.conn.prepareStatement(junQ);
            rs = prestate.executeQuery();
            if(rs.next()){
                totalfee.getData().add(new XYChart.Data<>("Jun",rs.getInt("fee_collected")));
                remainingfee.getData().add(new XYChart.Data<>("Jun",(totalStudents - rs.getInt("fee_collected"))));
            }
            prestate = databaseconnection.MySQLConnection.conn.prepareStatement(julQ);
            rs = prestate.executeQuery();
            if(rs.next()){
                totalfee.getData().add(new XYChart.Data<>("Jul",rs.getInt("fee_collected")));
                remainingfee.getData().add(new XYChart.Data<>("Jul",(totalStudents - rs.getInt("fee_collected"))));
            }
            prestate = databaseconnection.MySQLConnection.conn.prepareStatement(augQ);
            rs = prestate.executeQuery();
            if(rs.next()){
                totalfee.getData().add(new XYChart.Data<>("Aug",rs.getInt("fee_collected")));
                remainingfee.getData().add(new XYChart.Data<>("Aug",(totalStudents - rs.getInt("fee_collected"))));
            }
            prestate = databaseconnection.MySQLConnection.conn.prepareStatement(sepQ);
            rs = prestate.executeQuery();
            if(rs.next()){
                totalfee.getData().add(new XYChart.Data<>("Sep",rs.getInt("fee_collected")));
                remainingfee.getData().add(new XYChart.Data<>("Sep",(totalStudents - rs.getInt("fee_collected"))));
            }
            prestate = databaseconnection.MySQLConnection.conn.prepareStatement(octQ);
            rs = prestate.executeQuery();
            if(rs.next()){
                totalfee.getData().add(new XYChart.Data<>("Oct",rs.getInt("fee_collected")));
                remainingfee.getData().add(new XYChart.Data<>("Oct",(totalStudents - rs.getInt("fee_collected"))));
            }
            prestate = databaseconnection.MySQLConnection.conn.prepareStatement(novQ);
            rs = prestate.executeQuery();
            if(rs.next()){
                totalfee.getData().add(new XYChart.Data<>("Nov",rs.getInt("fee_collected")));
                remainingfee.getData().add(new XYChart.Data<>("Nov",(totalStudents - rs.getInt("fee_collected"))));
            }
            prestate = databaseconnection.MySQLConnection.conn.prepareStatement(decQ);
            rs = prestate.executeQuery();
            if(rs.next()){
                totalfee.getData().add(new XYChart.Data<>("Dec",rs.getInt("fee_collected")));
                remainingfee.getData().add(new XYChart.Data<>("Dec",(totalStudents - rs.getInt("fee_collected"))));
            }
            totalfee.setName("Fee Paid Students");
            total_fee_details.getData().add(totalfee);
            remainingfee.setName("Remaining Students");
            total_fee_details.getData().add(remainingfee);
            total_fee_details.setTitle("Fee piad and remaining students in each month for the year "+currentYear+"");
            
            
            rs.close();
            prestate.close();
            MySQLConnection.conn.close();
        } catch (SQLException ex) {
            ExceptionHandling.showException(ex);
        }
  
    }
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        try {
            new MySQLConnection();
            prestate = MySQLConnection.conn.prepareStatement("select *from school");
            rs = prestate.executeQuery();
            if(rs.next()) {
                schoolName.setText(rs.getString("name"));
            }
            //initializing current year
            currentYear = Calendar.getInstance().get(Calendar.YEAR);
            System.out.println(currentYear);
            //Setting the Mainwindow as a defalt window
            MainWindow.setVisible(true);
            HomeWindow.setVisible(false);
            user_name.setText(u_name);
            //Calling the functions
            totalStudents();
            chartData();
            pieChartData();
        } catch (SQLException ex) {
            Logger.getLogger(FXMLDocumentController.class.getName()).log(Level.SEVERE, null, ex);
        }
            
        
    }    
    
}
