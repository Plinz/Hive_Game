/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package main.java.view;

import javafx.fxml.FXML;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.Initializable;
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
        System.out.println("Rules");
    }
   
    
    @FXML 
    public void handleTutorialClick(){
        System.out.println("Didact");
    }
    
     @FXML 
    public void handleOptionsClick(){
        System.out.println("Options");
    }
    
    @FXML 
    public void handleLeaveClick(){
        System.out.println("Leave");
    }
    
    @FXML
    public void initialize(URL url, ResourceBundle rb) {
    }    
    
    public void setMainApp(Main mainApp) {
        this.main = mainApp;  
    }
    
}
