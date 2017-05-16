/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package main.java.implement;

import javafx.animation.Interpolator;
import javafx.animation.PathTransition;
import javafx.animation.Timeline;
import javafx.animation.Transition;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.CubicCurveTo;
import javafx.scene.shape.LineTo;
import javafx.scene.shape.MoveTo;
import javafx.scene.shape.Path;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.QuadCurveTo;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import javafx.util.Duration;
import main.java.utils.Consts;
import main.java.utils.CoordGene;
import main.java.view.TraducteurBoard;

/**
 *
 * @author duvernet
 */
public class TestAnimation extends Application {

    
    public void start(final Stage primaryStage) { 
            TraducteurBoard traducteur = new TraducteurBoard();
            double sizeHex = Consts.SIDE_SIZE;
              
              double a = Math.sqrt((sizeHex*sizeHex)- ((sizeHex/2)*(sizeHex/2)));             
                    
              CoordGene<Double> coord = new CoordGene(0.0,0.0); 
              CoordGene<Double> coordPix =traducteur.axialToPixel(coord);
              
              
              double X = coordPix.getX();
              double Y = coordPix.getY();
              
              double[] x = new double[6];
              double[] y = new double[6];
              
              x[0] = X ; y[0] = Y-sizeHex;
              x[1] = X +a; y[1] = Y - (sizeHex/2);
              x[2] = X +a; y[2] = Y+ (sizeHex/2);
              x[3] = X; y[3] = Y+ sizeHex;
              x[4] = X - a; y[4] = Y + (sizeHex/2);
              x[5] = X - a; y[5] = Y-(sizeHex/2);
              
        Polygon polygon = new Polygon();
        polygon.getPoints().addAll(new Double[]{
                    x[0], y[0],
                    x[1], y[1],
                    x[2], y[2],
                    x[3], y[3],
                    x[4], y[4],
                    x[5], y[5] });

        polygon.setFill(Color.RED); 
        final Path path = new Path( 
                new MoveTo(50, 50), 
                new LineTo(100, 50), 
                new LineTo(150, 150), 
                new QuadCurveTo(150, 100, 250, 200), 
                new CubicCurveTo(0, 250, 400, 0, 300, 250)); 
        
        Canvas gameCanvas = new Canvas(800, 800);
        //ScrollPane scrollPane = new ScrollPane(gameCanvas);
        gameCanvas.getGraphicsContext2D().setFill(Color.BLUE);
        gameCanvas.getGraphicsContext2D().strokeRect(20,20, gameCanvas.getWidth()/4, gameCanvas.getHeight()/4);
        gameCanvas.getGraphicsContext2D().fillRect(0, 0, gameCanvas.getWidth()/2, gameCanvas.getHeight()/2);
        
        final Pane root = new Pane(path,gameCanvas,polygon); 
        final Scene scene = new Scene(root, 350, 300); 
        primaryStage.setTitle("Test d'animation le long d'un chemin"); 
        primaryStage.setScene(scene); 
        primaryStage.show(); 
        
        final Transition pathAnimation = new PathTransition(Duration.seconds(10), path, polygon);   
 
        pathAnimation.play(); 
    } 
  
    public static void main(String[] args) { 
        launch(args); 
    } 
    
}
