
package firstscenebuilderapplication;

import databaseconnection.MySQLConnection;
import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author Bakht Zada
 */
public class LoginFormController implements Initializable {
    @FXML
    private AnchorPane register_window;

    @FXML
    private AnchorPane login_window;
    
    @FXML
    private Button open_login;
    
    @FXML
    private TextField user_name;

    @FXML
    private PasswordField password;
    
    @FXML
    private TextField new_user;

    @FXML
    private PasswordField enter_password;

    @FXML
    private PasswordField re_enter_password;

    @FXML
    private TextField existed_user;
    
    @FXML
    private Text show_message;
    
    @FXML
    private Button register_btn;
    
    //object of FXMLDocumentController
    FXMLDocumentController homeWindow = new FXMLDocumentController();
    
    private PreparedStatement prestate;
    private ResultSet rs;
    private int login_status;
    private int password_match;
    private String existed_u;
    private static Stage stage;
    
    public void loadLoginForm() throws Exception {
        stage = new Stage();
        stage.setTitle("");
        //stage.initStyle(StageStyle.TRANSPARENT);
        //stage.setResizable(false);
        stage.setMaximized(true);
        stage.getIcons().add(new Image(getClass().getResourceAsStream("Icons/brick.png")));
        Parent root = FXMLLoader.load(getClass().getResource("LoginForm.fxml"));
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }
    
    public void openRegisterWindow() {
        login_window.setVisible(false);
        register_window.setVisible(true);
        //register_btn.setVisible(false);
        open_login.setVisible(true);

    }
    public void openLoginWindow() {
        register_window.setVisible(false);
        login_window.setVisible(true);
        open_login.setVisible(false);
        //register_btn.setVisible(true);
    }
    public void loginAction() {
        new MySQLConnection();
        try {
        String query = "Select * from login";
        prestate = databaseconnection.MySQLConnection.conn.prepareStatement(query);
        rs = prestate.executeQuery();
        if(user_name.getText().isEmpty() || password.getText().isEmpty()){
            Alert alert = new Alert(AlertType.INFORMATION);
            alert.setTitle("Enter Name and Password");
            alert.setContentText("Please enter user name and password then click on login button");
            alert.show();
        }
        else {
                String name = user_name.getText();
                String pass = password.getText();
                while(rs.next()){
                    if(name.equals(rs.getString("user_name")) && pass.equals(rs.getString("password"))){
                        FXMLDocumentController.u_name = name;
                        homeWindow.loadHomeWindow();
                        login_status = 0;
                        stage.close();
                        break;
                    }
                    else {
                        login_status = 1;
                    }
                }
                if(login_status == 1) {
                    Alert alert = new Alert(AlertType.ERROR);
                    alert.setTitle("Login Field");
                    alert.setContentText("User name or password is incorrect. Please try gain");
                    alert.show(); 
                }
        }
        rs.close();
        prestate.close();
        MySQLConnection.conn.close();
        } catch (SQLException ex) {
                ExceptionHandling.showException(ex);
        } 
    }
    public void registerUser() {
        if(existed_user.getText().isEmpty() || new_user.getText().isEmpty() || 
                enter_password.getText().isEmpty() || re_enter_password.getText().isEmpty()) {
            Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle("Fill the form");
            alert.setContentText("Please fill from completely then click on sign up button");
            alert.show();
        }
        else {
            try {
                String existedUser = existed_user.getText();
                prestate = databaseconnection.MySQLConnection.conn.prepareStatement("select user_name from login");
                rs = prestate.executeQuery();
                while(rs.next()){
                    existed_u = rs.getString("user_name"); 
                    if(existedUser.equals(existed_u) && password_match == 0){
                        String query = "insert into login(user_name, password) values(?,?)";
                        prestate = databaseconnection.MySQLConnection.conn.prepareStatement(query);
                        prestate.setString(1, new_user.getText());
                        prestate.setString(2,enter_password.getText());
                        prestate.execute();
                        Alert alert = new Alert(AlertType.INFORMATION);
                        alert.setTitle("Sign up succeded");
                        alert.setContentText("You are successfully registered");
                        alert.show();
                        break;
                    }
 
                }
                rs.close();
                prestate.close();
                if(!(existedUser.equals(existed_u) && password_match == 0)) {
                    Alert alert = new Alert(AlertType.ERROR);
                    alert.setTitle("Sign up field");
                    alert.setContentText("Password doesn't matched or no existed user found with name "+existed_user.getText()+"");
                    alert.show();  
                }
            } catch (SQLException ex) {
               ExceptionHandling.showException(ex);
            }
        }
    }
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        open_login.setVisible(false);
        new MySQLConnection();
        re_enter_password.textProperty().addListener(new ChangeListener<String>(){
        @Override
        public void changed(ObservableValue<? extends String> arg0, String oldPropertyValue, String newPropertyValue)
        {
            if(!(enter_password.getText().isEmpty() || re_enter_password.getText().isEmpty())){
                String pass1 = enter_password.getText();
                String pass2 = re_enter_password.getText();
                if(!pass1.equals(pass2)) {
                    show_message.setText("Password doesn't match!");
                    password_match = 1;
                }
                else {
                    password_match = 0;
                    show_message.setText("");
                }
                }
        }
        });
        
    }
}