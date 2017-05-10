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
public class NewGameScreenController implements Initializable {

    /**
     * Initializes the controller class.
     */
    
    private Main main;
    
    public NewGameScreenController() {
    }
    
    @FXML 
    public void handleMenuClick(){
        System.out.println("Menu");
        main.showMainMenu();
    }
    
    @FXML
    public void initialize(URL url, ResourceBundle rb) {
    }    
    
    public void setMainApp(Main mainApp) {
        this.main = mainApp;
        // Add observable list data to the table     
    }
    
}
