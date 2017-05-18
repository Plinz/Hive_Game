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
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleButton;
import javafx.scene.image.Image;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.ImagePattern;
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
    private int state = Consts.WHITE_FIRST;
    
    @FXML private Label labelPlayer2;
    @FXML private TextField namePlayer1;
    @FXML private TextField namePlayer2;
    @FXML private ToggleButton easyButton;
    @FXML private ToggleButton normalButton;
    @FXML private ToggleButton hardButton;
    @FXML private ToggleButton choicePVP;
    @FXML private ToggleButton choicePVAI;
    @FXML private ToggleButton colorPlayer1;
    @FXML private ToggleButton colorPlayer2;
    @FXML private GridPane gridPlayer2;
    
    public NewGameScreenController() {
    }
    
    @FXML
    public void handleLaunchGameClick(){
        Core core;
        int iaDifficulty = 0;
        //Si on est en mode PVIA
        
        if(state == Consts.RANDOM)
        	state = (new Random()).nextInt(2);

        if(easyButton.isVisible()){
            String iaName;            
            if(easyButton.isSelected()){
            	iaName = "IA_Facile";
                iaDifficulty = Consts.EASY;
            }
            else if(normalButton.isSelected()){
            	iaName = "IA_Normale";
                iaDifficulty = Consts.MEDIUM;
            }
            else{
            	iaName = "IA_Difficile";
                iaDifficulty = Consts.HARD;
            }
            if(state == Consts.WHITE_FIRST){
                core = new Core(Consts.PVAI, iaDifficulty);
                core.getPlayers()[0].setName(namePlayer1.getText());
                core.getPlayers()[1].setName(iaName);
            }
            else {
                core = new Core(Consts.AIVP, iaDifficulty);
                core.getPlayers()[0].setName(iaName);
                core.getPlayers()[1].setName(namePlayer1.getText());
            }
            
        }
        //Sinon si on est en mode PVP
        else{
            core = new Core(Consts.PVP, Consts.EASY);
            if(state == Consts.WHITE_FIRST){
                core.getPlayers()[0].setName(namePlayer1.getText());
                core.getPlayers()[1].setName(namePlayer2.getText());
            }
            else{
                core.getPlayers()[0].setName(namePlayer2.getText());
                core.getPlayers()[1].setName(namePlayer1.getText());
            }
            
        }
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
        colorPlayer2.setVisible(true);
        gridPlayer2.setVisible(true);
    }
    
    @FXML
    public void handleChoiceButtonPVAI(){
        choicePVAI.setSelected(true);
        labelPlayer2.setVisible(false);
        namePlayer2.setVisible(false);
        easyButton.setVisible(true);
        normalButton.setVisible(true);
        hardButton.setVisible(true);
        colorPlayer2.setVisible(false);
        gridPlayer2.setVisible(false);
    }
    
    public void majColorButton(){
        switch(state){
            case Consts.WHITE_FIRST:
                colorPlayer1.setBackground(new Background(new BackgroundFill(new ImagePattern(new Image(getClass().getClassLoader().getResource("main/resources/img/misc/whitestart.png").toString())), CornerRadii.EMPTY, Insets.EMPTY)));
                colorPlayer2.setBackground(new Background(new BackgroundFill(new ImagePattern(new Image(getClass().getClassLoader().getResource("main/resources/img/misc/blackstart.png").toString())), CornerRadii.EMPTY, Insets.EMPTY)));
                break;
            case Consts.BLACK_FIRST:
                colorPlayer2.setBackground(new Background(new BackgroundFill(new ImagePattern(new Image(getClass().getClassLoader().getResource("main/resources/img/misc/whitestart.png").toString())), CornerRadii.EMPTY, Insets.EMPTY)));
                colorPlayer1.setBackground(new Background(new BackgroundFill(new ImagePattern(new Image(getClass().getClassLoader().getResource("main/resources/img/misc/blackstart.png").toString())), CornerRadii.EMPTY, Insets.EMPTY)));
                break;
            case Consts.RANDOM:
                colorPlayer1.setBackground(new Background(new BackgroundFill(new ImagePattern(new Image(getClass().getClassLoader().getResource("main/resources/img/misc/rngstart.png").toString())), CornerRadii.EMPTY, Insets.EMPTY)));
                colorPlayer2.setBackground(new Background(new BackgroundFill(new ImagePattern(new Image(getClass().getClassLoader().getResource("main/resources/img/misc/rngstart.png").toString())), CornerRadii.EMPTY, Insets.EMPTY)));
                break;
            default:
                break;
        }
    }
    
    @FXML
    public void handleColorClick(){
        state = (state+1)%3;
        majColorButton();
    }
    
    @FXML
    public void handleEasyAI(){
        easyButton.setSelected(true);
    }
    
    @FXML
    public void handleMediumAI(){
        normalButton.setSelected(true);
    }
    
    @FXML
    public void handleHardAI(){
        hardButton.setSelected(true);
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
