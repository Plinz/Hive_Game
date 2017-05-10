/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package main.java.view;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.Initializable;
import main.java.implement.Main;

/**
 *
 * @author duvernet
 */
public class GameScreenController implements Initializable {
    
    private Main main;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
    }
    
    public void setMainApp(Main mainApp) {
        this.main = mainApp;    
    }
}
