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
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleButton;
import javafx.scene.image.Image;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.paint.ImagePattern;
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
    private int state = Consts.WHITE;
    @FXML private Label hostLabel;
    @FXML private TextField hostName;
    @FXML private Label playerLabel;
    @FXML private TextField playerName;
    @FXML private ToggleButton choiceHost;
    @FXML private ToggleButton choiceClient;
    @FXML private ToggleButton colorHost;
    
    
    
    
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
    	
    	Core core = new Core(-1, -1);
    	String host = choiceClient.isSelected()?hostName.getText():null;
    	int mode = choiceHost.isSelected()?(state==Consts.WHITE?Consts.PVEX:Consts.EXVP):-1;
    	core.connect(host, mode, playerName.getText());
    	main.showGameScreen(core);
    }
    
    @FXML
    public void handleHostClick(){
        choiceHost.setSelected(true);
        hostLabel.setVisible(false);
        hostName.setVisible(false);
        
    }
    
    @FXML
    public void handleClientClick(){
        choiceClient.setSelected(true);
        hostLabel.setVisible(true);
        hostName.setVisible(true);
    }
    
    @FXML
    public void handleColorHostClick(){
        state = (state+1)%3;
        majColorButton();
    }
    
    public void majColorButton(){
        switch(state){
            case Consts.WHITE:
                colorHost.setBackground(new Background(new BackgroundFill(new ImagePattern(new Image(getClass().getClassLoader().getResource("main/resources/img/misc/whitestart.png").toString())), CornerRadii.EMPTY, Insets.EMPTY)));
                break;
            case Consts.BLACK:
                colorHost.setBackground(new Background(new BackgroundFill(new ImagePattern(new Image(getClass().getClassLoader().getResource("main/resources/img/misc/blackstart.png").toString())), CornerRadii.EMPTY, Insets.EMPTY)));
                break;
            case Consts.RANDOM:
                colorHost.setBackground(new Background(new BackgroundFill(new ImagePattern(new Image(getClass().getClassLoader().getResource("main/resources/img/misc/rngstart.png").toString())), CornerRadii.EMPTY, Insets.EMPTY)));
                break;
            default:
                break;
        }
    }
     
    @FXML 
    public void handleMenuClick(){
        main.showMainMenu();
    }
}
