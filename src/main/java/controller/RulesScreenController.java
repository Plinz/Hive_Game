package main.java.controller;


import javafx.fxml.FXML;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import main.java.implement.Main;
import main.java.utils.Consts;


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
    private ArrayList<Label> listLabel;
    @FXML private ImageView imageTitleRule;
    @FXML private ImageView imageRule;
    @FXML private Text textRule;
    @FXML private Label queenTitle, grassHopperTitle, beetleTitle, spiderTitle,antTitle;
    private Label labelTemp;
    
    public RulesScreenController() {

    }

    @FXML
    @Override
    public void initialize(URL url, ResourceBundle rb) {
         listLabel = new ArrayList<>();
         listLabel.add(queenTitle);
         listLabel.add(grassHopperTitle);
         listLabel.add(beetleTitle);
         listLabel.add(spiderTitle);
         listLabel.add(antTitle);   
         labelTemp = new Label();
    }  
    
    @FXML 
    public void handleMenuClick(){
        main.showMainMenu();
    }
    
    
    public void setMainApp(Main mainApp) {
        this.main = mainApp;    
    }
    
   public void handleGoalGame(){
       
       imageTitleRule.setImage(new Image("/main/resources/img/rulesTitle/goalGame.png"));
       imageRule.setImage(new Image("/main/resources/img/rules/butdujeu.png"));
       textRule.setText(Consts.GOAL);
   }
   
   public void handleGamePlay(){
      imageTitleRule.setImage(new Image("/main/resources/img/rulesTitle/gamePlay.png"));
      imageRule.setImage(new Image("/main/resources/img/rules/deroulement.png"));
      textRule.setText(Consts.DURING_THE_GAME);
   }
   
   public void handlePiecePositionings(){
      imageTitleRule.setImage(new Image("/main/resources/img/rulesTitle/PiecePositionings.png"));
      imageRule.setImage(new Image("/main/resources/img/rules/placement.png"));
      textRule.setText(Consts.PIECE_POSITIONNING);
   }
   
   public void handleMoveQueen(){
      labelTemp = queenTitle;
      resetClickColor();
      queenTitle.setBackground(new Background(new BackgroundFill(Color.web("#e1ab0a"),CornerRadii.EMPTY, Insets.EMPTY)));
      imageTitleRule.setImage(new Image("/main/resources/img/rulesTitle/queen.png"));
      imageRule.setImage(new Image("/main/resources/img/rules/game.png"));
      textRule.setText(Consts.QUEEN_MOVEMENT);
   }
   
   public void handleMoveGrassHopper(){
      labelTemp = grassHopperTitle;
      resetClickColor();
      grassHopperTitle.setBackground(new Background(new BackgroundFill(Color.web("#e1ab0a"),CornerRadii.EMPTY, Insets.EMPTY)));
      imageTitleRule.setImage(new Image("/main/resources/img/rulesTitle/grassHopper.png"));
      imageRule.setImage(new Image("/main/resources/img/rules/game.png"));
      textRule.setText(Consts.GRASSHOPPER_MOVEMENT);
   }
   
   public void handleMoveBeetle(){
     labelTemp = beetleTitle;
     resetClickColor();
     beetleTitle.setBackground(new Background(new BackgroundFill(Color.web("#e1ab0a"),CornerRadii.EMPTY, Insets.EMPTY)));
     imageTitleRule.setImage(new Image("/main/resources/img/rulesTitle/beetle.png"));
     imageRule.setImage(new Image("/main/resources/img/rules/game.png"));
     textRule.setText(Consts.BEETLE_MOVEMENT);
   }
   
   public void handleMoveSpider(){
      labelTemp = spiderTitle;
      resetClickColor();
      spiderTitle.setBackground(new Background(new BackgroundFill(Color.web("#e1ab0a"),CornerRadii.EMPTY, Insets.EMPTY)));
      imageTitleRule.setImage(new Image("/main/resources/img/rulesTitle/spider.png"));
      imageRule.setImage(new Image("/main/resources/img/rules/game.png"));
      textRule.setText(Consts.SPIDER_MOVEMENT);
   }
   
   public void handleMoveAnt(){
      labelTemp = antTitle;
      resetClickColor();
      antTitle.setBackground(new Background(new BackgroundFill(Color.web("#e1ab0a"),CornerRadii.EMPTY, Insets.EMPTY)));
      imageTitleRule.setImage(new Image("/main/resources/img/rulesTitle/ant.png"));
      imageRule.setImage(new Image("/main/resources/img/rules/game.png"));
      textRule.setText(Consts.ANT_MOVEMENT);
   }
   
   private void resetClickColor(){
       for(int i = 0; i<5;i++){
           if(listLabel.get(i) != labelTemp)
                listLabel.get(i).setBackground(new Background(new BackgroundFill(Color.TRANSPARENT,CornerRadii.EMPTY, Insets.EMPTY)));
       }      
   }
   
}
