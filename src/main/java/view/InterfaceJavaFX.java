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
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import main.java.model.Core;
import main.java.model.Tile;
import main.java.model.piece.Ant;
import main.java.model.piece.Beetle;
import main.java.utils.Coord;

/**
 *
 * @author gontardb
 */
public class InterfaceJavaFX extends Application{
    
    private Core core;
    private ArrayList<ArrayList<Polygon>> poly;
    

    @Override
    public void start(Stage primaryStage) throws Exception {
        
        double size = 15;
        double x = 70;
        double y = 30;
        
        
        
        /*Initialisation de la fenêtre */
        BorderPane gameBorderPane = new BorderPane();
        Canvas gameCanvas = new Canvas();
        gameBorderPane.setLeft(gameCanvas);
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
        poly = new ArrayList();
        core = new Core(2);
        
        core.addPiece(new Ant(0), new Coord(0, 0));
        core.movePiece(new Coord(1, 1), new Coord(0, 1));
        System.err.println("toto"+core.getCurrentState().getBoard().getTile(new Coord(1, 1)).getPiece());
        
        System.out.println(core.getCurrentState().getBoard().getBoard().size());
        for(int i = 0; i<core.getCurrentState().getBoard().getBoard().size();i++){
            ArrayList<Polygon> tmp = new ArrayList<>();
            for(int j = 0; j<core.getCurrentState().getBoard().getBoard().get(i).size();j++){
                
               if(core.getCurrentState().getBoard().getBoard().get(i).get(j).size() != 0){
                   if (core.getCurrentState().getBoard().getBoard().get(i).get(j).get(0).getPiece()!=null)
                       tmp.add(addPoly(gameCanvas, x, y, i, j, size,Color.RED));
                   else{
                        tmp.add(addPoly(gameCanvas, x, y, i, j, size,Color.BLUE));
                   }
                }
               else
                   tmp.add(addPoly(gameCanvas, x, y, i, j, size,Color.BLACK));
                
            }
            poly.add(tmp);
        }
            
        /************************/
        
        /* Initialisation du tableau de polygones 
        poly = new ArrayList<>();
        for(double i = 0; i<3;i++){
            ArrayList<Polygon> tmp = new ArrayList<>();
            for(double j = 0;j<3;j++){
                //tmp.add(addPoly(gameCanvas, x, y, i, j, size));
            }
            poly.add(tmp);
        }
        /*******************************************/
        
        /* Clic sur le canvas principal */
        gameCanvas.setOnMousePressed(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent m) {
                Point2D fdp = new Point2D.Double(m.getX(), m.getY());
                for(int i = 0;i<poly.size();i++){
                    for(int j = 0;j<poly.get(i).size();j++){
                        if(poly.get(i).get(j).contains(fdp) && core.getCurrentState().getBoard().getTile(new Coord(i, j)) != null){
                            System.err.println("T'as cliqué dans un polygone");
                            if(core.getCurrentState().getBoard().getTile(new Coord(i, j)).getPiece()!=null){
                                System.err.println("Tu as cliqué sur le polygone  " + i + " " + j + " !  " + core.getCurrentState().getBoard().getTile(new Coord(i, j)).getPiece());
                            }
                            else{
                                System.err.println("Tile mais pas de pieces " + i + " " + j);
                            }
                        }
                    }
                } 
            }
        });
        /********************************/
        
        
        
    }
    
    public void initPoly(Core c){
        
    }
    
    public Polygon addPoly(Canvas can,double x,double y,double i,double j,double size, Color color){
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
        GraphicsContext gc = can.getGraphicsContext2D();
        
        int [] rx = new int[6];
        int [] ry = new int[6];
        xd[0] = originX; yd[0] = originY;
        xd[1] = originX+size;yd[1] = originY+size/2;
        xd[2] = originX+size;yd[2] = (originY+size)+size/2;
        xd[3] = originX;yd[3] = originY+2*size;
        xd[4] = originX-size;yd[4] = (originY+size)+size/2;
        xd[5] = originX-size;yd[5] = originY+size/2;
        gc.setStroke(color);
        gc.strokePolygon(xd, yd, 6);
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
