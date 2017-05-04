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
import main.java.model.piece.Ant;
import main.java.utils.Coord;

/**
 *
 * @author gontardb
 */
public class InterfaceJavaFX extends Application{
    
    private Core core;
    

    @Override
    public void start(Stage primaryStage) throws Exception {
        
        /*Initialisation du core*/
        core = new Core(2);
        core.addPiece(new Ant(2), new Coord(0,0));
        System.err.println(core.getCurrentState().getBoard().getBoard().size());
        for(int i = 0; i < core.getCurrentState().getBoard().getBoard().size();i++){
            System.out.println(i);
        }
        /************************/
        
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
        
        /* Initialisation du tableau de polygones */
        double size = 40;
        double x = 70;
        double y = 30;
        ArrayList<ArrayList<Polygon>> poly = new ArrayList<>();
        for(double i = 0; i<6;i++){
            ArrayList<Polygon> tmp = new ArrayList<>();
            for(double j = 0;j<6;j++){
                if(j%2 == 0){
                      tmp.add(drawPoly(gameCanvas,x+(2*size*i)+((2*size)*Math.floor(j/2)),y+(size*j)+(j*(size/2)),size));   
                }
                else
                    tmp.add(drawPoly(gameCanvas,x+(2*size*i)+size+((2*size)*Math.floor(j/2)),y+(size*j)+(j*(size/2)),size));   
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
                        if(poly.get(i).get(j).contains(fdp))
                            System.err.println("Tu as cliqué sur le polygone  " + i + " " + j + " !");
                    }
                } 
            }
        });
        /********************************/
        
        
        
    }
    
    public Polygon drawPoly(Canvas can,double originX,double originY ,double size){
              GraphicsContext gc = can.getGraphicsContext2D();
              gc.setStroke(Color.BLACK);
              double[] x = new double[6];
              double[] y = new double[6];
              int [] rx = new int[6];
              int [] ry = new int[6];
              x[0] = originX; y[0] = originY;
              x[1] = originX+size;y[1] = originY+size/2;
              x[2] = originX+size;y[2] = (originY+size)+size/2;
              x[3] = originX;y[3] = originY+2*size;
              x[4] = originX-size;y[4] = (originY+size)+size/2;
              x[5] = originX-size;y[5] = originY+size/2;
              gc.strokePolygon(x, y, 6);
              for(int i = 0; i<6;i++){
                  rx[i] = (int) x[i];
                  ry[i] = (int) y[i];
              }
              return new Polygon(rx, ry, 6);
    }
    
    public static void creer(String[] args) {
        launch(args);
    }
    
    
    
}
