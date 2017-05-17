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
import javafx.scene.control.Label;
import javafx.scene.control.MenuButton;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleButton;
import main.java.engine.Core;
import main.java.implement.Main;
import main.java.utils.Consts;

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
    @FXML private MenuButton gameChoice;
    @FXML private Label labelPlayer2;
    @FXML private TextField namePlayer1;
    @FXML private TextField namePlayer2;
    @FXML private ToggleButton easyButton;
    @FXML private ToggleButton normalButton;
    @FXML private ToggleButton hardButton;
    @FXML private ToggleButton choicePVP;
    @FXML private ToggleButton choicePVAI;
    
    public NewGameScreenController() {
    }
    
    @FXML
    public void handleLaunchGameClick(){
        Core core;
        if(easyButton.isVisible()){
            if(easyButton.isSelected()){
                core = new Core(Consts.PVAI, Consts.EASY);
            }
            else if(normalButton.isSelected()){
                core = new Core(Consts.PVAI, Consts.MEDIUM);
            }
            else{
                core = new Core(Consts.PVAI, Consts.HARD);
            }
        }else{
            core = new Core(Consts.PVP, Consts.EASY);
        }
        core.getPlayers()[0].setName(namePlayer1.getText());
        core.getPlayers()[1].setName(namePlayer2.getText());
        main.showGameScreen(core);
        
    }
    
    @FXML
    public void handleChoiceButtonPVP(){
        choicePVP.setSelected(true);
        labelPlayer2.setVisible(true);
        namePlayer2.setVisible(true);
        easyButton.setVisible(false);
        normalButton.setVisible(false);
        hardButton.setVisible(false);
        
    }
    
    @FXML
    public void handleChoiceButtonPVAI(){
        choicePVAI.setSelected(true);
        labelPlayer2.setVisible(false);
        namePlayer2.setVisible(false);
        easyButton.setVisible(true);
        normalButton.setVisible(true);
        hardButton.setVisible(true);
    }
    
    
    @FXML
    public void handleChoicePVP(){
        choicePVP.setSelected(true);
        labelPlayer2.setVisible(true);
        namePlayer2.setVisible(true);
        easyButton.setVisible(false);
        normalButton.setVisible(false);
        hardButton.setVisible(false);
        
    }
    
    @FXML
    public void handleChangeChoicePVIA(){

        labelPlayer2.setVisible(false);
        namePlayer2.setVisible(false);
        easyButton.setVisible(true);
        normalButton.setVisible(true);
        hardButton.setVisible(true);
        
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
