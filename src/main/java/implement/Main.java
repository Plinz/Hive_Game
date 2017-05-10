package main.java.implement;

import java.io.IOException;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class Main extends Application {

    private Stage primaryStage;

    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;
        this.primaryStage.setTitle("Hive_Game");
        showMainMenu();
        this.primaryStage.show();
    }

    public void showMainMenu() {
        try {
            // Load person overview.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(Main.class.getResource("/main/java/view/MainMenu.fxml"));
            AnchorPane personOverview = (AnchorPane) loader.load();
            System.out.println(personOverview.getChildren());
            
            Scene scene = new Scene(personOverview);
            primaryStage.setScene(scene);

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

    public static void main(String[] args) {
        launch(args);
    }
}