/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package main.java.controller;


import javafx.fxml.FXML;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Dialog;
import main.java.implement.Main;

/**
 * FXML Controller class
 *
 * @author gontardb
 */
public class LoadGameScreenController implements Initializable {

    /**
     * Initializes the controller class.
     */
    
    private Main main;
    
    public LoadGameScreenController() {
    }
    
    @FXML
    public void handleLaunchGameClick(){
        Dialog d = new Alert(Alert.AlertType.INFORMATION);
        d.setTitle("Information");
        d.setContentText("Pas encore implémenté");
        d.show();
    }
    
    @FXML 
    public void handleMenuClick(){
        main.showMainMenu();
    }
    
    @FXML
    public void initialize(URL url, ResourceBundle rb) {
    }    
    
    public void setMainApp(Main mainApp) {
        this.main = mainApp;   
    }
    
}
