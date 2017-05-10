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
import javafx.scene.control.Alert;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import main.java.implement.Main;
import main.java.model.Core;
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
    
    public NewGameScreenController() {
    }
    
    @FXML
    public void handleLaunchGameClick(){
        Core core = new Core(2);
        /*
        switch(gameChoice.getText()){
            case Consts.PVP_STRING :
                core = new Core(Consts.PVP_INT);
                break;
            case Consts.PVIA_STRING : 
                core = new Core(Consts.PVIA_INT);
                break;
            default :
                break;
            
        }*/
        //core.getCurrentState().getPlayers()[0].setName();
        Dialog d = new Alert(Alert.AlertType.INFORMATION);
        d.setTitle("Information");
        d.setContentText("Pas encore implémenté");
        d.show();
    }
    
    
    @FXML
    public void handleChoicePVP(){
        gameChoice.setText("Joueur contre joueur");
        labelPlayer2.setVisible(true);
        namePlayer2.setVisible(true);
    }
    
    @FXML
    public void handleChangeChoicePVIA(){
        gameChoice.setText("Joueur contre ordinateur");
        labelPlayer2.setVisible(false);
        namePlayer2.setVisible(false);
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
    }
}
