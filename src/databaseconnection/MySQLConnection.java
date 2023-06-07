/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package databaseconnection;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import javafx.scene.control.Alert;
/**
 *
 * @author Bakht Zada
 */
public class MySQLConnection {
      //Creating string for holding driver name in this case it is mysql 
    //private String sqldriver = "com.mysql.jdbc.Driver"; 
    //Defining the URL of the database
    private String database_url = "jdbc:h2:./database/spsg;IFEXISTS=TRUE";
    // Defining the username and password for the database
    //I Already created a database named "spsg" 
    //the username of the spsg is "root" and password is "naamikhoda"
    //private String username = "root";
    //private String password = "naamikhoda";
      /*
        * defining a connection object. A connection object is used for the connection
        * defining a statement object that is use to execute sql qurey
      */
    public static Connection conn;
    //A connstructor that initialize basic parameters for database connection
   public MySQLConnection(){
       /*
            **Register a JDBC driver.
            * JDBC driver can support hetrogenous database therefore we have to register a particular database first
       */
        try {
            //I already defined sqldriver string so I passed it to class
            //Class.forName(sqldriver);
            Class.forName("org.h2.Driver");
            //Opening connection with database
            conn = DriverManager.getConnection(database_url,"sa","");  
        } catch (ClassNotFoundException | SQLException ex) {
            Alert mymessage = new Alert(Alert.AlertType.ERROR);
            mymessage.setTitle("Error Occured");
            mymessage.setContentText(ex.toString());
            mymessage.show();
        }
      
    } 
}

