/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package main.java.controller;

import java.io.IOException;
import java.net.URL;
import java.util.Arrays;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ChoiceBox;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;

import main.java.engine.OptionManager;
import main.java.implement.Main;
import main.java.utils.Consts;
import org.xml.sax.SAXException;

/**
 * FXML Controller class
 *
 * @author gontardb
 */
public class OptionsScreenController implements Initializable {
    @FXML private ChoiceBox<String> choiceResolution;
    @FXML private CheckBox fullScreen;
    @FXML private CheckBox help;
    @FXML private CheckBox animations;
    @FXML private CheckBox grid;
    private Main main;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    
    
    @FXML 
    public void handleApplyClick() throws SAXException, IOException, ParserConfigurationException, TransformerException{
        OptionManager.modifyOptions(choiceResolution.getSelectionModel().getSelectedIndex(), fullScreen.isSelected(), help.isSelected(),animations.isSelected(), grid.isSelected());
        main.showMainMenu();
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
        resolutions.addAll(Arrays.asList(Consts.RESOLUTIONS));
        choiceResolution.setItems(resolutions);
        
        choiceResolution.getSelectionModel().select(OptionManager.getResolution());
        if(OptionManager.isFullscreen()){
            fullScreen.setSelected(true);
        }
        if(OptionManager.isHelpEnable()){
            help.setSelected(true);
        }
        if(OptionManager.isAnimationsEnable()){
            animations.setSelected(true);
        }
        if(OptionManager.isGridEnable()){
            grid.setSelected(true);
        }
    }
    
    public void setMainApp(Main mainApp) {
        this.main = mainApp;    
    }
    
}
