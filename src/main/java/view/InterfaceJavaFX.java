/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package main.java.view;


import java.awt.Polygon;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import javafx.application.Application;
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
import main.java.model.piece.*;
import main.java.utils.Consts;
import main.java.utils.Coord;

/**
 *
 * @author gontardb
 */
public class InterfaceJavaFX extends Application{
    
    private Core core;
    private ArrayList<ArrayList<Polygon>> poly;
    private Coord pieceChoose;
    int team = 0;
    private Piece piece;

    @Override
    public void start(Stage primaryStage) throws Exception {
        
        /*Initialisation de la fenêtre */
        BorderPane gameBorderPane = new BorderPane();
        
        Canvas gameCanvas = new Canvas(800,800);
        ScrollPane scrollPane = new ScrollPane(gameCanvas);
        
        gameBorderPane.setLeft(scrollPane);
        Scene gameScene = new Scene(gameBorderPane,800,800);   
        
        primaryStage.setScene(gameScene);
        primaryStage.setTitle("Hive_Test");
        primaryStage.show();
        
        Label choice = new Label("Le joueur " + team + " doit choisir sa pièce !");
        choice.setWrapText(true);
        
        VBox piecesToAdd = new VBox();
        
        Button grassHopper = new Button("GrassHoper");
        Button queen = new Button("Queen");
        Button beetle = new Button("Beetle");
        Button ant = new Button("Ant");
        Button  spider = new Button("Spider");
        
        grassHopper.setOnMousePressed(new EventHandler<MouseEvent>() {

            @Override
            public void handle(MouseEvent event) {
                piece = new Grasshopper(team);
                choice.setText(piece.getName()+piece.getTeam());
            }
        });
        
        queen.setOnMousePressed(new EventHandler<MouseEvent>() {

            @Override
            public void handle(MouseEvent event) {
                piece = new Queen(team);
                choice.setText(piece.getName()+piece.getTeam());
            }
        });
        
        beetle.setOnMousePressed(new EventHandler<MouseEvent>() {

            @Override
            public void handle(MouseEvent event) {
                piece = new Beetle(team);
                choice.setText(piece.getName()+piece.getTeam());
            }
        });
        
        ant.setOnMousePressed(new EventHandler<MouseEvent>() {

            @Override
            public void handle(MouseEvent event) {
                piece = new Ant(team);
                choice.setText(piece.getName()+piece.getTeam());
            }
        });
        
        spider.setOnMousePressed(new EventHandler<MouseEvent>() {

            @Override
            public void handle(MouseEvent event) {
                piece = new Spider(team);
                choice.setText(piece.getName()+piece.getTeam());
            }
        });
        
        piecesToAdd.getChildren().addAll(grassHopper,queen,beetle,ant,spider,choice);
        
        gameBorderPane.setRight(piecesToAdd);
        /*******************************/
        /* Bind du canvas et du scrollPane*/
        scrollPane.vvalueProperty().bind(primaryStage.widthProperty().multiply(0.80));
        scrollPane.hvalueProperty().bind(primaryStage.heightProperty());
       
       gameCanvas.widthProperty().bind(scrollPane.vvalueProperty());
        gameCanvas.heightProperty().bind(scrollPane.hvalueProperty());
        /******************/
        
        /*Initialisation du core et tests basiques*/
        core = new Core(2);
        initPoly(core);

        
        /* Clic sur le canvas principal */
        gameCanvas.setOnMousePressed(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent m) {
                Point2D fdp = new Point2D.Double(m.getX(), m.getY());
                if(m.getButton() == MouseButton.PRIMARY){
                    for(int i = 0;i<poly.size();i++){
                        for(int j = 0;j<poly.get(i).size();j++){
                            if(poly.get(i).get(j).contains(fdp) && core.getCurrentState().getBoard().getTile(new Coord(i, j)) != null){
                                if( pieceChoose != null && core.getDestination().contains(new Coord(i, j))){
                                    System.out.println("Déplacement de la piece choisie");
                                    core.movePiece(pieceChoose, new Coord(i, j));
                                    pieceChoose = null;
                                    initPoly(core);
                                }
                                 else if(core.getCurrentState().getBoard().getTile(new Coord(i, j)).getPiece()!=null){
                                    System.out.println("Tu as cliqué sur le polygone  " + i + " " + j + " ! " + core.getCurrentState().getBoard().getTile(new Coord(i, j)).getPiece());
                                    pieceChoose = new Coord(i,j);
                                    core.setDestination((ArrayList<Coord>) core.getCurrentState().getBoard().getTile(new Coord(i, j)).getPiece().getPossibleMovement(core.getCurrentState().getBoard().getTile(new Coord(i, j)), core.getCurrentState().getBoard()));                                 
                                }
                                
                                else{
                                    System.out.println("Cliquable mais pas de pieces  " + i + " " + j + " !  ");
                                }
                            }
                            else 
                                if(poly.get(i).get(j).contains(fdp))
                                    System.out.println("Pas cliquable  " + i + " " + j + " !  ");
                        }
                    } 
                }
                else if(m.getButton() == MouseButton.SECONDARY){
                    for(int i = 0;i<poly.size();i++){
                        for(int j = 0;j<poly.get(i).size();j++){
                            if(poly.get(i).get(j).contains(fdp)){
                                System.out.println("Clic droit" + i + " " + j);
                                if(core.getCurrentState().getBoard().getTile(new Coord(i, j)) != null){
                                    if(piece != null){
                                        core.addPiece(piece, new Coord(i, j));
                                        piece = null;
                                        team = (team+1)%2;
                                        choice.setText("Le joueur " + team + " doit choisir sa pièce !");
                                        initPoly(core);
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
                else{
                    for(int i = 0;i<poly.size();i++){
                        for(int j = 0;j<poly.get(i).size();j++){
                            if(poly.get(i).get(j).contains(fdp)){
                                if(core.getCurrentState().getBoard().getTile(new Coord(i, j)).getPiece()!=null && pieceChoose == null){
                                    core.getCurrentState().getBoard().removePiece(new Coord(i, j));
                                    initPoly(core);
                                }
                            }
                        }
                    } 
                }
            }
        });
        /********************************/
        
        gameCanvas.setOnMouseMoved(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent m) {
                Point2D fdp = new Point2D.Double(m.getX(), m.getY());
                for(int i = 0;i<poly.size();i++){
                    for(int j = 0;j<poly.get(i).size();j++){
                        if(poly.get(i).get(j).contains(fdp)){
                        }
                    }
                } 
            }
        }    
        );
        
        RefreshJavaFX r = new RefreshJavaFX(core, gameCanvas);
        r.start();
        
    }
    
    public void initPoly(Core c){
        System.err.println("Init lancé");
        poly = new ArrayList();
        for(int i = 0; i < c.getCurrentState().getBoard().getBoard().size();i++){
            ArrayList<Polygon> tmp = new ArrayList<>();
            for(int j = 0; j<c.getCurrentState().getBoard().getBoard().get(i).size();j++){ 
                   tmp.add(addPoly(Consts.X_ORIGIN, Consts.Y_ORIGIN, i, j, Consts.SIDE_SIZE));
            }
            poly.add(tmp);
        }
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
        xd[1] = originX+size;yd[1] = originY+size/2;
        xd[2] = originX+size;yd[2] = (originY+size)+size/2;
        xd[3] = originX;yd[3] = originY+2*size;
        xd[4] = originX-size;yd[4] = (originY+size)+size/2;
        xd[5] = originX-size;yd[5] = originY+size/2;
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
