/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package main.java.controller;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ChoiceBox;
import main.java.implement.Main;

/**
 * FXML Controller class
 *
 * @author gontardb
 */
public class OptionsScreenController implements Initializable {
    @FXML private ChoiceBox<String> choiceResolution;
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
    
    public void init(){
        initResolution();
    }
    
    public void initResolution(){
        ObservableList<String> resolutions = FXCollections.observableArrayList();
        resolutions.add("1270x720");
        choiceResolution.setItems(resolutions);
    }
    
    public void setMainApp(Main mainApp) {
        this.main = mainApp;    
    }
    
}
