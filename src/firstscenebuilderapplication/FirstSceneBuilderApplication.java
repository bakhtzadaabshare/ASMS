/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package firstscenebuilderapplication;

import javafx.application.Application;
import javafx.stage.Stage;

/**
 *
 * @author Bakht Zada
 */
public class FirstSceneBuilderApplication extends Application {
    LoginFormController load = new LoginFormController();
    @Override
    public void start(Stage stage) throws Exception {
        load.loadLoginForm();
    }
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
    
}
