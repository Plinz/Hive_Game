/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package main.java.controller;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.VPos;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Dialog;
import javafx.scene.control.MenuItem;
import javafx.scene.image.Image;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.text.Text;
import main.java.implement.Main;
import main.java.model.Core;
import main.java.model.Piece;
import main.java.utils.Consts;
import main.java.utils.CoordGene;
import main.java.view.Highlighter;
import main.java.view.RefreshJavaFX;
import main.java.view.TraducteurBoard;

/**
 *
 * @author duvernet
 */
public class GameScreenController implements Initializable {
    @FXML private Canvas gameCanvas;
    @FXML private GridPane inventoryPlayer1;
    @FXML private GridPane inventoryPlayer2;
    @FXML private Text namePlayer1;
    @FXML private Text namePlayer2;
    @FXML private MenuItem launchNewGame;
    private Main main;
    private Core core;
    private int pieceToChoose;
    private CoordGene<Integer> pieceToMove;
    private Highlighter highlighted;

    
    public void setMainApp(Main mainApp) {
        this.main = mainApp;    
    }
    public void setCore(Core c) {
        this.core = c;    
    }

    public void setPieceToChoose(int pieceToChoose) {
        this.pieceToChoose = pieceToChoose;
    }

    public void setPieceToMove(CoordGene<Integer> pieceToMove) {
        this.pieceToMove = pieceToMove;
    }

    public void setHighlighted(Highlighter highlighted) {
        this.highlighted = highlighted;
    }
    
    
    
     @Override
    public void initialize(URL url, ResourceBundle rb) {
    }
    
    public void initGame(Main mainApp, Core c){
        setMainApp(mainApp);
        setCore(c);
        this.pieceToChoose = -1;
        highlighted = new Highlighter();
        
        initButtonByInventory();
        initGameCanvas();
        
        RefreshJavaFX r = new RefreshJavaFX(core, gameCanvas, highlighted);
        r.start();
        
    }
    /* Inititialisation des handlers */
    public void initGameCanvas(){
        
        gameCanvas.setOnMousePressed(new EventHandler<MouseEvent>() {
            TraducteurBoard t = new TraducteurBoard();

            @Override
            public void handle(MouseEvent m) {
                CoordGene<Double> coordPix = new CoordGene<>(m.getX(), m.getY());
                CoordGene<Double> coordAx = t.pixelToAxial(coordPix);
                int i = coordAx.getX().intValue();
                int j = coordAx.getY().intValue();

                if (m.getButton() == MouseButton.PRIMARY) {
                    if (core.getCurrentState().getBoard().getTile(new CoordGene<Integer>(i, j)) != null) {
                        CoordGene<Integer> coord = new CoordGene<>(i, j);
                        if (pieceToMove != null &&  core.getCurrentState().getBoard().getTile(pieceToMove).getPiece().getPossibleMovement(core.getCurrentState().getBoard().getTile(pieceToMove), core.getCurrentState().getBoard()).contains(coord)/*&& core.getDestination().contains(coord)*/) {
                            if(core.movePiece(pieceToMove, coord)){
                                handleEndGame();
                            }
                            resetPiece();
                            initButtonByInventory();
                            highlighted.setListTohighlight(null);
                            
                        } else if (core.getCurrentState().getBoard().getTile(coord).getPiece() != null && core.checkQueenRule()/* A commenter si nécessaire */) {
                            if (core.getCurrentState().getBoard().getTile(coord).getPiece().getTeam() == core.getCurrentState().getCurrentPlayer()) {
                                pieceToMove = coord;
                                highlighted.setListTohighlight(core.getPossibleMovement(coord));
                                pieceToChoose = -1;
                            }
                        } else {
                            System.out.println("Cliquable mais pas de pieces  " + i + " " + j + " !  ");
                            if (pieceToChoose != -1 && core.getPossibleAdd().contains(coord)) {
                                if(core.addPiece(pieceToChoose, coord)){
                                    handleEndGame();
                                }
                                resetPiece();
                                initButtonByInventory();
                                highlighted.setListTohighlight(null);
                            } else {
                                resetPiece();
                                highlighted.setListTohighlight(null);
                            }
                        }
                    } else {
                        System.out.println("Pas cliquable  " + i + " " + j + " !  ");
                        resetPiece();
                        highlighted.setListTohighlight(null);
                    }
                }
            }

        });
    }
    public void handleNewGame(){
        Core c = new Core(this.core.getMode(),Consts.MEDIUM);
        c.getCurrentState().getPlayers()[0].setName(core.getCurrentState().getPlayers()[0].getName());
        c.getCurrentState().getPlayers()[1].setName(core.getCurrentState().getPlayers()[1].getName());
        main.showGameScreen(c);
    }
    
    public void handleLeaveGame(){
        main.showMainMenu();
    }
    
    
    /*Fin des handlers */
    
    
    
    /*Méthodes d'initialisation */
    public void initButtonByInventory() {
        if(core.getCurrentState().getCurrentPlayer() == 0){
            
            //inventoryPlayer1.setBorder(new Border(new BorderStroke(Color.LIGHTGREEN, BorderStrokeStyle.SOLID, CornerRadii.EMPTY, BorderWidths.EMPTY)));
            inventoryPlayer1.setBackground(new Background(new BackgroundFill(Color.LIGHTGREEN, CornerRadii.EMPTY, Insets.EMPTY)));
            inventoryPlayer2.setBackground(new Background(new BackgroundFill(Color.RED, CornerRadii.EMPTY, Insets.EMPTY)));
            namePlayer1.setText(core.getCurrentState().getPlayers()[core.getCurrentState().getCurrentPlayer()].getName() + " à vous de jouer !");
            namePlayer2.setText(core.getCurrentState().getPlayers()[1-core.getCurrentState().getCurrentPlayer()].getName() + " attends !");
        }
        else{
            inventoryPlayer1.setBackground(new Background(new BackgroundFill(Color.RED, CornerRadii.EMPTY, Insets.EMPTY)));
            inventoryPlayer2.setBackground(new Background(new BackgroundFill(Color.LIGHTGREEN, CornerRadii.EMPTY, Insets.EMPTY)));
            namePlayer1.setText(core.getCurrentState().getPlayers()[core.getCurrentState().getCurrentPlayer()].getName() + " attends !");
            namePlayer2.setText(core.getCurrentState().getPlayers()[1-core.getCurrentState().getCurrentPlayer()].getName() + " à vous de jouer !");
        } 
        initPlayer1Button();
        initPlayer2Button();
    }
        
    public void initPlayer1Button(){
        inventoryPlayer1.getChildren().remove(0, inventoryPlayer1.getChildren().size());
        List<Piece> inventory = core.getCurrentState().getPlayers()[0].getInventory();
        int line = 0;
        int col = 0;
        for (int i = 0; i < inventory.size(); i++) {
            String name = inventory.get(i).getName();
            int team = inventory.get(i).getTeam();
            Button b = new Button();
            b.setMinSize(40, 40);
            b.setBackground(new Background(new BackgroundFill(new ImagePattern(new Image(getClass().getClassLoader().getResource("main/resources/img/tile/"+name + team + ".png").toString())), CornerRadii.EMPTY, Insets.EMPTY)));

            if(core.getCurrentState().getCurrentPlayer() == 0){
                b.setOnMousePressed(new ControllerButtonPiece(this,highlighted,core, i));
            }
            if(i%4 == 0){
                col = 0;
                line++;
            }
            else{
                col++;
            }
            GridPane.setConstraints(b, col, line);
            GridPane.setHalignment(b, HPos.CENTER);
            GridPane.setValignment(b, VPos.CENTER);
            inventoryPlayer1.getChildren().add(b);
        }
    }
    
    public void initPlayer2Button(){
        inventoryPlayer2.getChildren().remove(0, inventoryPlayer2.getChildren().size());
        List<Piece> inventory = core.getCurrentState().getPlayers()[1].getInventory();
        int line = 0;
        int col = 0;
        for (int i = 0; i < inventory.size(); i++) {
            String name = inventory.get(i).getName();
            int team = inventory.get(i).getTeam();
            Button b = new Button();
            b.setMinSize(40, 40);
            b.setBackground(new Background(new BackgroundFill(new ImagePattern(new Image(getClass().getClassLoader().getResource("main/resources/img/tile/"+name + team + ".png").toString())), CornerRadii.EMPTY, Insets.EMPTY)));
            
            if(core.getCurrentState().getCurrentPlayer() == 1){
                b.setOnMousePressed(new ControllerButtonPiece(this,highlighted,core, i));
            }
            if(i%4 == 0){
                col = 0;
                line++;
            }
            else{
                col++;
            }
            GridPane.setConstraints(b, col, line);
            GridPane.setHalignment(b, HPos.CENTER);
            GridPane.setValignment(b, VPos.CENTER);
            inventoryPlayer2.getChildren().add(b);
        }
    }
    
    public void resetPiece(){
        pieceToMove = null;
        pieceToChoose = -1;
    }
    
    public void handleEndGame(){
        Dialog dialog = new Alert(Alert.AlertType.INFORMATION);
        dialog.setTitle("Fin de partie !");
        switch(core.getStatus()){
            case 0 :
                dialog.setContentText("Le joueur Blanc perd la partie");
                break;
            case 1 :
                dialog.setContentText("Le joueur Noir perd la partie");
                break;
            case Consts.NUL:
                dialog.setContentText("Match nul");
                break;
            default :
                dialog.setContentText("Ne devrait pas arriver !");
                break;
        }
        dialog.show();
    }
    
    /*******************/
}
