package main.java.view;


import javafx.fxml.FXML;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;
import main.java.implement.Main;


/**
 * FXML Controller class
 *
 * @author gontardb
 */
public class RulesScreenController implements Initializable {

    /**
     * Initializes the controller class.
     */
    private Main main;
    @FXML private Label titleRule;
    @FXML private ImageView imageRule;
    @FXML private Text textRule;
    
    public RulesScreenController() {
    }

    
    @FXML 
    public void handleMenuClick(){
        main.showMainMenu();
    }
    
    @FXML
    public void initialize(URL url, ResourceBundle rb) {
        this.handleGoalGame();
    }    
    
    public void setMainApp(Main mainApp) {
        this.main = mainApp;    
    }
    
   public void handleGoalGame(){
       titleRule.setText("Lol");
       imageRule.setImage(new Image("goalGame.png"));
       textRule.setText("pohfvljhqb vhzb vlq cljhqbvoHBV Q:,N JEHRBVO HLJ CJHREFBV  FLJH BVPAEHB LQND QOHRB");
   }
   
   public void handleGamePlay(){
   
   }
   
   public void handlePiecePositionings(){
   
   }
   
   public void handleMoveQueen(){
   
   }
   
   public void handleMoveGrassHopper(){
   
   }
   
   public void handleMoveBeetle(){
   
   }
   
   public void handleMoveSpider(){
   
   }
   
   public void handleMoveAnt(){
   
   }
   
}
