package main.java.implement;

import java.io.IOException;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;

import main.java.controller.GameScreenController;
import main.java.controller.LoadGameScreenController;
import main.java.controller.MainMenuController;
import main.java.controller.NewGameScreenController;
import main.java.controller.OptionsScreenController;
import main.java.controller.RulesScreenController;
import main.java.engine.Core;
import main.java.engine.OptionManager;

import org.xml.sax.SAXException;

public class Main extends Application {

    private Stage primaryStage;

    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;
        this.primaryStage.setTitle("Hive_Game");
        this.primaryStage.setScene(new Scene(new AnchorPane()));
        showMainMenu();
        this.primaryStage.show();
    }

    public void showMainMenu() {
        try {
            // Load person overview.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(Main.class.getResource("/main/java/view/MainMenu.fxml"));
            AnchorPane personOverview = (AnchorPane) loader.load();
            
            MainMenuController controller = loader.getController();
            controller.setMainApp(this);
            
            primaryStage.getScene().setRoot(personOverview);
            
            if(OptionManager.isFullscreen())
                primaryStage.setFullScreen(true);
            else
                primaryStage.setFullScreen(false);

            // Set person overview into the center of root layout.
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    public void showNewGameScreen(){
        try {
            // Load person overview.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(Main.class.getResource("/main/java/view/NewGameScreen.fxml"));
            AnchorPane personOverview = (AnchorPane) loader.load();
            
            NewGameScreenController controller = loader.getController();
            controller.setMainApp(this);
            
            primaryStage.getScene().setRoot(personOverview);
            if(OptionManager.isFullscreen())
                primaryStage.setFullScreen(true);
            else
                primaryStage.setFullScreen(false);
            

            // Set person overview into the center of root layout.
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    public void showLoadGameScreen(){
        try {
            // Load person overview.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(Main.class.getResource("/main/java/view/LoadGameScreen.fxml"));
            AnchorPane personOverview = (AnchorPane) loader.load();
            
            LoadGameScreenController controller = loader.getController();
            controller.setMainApp(this);
            controller.initGameList();
            
            primaryStage.getScene().setRoot(personOverview);
            if(OptionManager.isFullscreen())
                primaryStage.setFullScreen(true);
            else
                primaryStage.setFullScreen(false);

            // Set person overview into the center of root layout.
        } catch (IOException e) {
            e.printStackTrace();
        }
        
    }
    
    public void showRulesScreen(){
        try {
            // Load person overview.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(Main.class.getResource("/main/java/view/RulesScreen.fxml"));
            AnchorPane personOverview = (AnchorPane) loader.load();
            
            RulesScreenController controller = loader.getController();
            controller.setMainApp(this);
            
            primaryStage.getScene().setRoot(personOverview);
            if(OptionManager.isFullscreen())
                primaryStage.setFullScreen(true);
            else
                primaryStage.setFullScreen(false);

            // Set person overview into the center of root layout.
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    public void showGameScreen(Core c){
        try {
            // Load person overview.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(Main.class.getResource("/main/java/view/GameScreen.fxml"));
            AnchorPane personOverview = (AnchorPane) loader.load();
            
            GameScreenController controller = loader.getController();
            controller.initGame(this, c);
            
            primaryStage.getScene().setRoot(personOverview);
            if(OptionManager.isFullscreen())
                primaryStage.setFullScreen(true);
            else
                primaryStage.setFullScreen(false);

            // Set person overview into the center of root layout.
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
        
        public void showOptionsScreen(){
        try {
            // Load person overview.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(Main.class.getResource("/main/java/view/OptionsScreen.fxml"));
            AnchorPane personOverview = (AnchorPane) loader.load();
            
            OptionsScreenController controller = loader.getController();
            controller.setMainApp(this);
                
            controller.init();
            primaryStage.getScene().setRoot(personOverview);
            if(OptionManager.isFullscreen())
                primaryStage.setFullScreen(true);
            else
                primaryStage.setFullScreen(false);
            

            // Set person overview into the center of root layout.
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Returns the main stage.
     * @return
     */
    public Stage getPrimaryStage() {
        return primaryStage;
    }

    public static void main(String[] args) throws IOException, ParserConfigurationException, TransformerException, TransformerConfigurationException, SAXException {
        OptionManager.init();
        launch(args);
    }
}