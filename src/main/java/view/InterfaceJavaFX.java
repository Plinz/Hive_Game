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
import javafx.scene.control.ScrollPane;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import main.java.model.Core;
import main.java.model.piece.Spider;
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

    @Override
    public void start(Stage primaryStage) throws Exception {
        
        /*Initialisation de la fenêtre */
        BorderPane gameBorderPane = new BorderPane();
        Canvas gameCanvas = new Canvas(1000,1000);
        ScrollPane scrollPane = new ScrollPane(gameCanvas);
        gameBorderPane.setLeft(scrollPane);
        Scene gameScene = new Scene(gameBorderPane,800,800);   
        primaryStage.setScene(gameScene);
        primaryStage.setTitle("Hive_Test");
        primaryStage.show();
        /*******************************/
        
        /* Bind du canvas */
        gameCanvas.widthProperty().bind(gameBorderPane.widthProperty());
        gameCanvas.heightProperty().bind(gameBorderPane.heightProperty());
        /******************/
        
        /*Initialisation du core et tests basiques*/
        core = new Core(2);
        
        //core.addPiece(new Ant(0), new Coord(0, 0));
        //core.addPiece(new Ant(1), new Coord(0 , 1));
        //core.addPiece(new Beetle(1), new Coord(1 , 0));
        //core.addPiece(new Beetle(2), new Coord(0 , 3));
        //core.addPiece(new Beetle(3), new Coord(2 , 3));
        //core.movePiece(new Coord(1, 1), new Coord(2, 2));
        initPoly(core);
        
        
        /************************/
        
        /* Initialisation du tableau de polygones 
        poly = new ArrayList<>();
        for(double i = 0; i<3;i++){
            ArrayList<Polygon> tmp = new ArrayList<>();
            for(double j = 0;j<3;j++){
                //tmp.add(addPoly(gameCanvas, x, y, i, j, Consts.SIDE_SIZE));
            }
            poly.add(tmp);
        }
        /*******************************************/
        
        /* Clic sur le canvas principal */
        gameCanvas.setOnMousePressed(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent m) {
                Point2D fdp = new Point2D.Double(m.getX(), m.getY());
                if(m.getButton() == MouseButton.PRIMARY){
                    for(int i = 0;i<poly.size();i++){
                        for(int j = 0;j<poly.get(i).size();j++){
                            if(poly.get(i).get(j).contains(fdp) && core.getCurrentState().getBoard().getTile(new Coord(i, j)) != null){
                                if(core.getCurrentState().getBoard().getTile(new Coord(i, j)).getPiece()!=null && pieceChoose == null){
                                    System.err.println("Tu as cliqué sur le polygone  " + i + " " + j + " !  " + core.getCurrentState().getBoard().getTile(new Coord(i, j)).getPiece());
                                    //core.getCurrentState().getBoard().removePiece(new Coord(i, j));
                                    pieceChoose = new Coord(i,j);
                                }
                                else if( core.getCurrentState().getBoard().getTile(new Coord(i, j)).getPiece()==null && pieceChoose != null){
                                    System.err.println("Piece choisie, on essaye de la déplacer " + pieceChoose.getX() + pieceChoose.getY());
                                    core.getCurrentState().getBoard().movePiece(pieceChoose, new Coord(i, j));
                                    pieceChoose = null;
                                    initPoly(core);
                                }
                                else{
                                    System.err.println("Cliquable mais pas de pieces  " + i + " " + j + " !  ");
                                }
                            }
                            else 
                                if(poly.get(i).get(j).contains(fdp))
                                    System.err.println("Pas cliquable  " + i + " " + j + " !  ");
                        }
                    } 
                }
                else{
                    for(int i = 0;i<poly.size();i++){
                        for(int j = 0;j<poly.get(i).size();j++){
                            if(poly.get(i).get(j).contains(fdp)){
                                System.out.println("Clidroit" + i + " " + j);
                                if(core.getCurrentState().getBoard().getTile(new Coord(i, j)) != null){
                                    core.addPiece(new Spider(1), new Coord(i, j));
                                    initPoly(core);
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
        
        gameCanvas.setOnMouseMoved(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent m) {
                Point2D fdp = new Point2D.Double(m.getX(), m.getY());
                for(int i = 0;i<poly.size();i++){
                    for(int j = 0;j<poly.get(i).size();j++){
                        if(poly.get(i).get(j).contains(fdp)){
                            //System.out.println(" Pos : " + i + " "+ j);
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
