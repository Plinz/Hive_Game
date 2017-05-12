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
import javafx.scene.effect.InnerShadow;
import javafx.scene.image.Image;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
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
import main.java.model.Tile;
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
    private CoordGene<Double> lastCoord;
    private CoordGene<Integer> pieceToMove;
    private List<CoordGene<Integer>> possibleMovement;
    private Highlighter highlighted;
    private TraducteurBoard t;
    private boolean freeze;

    
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
        pieceToChoose = -1;
        highlighted = new Highlighter();
        t = new TraducteurBoard();
        freeze = false;
        
        initButtonByInventory();
        initGameCanvas();
        
        RefreshJavaFX r = new RefreshJavaFX(core, gameCanvas, highlighted, t);
        r.start();
        
    }
    /* Inititialisation des handlers */
    public void initGameCanvas(){
        
        gameCanvas.setOnMousePressed(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent m) {
                if(!freeze){
                	CoordGene<Double> coordAx = t.pixelToAxial(new CoordGene<Double>(m.getX(), m.getY()));
                	CoordGene<Integer> coord = new CoordGene<Integer>(coordAx.getX().intValue(), coordAx.getY().intValue());
                	CoordGene<Double> origin = t.getMoveOrigin();
                	lastCoord = new CoordGene<Double>(m.getX() - origin.getX(), m.getY() - origin.getY());
                    
                    if (m.getButton() == MouseButton.PRIMARY) {
                        if (core.isTile(coord)) {
                            if (pieceToMove != null &&  possibleMovement.contains(coord)) {
                                if(core.movePiece(pieceToMove, coord))
                                    handleEndGame();
                                else{
                                    resetPiece();
                                    initButtonByInventory();
                                    highlighted.setListTohighlight(null);
                                }
                            } else if (core.isPieceOfCurrentPlayer(coord)) {
                                    pieceToMove = coord;
                                    possibleMovement = core.getPossibleMovement(coord);
                                    highlighted.setListTohighlight(possibleMovement);
                                    pieceToChoose = -1;
                            } else {
                                if (pieceToChoose != -1 && core.getPossibleAdd().contains(coord) && core.addPiece(pieceToChoose, coord))
                                	handleEndGame();
                                resetPiece();
                                initButtonByInventory();
                                highlighted.setListTohighlight(possibleMovement = null);
                            }
                        } else {
                            resetPiece();
                            highlighted.setListTohighlight(null);
                        }
                    }
                    else if (m.getButton() == MouseButton.SECONDARY)
                    	core.save("COCO");
                }
            }
        });
        
        gameCanvas.setOnMouseDragged(new EventHandler<MouseEvent>(){
                public void handle(MouseEvent m) {
                    t.setMoveOrigin(new CoordGene<Double>(m.getX() - lastCoord.getX(),m.getY() - lastCoord.getY()));
                }
     
        });
        
        gameCanvas.setOnScroll(new EventHandler<ScrollEvent>(){

            @Override
            public void handle(ScrollEvent event) {
                if(event.getDeltaY() > 0){
                     t.setSizeHex(t.getSizeHex()*1.1);
                }
                else{
                    t.setSizeHex(t.getSizeHex()*0.9);
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
    
    public void handleUpButton(){
        t.setMoveOrigin(new CoordGene<>(t.getMoveOrigin().getX(),t.getMoveOrigin().getY()-10));
    }
    
    public void handleRightButton(){
        t.setMoveOrigin(new CoordGene<>(t.getMoveOrigin().getX()+10,t.getMoveOrigin().getY()));
    }
    
    public void handleDownButton(){
        t.setMoveOrigin(new CoordGene<>(t.getMoveOrigin().getX(),t.getMoveOrigin().getY()+10));
    }
    
    public void handleLeftButton(){
        t.setMoveOrigin(new CoordGene<>(t.getMoveOrigin().getX()-10,t.getMoveOrigin().getY()));
    }
    
    public void handleUndoButton(){
        core.previousState();
        resetPiece();
        highlighted.setListTohighlight(null);
        initButtonByInventory();
    }
    
    public void handleRedoButton(){
        core.nextState();
        resetPiece();
        highlighted.setListTohighlight(null);
        initButtonByInventory();
    }
    /*Fin des handlers */
    
    /*Méthodes d'initialisation */
    public void initButtonByInventory() {
        if(core.getCurrentState().getCurrentPlayer() == 0){
            
            //inventoryPlayer1.setBorder(new Border(new BorderStroke(Color.LIGHTGREEN, BorderStrokeStyle.SOLID, CornerRadii.EMPTY, BorderWidths.EMPTY)));
            inventoryPlayer1.setBackground(new Background(new BackgroundFill(Color.LIGHTGREEN, CornerRadii.EMPTY, Insets.EMPTY)));
            inventoryPlayer2.setBackground(new Background(new BackgroundFill(Color.RED, CornerRadii.EMPTY, Insets.EMPTY)));
            namePlayer1.setText(core.getCurrentState().getPlayers()[0].getName() + " à vous de jouer !");
            namePlayer2.setText(core.getCurrentState().getPlayers()[1].getName() + " attend !");
        }
        else{
            inventoryPlayer1.setBackground(new Background(new BackgroundFill(Color.RED, CornerRadii.EMPTY, Insets.EMPTY)));
            inventoryPlayer2.setBackground(new Background(new BackgroundFill(Color.LIGHTGREEN, CornerRadii.EMPTY, Insets.EMPTY)));
            namePlayer1.setText(core.getCurrentState().getPlayers()[0].getName() + " attend !");
            namePlayer2.setText(core.getCurrentState().getPlayers()[1].getName() + " à vous de jouer !");
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
            b.setMinSize(45, 45);
            b.setBackground(new Background(new BackgroundFill(new ImagePattern(new Image(getClass().getClassLoader().getResource("main/resources/img/tile/"+name + team + ".png").toString())), CornerRadii.EMPTY, Insets.EMPTY)));

            if(core.getCurrentState().getCurrentPlayer() == 0 && !freeze){
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
            GridPane.setValignment(b, VPos.TOP);
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
            b.setMinSize(45, 45);
            b.setBackground(new Background(new BackgroundFill(new ImagePattern(new Image(getClass().getClassLoader().getResource("main/resources/img/tile/"+name + team + ".png").toString())), CornerRadii.EMPTY, Insets.EMPTY)));
            
            if(core.getCurrentState().getCurrentPlayer() == 1 && !freeze){
                b.setOnMousePressed(new ControllerButtonPiece(this,highlighted,core, i));
                b.setOnMouseEntered(new EventHandler<MouseEvent>(){

                    @Override
                    public void handle(MouseEvent event) {
                        b.setEffect(new InnerShadow(10, Color.WHITE));
                    }                  
                });
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
            GridPane.setValignment(b, VPos.TOP);
            inventoryPlayer2.getChildren().add(b);
        }
    }
    
    public void resetPiece(){
        pieceToMove = null;
        pieceToChoose = -1;
    }
    
    public void handleEndGame(){
        freeze = true;
        highlighted.setListTohighlight(null);
        initButtonByInventory();
        Dialog dialog = new Alert(Alert.AlertType.INFORMATION);
        dialog.setTitle("Fin de partie !");
        switch(core.getStatus()){
            case 0 :
                namePlayer1.setText(core.getCurrentState().getPlayers()[0].getName() + " à perdu !");
                namePlayer2.setText(core.getCurrentState().getPlayers()[1].getName() + " à gagné !");
                dialog.setContentText("Le joueur noir remporte la victoire !");
                break;
            case 1 :
                namePlayer1.setText(core.getCurrentState().getPlayers()[0].getName() + " à gagné !");
                namePlayer2.setText(core.getCurrentState().getPlayers()[1].getName() + " à perdu !");
                dialog.setContentText("Le joueur blanc remporte la victoire");
                break;
            case Consts.NUL:
                namePlayer1.setText(core.getCurrentState().getPlayers()[0].getName() + " : match nul !");
                namePlayer2.setText(core.getCurrentState().getPlayers()[1].getName() + " : match nul !");
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

