/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package main.java.controller;


import java.io.File;
import javafx.fxml.FXML;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Dialog;
import javafx.scene.control.ListView;
import main.java.implement.Main;
import main.java.model.Core;

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
    private Core core;
    @FXML private ListView saveList;
    
    public LoadGameScreenController() {
    }
    
    @FXML
    public void handleLaunchGameClick(){
        if(saveList.getSelectionModel().getSelectedItems() != null){
            String saveName = (String)saveList.getSelectionModel().getSelectedItems().get(0);
            core.load(saveName);
        }
        main.showGameScreen(core);
    }
    
    @FXML 
    public void handleMenuClick(){
        main.showMainMenu();
    }

    
    @FXML
    public void initialize(URL url, ResourceBundle rb) {
    }    
    
    public void initGameList(){
        core = new Core(0, 0);
        List<String> saves = core.load();
        saveList.getItems().addAll(saves);
    }
    
    
    public void setMainApp(Main mainApp) {
        this.main = mainApp;   
    }
    
}
