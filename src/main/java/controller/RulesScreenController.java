package main.java.controller;


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
    @FXML private ImageView imageTitleRule;
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
       imageTitleRule.setImage(new Image("/main/resources/img/rulesTitle/goalGame.png"));
       imageRule.setImage(new Image("/main/resources/img/rules/game.png"));
       textRule.setText("pohfvljhqb vhzb vlq cljhqbvoHBV Q:,N JEHRBVO HLJ CJHREFBV  FLJH BVPAEHB LQND QOHRB");
   }
   
   public void handleGamePlay(){
      imageTitleRule.setImage(new Image("/main/resources/img/rulesTitle/gamePlay.png"));
      imageRule.setImage(new Image("/main/resources/img/rules/game.png"));
      textRule.setText("pohfvljhqb vhzb vlq cljhqbvoHBV Q:,N JEHRBVO HLJ CJHREFBV  FLJH BVPAEHB LQND QOHRB");
   }
   
   public void handlePiecePositionings(){
      imageTitleRule.setImage(new Image("/main/resources/img/rulesTitle/PiecePositionings.png"));
      imageRule.setImage(new Image("/main/resources/img/rules/game.png"));
      textRule.setText("pohfvljhqb vhzb vlq cljhqbvoHBV Q:,N JEHRBVO HLJ CJHREFBV  FLJH BVPAEHB LQND QOHRB");
   }
   
   public void handleMoveQueen(){
      imageTitleRule.setImage(new Image("/main/resources/img/rulesTitle/queen.png"));
      imageRule.setImage(new Image("/main/resources/img/rules/game.png"));
      textRule.setText("pohfvljhqb vhzb vlq cljhqbvoHBV Q:,N JEHRBVO HLJ CJHREFBV  FLJH BVPAEHB LQND QOHRB");
   }
   
   public void handleMoveGrassHopper(){
      imageTitleRule.setImage(new Image("/main/resources/img/rulesTitle/grassHopper.png"));
      imageRule.setImage(new Image("/main/resources/img/rules/game.png"));
      textRule.setText("pohfvljhqb vhzb vlq cljhqbvoHBV Q:,N JEHRBVO HLJ CJHREFBV  FLJH BVPAEHB LQND QOHRB");
   }
   
   public void handleMoveBeetle(){
     imageTitleRule.setImage(new Image("/main/resources/img/rulesTitle/beetle.png"));
     imageRule.setImage(new Image("/main/resources/img/rules/game.png"));
     textRule.setText("pohfvljhqb vhzb vlq cljhqbvoHBV Q:,N JEHRBVO HLJ CJHREFBV  FLJH BVPAEHB LQND QOHRB");
   }
   
   public void handleMoveSpider(){
      imageTitleRule.setImage(new Image("/main/resources/img/rulesTitle/spider.png"));
      imageRule.setImage(new Image("/main/resources/img/rules/game.png"));
      textRule.setText("pohfvljhqb vhzb vlq cljhqbvoHBV Q:,N JEHRBVO HLJ CJHREFBV  FLJH BVPAEHB LQND QOHRB");
   }
   
   public void handleMoveAnt(){
      imageTitleRule.setImage(new Image("/main/resources/img/rulesTitle/ant.png"));
      imageRule.setImage(new Image("/main/resources/img/rules/game.png"));
      textRule.setText("pohfvljhqb vhzb vlq cljhqbvoHBV Q:,N JEHRBVO HLJ CJHREFBV  FLJH BVPAEHB LQND QOHRB");
   }
   
}
