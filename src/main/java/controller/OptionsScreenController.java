/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package main.java.controller;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import main.java.implement.Main;

/**
 * FXML Controller class
 *
 * @author gontardb
 */
public class OptionsScreenController implements Initializable {
    
    private Main main;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    
    
    @FXML 
    public void handleApplyClick(){
        System.out.println("Pas encore implémenté");
    }
    
    @FXML 
    public void handleMenuClick(){
        main.showMainMenu();
    }
    
    public void setMainApp(Main mainApp) {
        this.main = mainApp;    
    }
    
}
