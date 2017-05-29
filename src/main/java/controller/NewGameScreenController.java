/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package main.java.controller;


import java.net.URL;
import java.util.Random;
import java.util.ResourceBundle;
import java.util.function.UnaryOperator;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
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
    private int state = Consts.WHITE;
    
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
        int iaDifficulty = -1;
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
            if(state == Consts.WHITE){
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
            String player1 = "Anonyme1";
            String player2 = "Anonyme2";
            if(state == Consts.WHITE){
                if(!namePlayer1.getText().equals(""))
                    player1 = namePlayer1.getText();
                    
                if(!namePlayer2.getText().equals(""))
                    player2 = namePlayer2.getText();
                core.getPlayers()[0].setName(player1);
                core.getPlayers()[1].setName(player2);
            }
            else{
                if(!namePlayer1.getText().equals(""))
                    player1 = namePlayer1.getText();
                    
                if(!namePlayer2.getText().equals(""))
                    player2 = namePlayer2.getText();
                core.getPlayers()[0].setName(player2);
                core.getPlayers()[1].setName(player1);
            }
            
        }
        main.showGameScreen(core,false);
        
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
            case Consts.WHITE:
                colorPlayer1.setBackground(new Background(new BackgroundFill(new ImagePattern(new Image(getClass().getClassLoader().getResource("main/resources/img/misc/whitestart.png").toString())), CornerRadii.EMPTY, Insets.EMPTY)));
                colorPlayer2.setBackground(new Background(new BackgroundFill(new ImagePattern(new Image(getClass().getClassLoader().getResource("main/resources/img/misc/blackstart.png").toString())), CornerRadii.EMPTY, Insets.EMPTY)));
                break;
            case Consts.BLACK:
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
    public void handleClickInputPlayer1(){
        namePlayer1.clear();
    }
    @FXML 
    public void handleClickInputPlayer2(){
        namePlayer2.clear();
    }
    
    @FXML
    public void initialize(URL url, ResourceBundle rb) {
        UnaryOperator<TextFormatter.Change> filter = c -> {
            String proposedText = c.getControlNewText();
             if (proposedText.matches(".{0,15}")) {
                return c ;
            } else {
                return null ;
            }
        };
        namePlayer1.setTextFormatter(new TextFormatter<String>(filter));
        namePlayer2.setTextFormatter(new TextFormatter<String>(filter));
    }    
    
    public void setMainApp(Main mainApp) {
        this.main = mainApp;    
    }
    
    
}
