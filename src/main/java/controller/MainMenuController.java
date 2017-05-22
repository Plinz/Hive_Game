/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package main.java.controller;

import javafx.fxml.FXML;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import main.java.implement.Main;

/**
 * FXML Controller class
 *
 * @author gontardb
 */
public class MainMenuController implements Initializable {

    /**
     * Initializes the controller class.
     */
    
    private Main main;
    
    public MainMenuController() {
    }
    
    @FXML 
    public void handleNewGameClick(){
        main.showNewGameScreen();
    }
    
    @FXML 
    public void handleLoadGameClick(){
        main.showLoadGameScreen();
    }
    
    @FXML 
    public void handleDisplayRulesClick(){
        main.showRulesScreen();
    }
   
    
    @FXML 
    public void handleNetworkClick(){
        main.showNetworkMenu();
    }
    
     @FXML 
    public void handleOptionsClick(){
        main.showOptionsScreen();
    }
    
    @FXML 
    public void handleLeaveClick(){
        Platform.exit();
    }
    
    @FXML
    public void initialize(URL url, ResourceBundle rb) {
    }    
    
    public void setMainApp(Main mainApp) {
        this.main = mainApp;  
    }
    
}
