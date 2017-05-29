package main.java.controller;


import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import javafx.fxml.FXML;
import java.net.URL;
import java.util.Iterator;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.Initializable;
import javafx.scene.control.ListView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderStroke;
import javafx.scene.layout.BorderStrokeStyle;
import javafx.scene.layout.BorderWidths;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
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
    @FXML private Pane paneSaveImage;
    @FXML private HBox myBox;
   
    public LoadGameScreenController() {
    }
   
    @FXML
    public void handleLaunchGameClick(){
        if(saveList.getSelectionModel().getSelectedItems() != null){
            String saveName = (String)saveList.getSelectionModel().getSelectedItems().get(0);
            if(saveName != null){
                core.load(saveName);
                main.showGameScreen(core,true);
            }
        }
    }
   
    @FXML
    public void handleMenuClick(){
        main.showMainMenu();
    }
    
    @FXML
    public void handleDeleteClick(){
        if(saveList.getSelectionModel().getSelectedItems() != null){
            String name = (String)saveList.getSelectionModel().getSelectedItems().get(0);
            File fileToDelete = new File("Hive_init/Hive_save_images/"+name+".png");
            if (fileToDelete.exists()) {
               fileToDelete.delete();     
            }
            fileToDelete = new File("Hive_init/Hive_save/"+name);
            if (fileToDelete.exists()) {
               fileToDelete.delete();     
            }
        }
        initSaves();
    }

   
    @FXML
    public void initialize(URL url, ResourceBundle rb) {
    }   
    
    public void initSaves(){
        saveList.getItems().remove(0,saveList.getItems().size());
        core = new Core(0, 0);
        List<String> saves = core.load();
        Iterator it = saves.iterator();
        String name;
        while(it.hasNext()){
            name = (String)it.next();
            System.out.println(name.substring(0, 4));
            if(!name.substring(0, 4).equals(".nfs")){
                saveList.getItems().add(name);
            }
        }
        saves.clear();
    }
   
    public void initGameList(){
       
        initSaves();
        saveList.getSelectionModel().selectedItemProperty().addListener(new ChangeListener() {

            @Override
            public void changed(ObservableValue observable, Object oldValue, Object newValue) {
               
                String imageName = (String)saveList.getSelectionModel().getSelectedItems().get(0);
                try {
                    //paneSaveImage.setBackground(new Background(new BackgroundImage(new Image(new FileInputStream(new File("Hive_init/Hive_save_images/"+imageName+".png"))), BackgroundRepeat.REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.CENTER, backgroundSize)));
                    if(imageName != null){
                        FileInputStream fis = new FileInputStream(new File("Hive_init/Hive_save_images/"+imageName+".png"));
                        saveImage.setImage(new Image(fis));
                        fis.close();
                    }
                    else{
                        saveImage.setImage(null);
                    }
                } catch (FileNotFoundException ex) {
                    Logger.getLogger(LoadGameScreenController.class.getName()).log(Level.SEVERE, null, ex);
                } catch (IOException ex) {
                    Logger.getLogger(LoadGameScreenController.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
        saveImage.fitWidthProperty().bind(paneSaveImage.widthProperty());
        saveImage.fitHeightProperty().bind(paneSaveImage.heightProperty());

        myBox.setBorder(new Border(new BorderStroke(Color.BLACK, BorderStrokeStyle.SOLID, new CornerRadii(10), new BorderWidths(3))));
    }
   
   
    public void setMainApp(Main mainApp) {
        this.main = mainApp;  
    }
}