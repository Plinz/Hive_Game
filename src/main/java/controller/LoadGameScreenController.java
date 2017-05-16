package main.java.controller;


import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import javafx.fxml.FXML;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Dialog;
import javafx.scene.control.ListView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import main.java.implement.Main;
import main.java.engine.Core;

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
    @FXML private ImageView saveImage;
   
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
       
        saveList.getSelectionModel().selectedItemProperty().addListener(new ChangeListener() {

            @Override
            public void changed(ObservableValue observable, Object oldValue, Object newValue) {
               
                String imageName = (String)saveList.getSelectionModel().getSelectedItems().get(0);
                try {
                    saveImage.setImage(new Image(new FileInputStream(new File("Hive_save_images/"+imageName+".png"))));
                } catch (FileNotFoundException ex) {
                    Logger.getLogger(LoadGameScreenController.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
    }
   
   
    public void setMainApp(Main mainApp) {
        this.main = mainApp;  
    }
}