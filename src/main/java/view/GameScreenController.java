/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package main.java.view;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import main.java.controller.ControllerButton;
import main.java.implement.Main;
import main.java.model.Core;
import main.java.model.Piece;
import main.java.utils.CoordGene;

/**
 *
 * @author duvernet
 */
public class GameScreenController implements Initializable {
    @FXML private Canvas gameCanvas;
    @FXML private GridPane inventoryPlayer1;
    @FXML private GridPane inventoryPlayer2;
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
    
    public void initGameCanvas(){
        gameCanvas.setOnMousePressed(new EventHandler<MouseEvent>() {
            TraducteurBoard t = new TraducteurBoard();

            @Override
            public void handle(MouseEvent m) {
                //Point2D mousePoint = new Point2D.Double(m.getX(), m.getY());
                CoordGene<Double> coordPix = new CoordGene<>(m.getX(), m.getY());
                CoordGene<Double> coordAx = t.pixelToAxial(coordPix);
                int i = coordAx.getX().intValue();
                int j = coordAx.getY().intValue();

                if (m.getButton() == MouseButton.PRIMARY) {
                    if (core.getCurrentState().getBoard().getTile(new CoordGene<Integer>(i, j)) != null) {
                        CoordGene<Integer> coord = new CoordGene<>(i, j);
                        if (pieceToMove != null &&  core.getCurrentState().getBoard().getTile(pieceToMove).getPiece().getPossibleMovement(core.getCurrentState().getBoard().getTile(pieceToMove), core.getCurrentState().getBoard()).contains(coord)/*&& core.getDestination().contains(coord)*/) {
                            //System.out.println("Déplacement de la piece choisie");
                            core.movePiece(pieceToMove, coord);
                            pieceToMove = null;
                            pieceToChoose = -1;
                            initButtonByInventory();
                            //choice.setText("Le joueur " + core.getCurrentState().getCurrentPlayer() + " doit choisir sa pièce !");
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
                                core.addPiece(pieceToChoose, coord);
                                pieceToMove = null;
                                pieceToChoose = -1;
                                initButtonByInventory();
                                //choice.setText("Le joueur " + core.getCurrentState().getCurrentPlayer() + " doit choisir sa pièce !");
                                initButtonByInventory();
                                highlighted.setListTohighlight(null);
                            } else {
                                pieceToMove = null;
                                pieceToChoose = -1;
                                highlighted.setListTohighlight(null);
                            }
                        }
                    } else {
                        System.out.println("Pas cliquable  " + i + " " + j + " !  ");
                        pieceToMove = null;
                        pieceToChoose = -1;
                        highlighted.setListTohighlight(null);
                    }
                }
            }

        });
    }
    
    
    public void initButtonByInventory() {
        if(core.getCurrentState().getCurrentPlayer() == 0){
            inventoryPlayer1.setBackground(new Background(new BackgroundFill(Color.LIGHTGREEN, CornerRadii.EMPTY, Insets.EMPTY)));
            inventoryPlayer2.setBackground(new Background(new BackgroundFill(Color.RED, CornerRadii.EMPTY, Insets.EMPTY)));
        }
        else{
            inventoryPlayer1.setBackground(new Background(new BackgroundFill(Color.RED, CornerRadii.EMPTY, Insets.EMPTY)));
            inventoryPlayer2.setBackground(new Background(new BackgroundFill(Color.LIGHTGREEN, CornerRadii.EMPTY, Insets.EMPTY)));
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
            b.setBackground(new Background(new BackgroundFill(new ImagePattern(new Image(name + team + ".png")), CornerRadii.EMPTY, Insets.EMPTY)));

            //b.setOnMousePressed(new ControllerButton(this,highlighted,core, i));
            //list.add(b);
            if(i%4 == 0){
                col = 0;
                line++;
            }
            else{
                col++;
            }
            GridPane.setConstraints(b, col, line);
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
            b.setBackground(new Background(new BackgroundFill(new ImagePattern(new Image(name + team + ".png")), CornerRadii.EMPTY, Insets.EMPTY)));
            
            //b.setOnMousePressed(new ControllerButton(this,highlighted,core, i));
            //list.add(b);
            if(i%4 == 0){
                col = 0;
                line++;
            }
            else{
                col++;
            }
            GridPane.setConstraints(b, col, line);
            inventoryPlayer2.getChildren().add(b);
        }
    }
}
