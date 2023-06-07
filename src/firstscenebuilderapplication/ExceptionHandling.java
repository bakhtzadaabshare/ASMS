/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package firstscenebuilderapplication;

import javafx.scene.control.Alert;

/**
 *
 * @author Bakht Zada
 */
public class ExceptionHandling {
    public static void showException(Exception ex) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error Occurred");
        alert.setContentText(ex.toString());
        alert.show();
    }
}
