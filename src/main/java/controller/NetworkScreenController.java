/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package main.java.controller;

import java.net.URL;
import java.util.Random;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import main.java.engine.Core;
import main.java.implement.Main;
import main.java.utils.Consts;

/**
 * FXML Controller class
 *
 * @author gontardb
 */
public class NetworkScreenController implements Initializable {
    
    
    private Main main;
    
    
    public void setMainApp(Main mainApp) {
        this.main = mainApp;  
    }

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    
    
     @FXML
    public void handleLaunchGameClick(){
         System.out.println("click");
        
    }
    
    
    @FXML 
    public void handleMenuClick(){
        main.showMainMenu();
    }
}
