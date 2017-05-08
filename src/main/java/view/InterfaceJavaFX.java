/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package main.java.view;

import java.awt.Polygon;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.List;
import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import main.java.model.*;
import main.java.utils.Consts;
import main.java.utils.Coord;
/**
 *
 * @author gontardb
 */
public class InterfaceJavaFX extends Application{
    
    private Core core;
    private ArrayList<ArrayList<Polygon>> poly;
    private Label choice;
    private VBox piecesToAdd;


    @Override
    public void start(Stage primaryStage) throws Exception {
        
        /*Initialisation du core et tests basiques*/
        core = new Core(2);
        initPoly(core);
        
        /*Initialisation de l'obsbservable */
        core.getCurrentState().getCurrentPlayer().addListener(new ChangeListener<Number>(){
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                initPoly(core);
                choice.setText("Le joueur " + core.getCurrentState().getCurrentPlayer().get() + " doit choisir sa pièce !");
                piecesToAdd.getChildren().remove(0, piecesToAdd.getChildren().size());
                piecesToAdd.getChildren().add(choice);
                piecesToAdd.getChildren().addAll(initButtonByInventory());
            }
        
        });
        /*Initialisation de la fenêtre */
        BorderPane gameBorderPane = new BorderPane();
        
        Canvas gameCanvas = new Canvas(800,800);
        ScrollPane scrollPane = new ScrollPane(gameCanvas);
        
        gameBorderPane.setRight(scrollPane);
        Scene gameScene = new Scene(gameBorderPane,800,800);   
        
        primaryStage.setScene(gameScene);
        primaryStage.setTitle("Hive_Test");
        primaryStage.show();
        
        choice = new Label("Le joueur " + core.getCurrentState().getCurrentPlayer().getValue() + " doit choisir sa pièce !");
        choice.setWrapText(true);
        
        piecesToAdd = new VBox();
        
        piecesToAdd.getChildren().add(choice);
        piecesToAdd.getChildren().addAll(initButtonByInventory());
        
        gameBorderPane.setLeft(piecesToAdd);
        /*******************************/
        /* Bind du canvas et du scrollPane*/
        scrollPane.vvalueProperty().bind(primaryStage.widthProperty().multiply(0.80));
        scrollPane.hvalueProperty().bind(primaryStage.heightProperty());
       
        gameCanvas.widthProperty().bind(scrollPane.vvalueProperty());
        gameCanvas.heightProperty().bind(scrollPane.hvalueProperty());
        /******************/
        
        /* Clic sur le canvas principal */
        gameCanvas.setOnMousePressed(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent m) {
                Point2D mousePoint = new Point2D.Double(m.getX(), m.getY());
                if(m.getButton() == MouseButton.PRIMARY){
                    for(int i = 0;i<poly.size();i++){
                        for(int j = 0;j<poly.get(i).size();j++){
                            if(poly.get(i).get(j).contains(mousePoint) && core.getCurrentState().getBoard().getTile(new Coord(i, j)) != null){
                                Coord coord = new Coord(i, j);
                                if( core.getPieceChoose() != null && core.getDestination().contains(coord)){
                                    //System.out.println("Déplacement de la piece choisie");
                                    core.movePiece(core.getPieceChoose(), coord);
                                }
                                 else if(core.getCurrentState().getBoard().getTile(coord).getPiece() != null){
                                    //System.out.println("Tu as cliqué sur le polygone  " + i + " " + j + " ! " + core.getCurrentState().getBoard().getTile(new Coord(i, j)).getPiece());
                                    if(core.getCurrentState().getBoard().getTile(coord).getPiece().getTeam() == core.getCurrentState().getCurrentPlayer().get()) {
                                        core.initPieceChoose(coord);                                
                                    }                               
                                }
                                
                                else{
                                    System.out.println("Cliquable mais pas de pieces  " + i + " " + j + " !  ");
                                    if(core.getPieceToPlace() != null){
                                        core.addPiece(core.getPieceToPlace(), coord);
                                    }
                                    else{
                                        core.clearPiecesAndDest();
                                    }
                                }
                            }
                            else 
                                if(poly.get(i).get(j).contains(mousePoint))
                                    System.out.println("Pas cliquable  " + i + " " + j + " !  ");
                        }
                    } 
                }
                else if(m.getButton() == MouseButton.SECONDARY){
                    for(int i = 0;i<poly.size();i++){
                        for(int j = 0;j<poly.get(i).size();j++){
                            if(poly.get(i).get(j).contains(mousePoint)){
                                System.out.println("Clic droit" + i + " " + j);
                                if(core.getCurrentState().getBoard().getTile(new Coord(i, j)) != null){
                                    if(core.getPieceToPlace() != null){
                                        core.addPiece(core.getPieceToPlace(), new Coord(i, j));
                                    }
                                    else{
                                        System.out.println("Pas de pieces choisies");
                                    }
                                }
                                else{
                                    System.err.println("On ajoute pas sur une case non cliquable");
                                }
                            }
                        }
                    }                   
                }
            }
        });
        /********************************/
        
        RefreshJavaFX r = new RefreshJavaFX(core, gameCanvas);
        r.start();
        
    }
    
    public void initPoly(Core c){
        System.err.println("Init lancé");
        poly = new ArrayList();
        for(int i = 0; i < c.getCurrentState().getBoard().getBoard().size();i++){
            ArrayList<Polygon> tmp = new ArrayList<>();
            for(int j = 0; j < c.getCurrentState().getBoard().getBoard().get(i).size();j++){ 
                   tmp.add(addPoly(Consts.X_ORIGIN, Consts.Y_ORIGIN, i, j, Consts.SIDE_SIZE));
            }
            poly.add(tmp);
        }
    }
    
    public List<Button> initButtonByInventory(){
        List<Piece> inventory = core.getCurrentState().getPlayers()[core.getCurrentState().getCurrentPlayer().get()].getInventory();
        List<Button> list = new ArrayList<>();
        
        for(int i = 0; i < inventory.size();i++){
            String name = inventory.get(i).getName();
            int team = inventory.get(i).getTeam();
            Button b = new Button(name);
            
            b.setOnMousePressed(new EventHandler<MouseEvent>() {

            @Override
            public void handle(MouseEvent event) {
                Piece piece = PieceFactory.create(name+team);
                core.setPieceToPlace(piece);
                choice.setText(piece.getName()+piece.getTeam());
            }
        });
            list.add(b);
        }
        return list;
    }
    
    public Polygon addPoly(double x,double y,double i,double j,double size){
        double[] xd = new double[6];
        double[] yd = new double[6];
        double originX,originY;
        
        if(j%2 == 0){
            originX = x+(2*size*i)+((2*size)*Math.floor(j/2));
        }
        else{
            originX = x+(2*size*i)+size+((2*size)*Math.floor(j/2));
            
        }
        originY = y+(size*j)+(j*(size/2));
        
        int [] rx = new int[6];
        int [] ry = new int[6];
        xd[0] = originX; yd[0] = originY;
        xd[1] = originX+size; yd[1] = originY+size/2;
        xd[2] = originX+size; yd[2] = (originY+size)+size/2;
        xd[3] = originX; yd[3] = originY+2*size;
        xd[4] = originX-size; yd[4] = (originY+size)+size/2;
        xd[5] = originX-size; yd[5] = originY+size/2;
        for(int a = 0; a<6;a++){
            rx[a] = (int) xd[a];
            ry[a] = (int) yd[a];
        }
        
        return new Polygon(rx,ry,6);
    }
    public static void creer(String[] args) {
        launch(args);
    }    
}
