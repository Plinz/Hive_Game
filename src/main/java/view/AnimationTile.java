/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package main.java.view;

import javafx.animation.PathTransition;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Path;
import javafx.scene.shape.Polygon;
import javafx.util.Duration;
import main.java.utils.CoordGene;

/**
 *
 * @author duvernet
 */
public class AnimationTile {
    private Path path;
    private PathTransition pathAnimation;
    private Polygon polygon;
    private Pane panCanvas;
    private TraducteurBoard traducteur;
    private Hexagon hex;
    
    public AnimationTile(Pane panCanvas,TraducteurBoard traducteur){
                
        hex = new Hexagon(0.0,0.0); 
        double x[] =  hex.getListXCoord();
        double y[] =  hex.getListYCoord();
        
        polygon = new Polygon();
        
        polygon.getPoints().addAll(new Double[]{
                    x[0], y[0],
                    x[1], y[1],
                    x[2], y[2],
                    x[3], y[3],
                    x[4], y[4],
                    x[5], y[5] });     
        
        pathAnimation = new PathTransition();
        pathAnimation.setOnFinished(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent t) {
                    panCanvas.getChildren().remove(polygon);
                }
        });
    };

    /**
     * @return the path
     */
    public Path getPath() {
        return path;
    }
    
    /**
     * @param path the path to set
     */
    public void setPath(Path path) {
        this.path = path;
    }

    /**
     * @return the polygon
     */
    public Polygon getPolygon() {
        return polygon;
    }

    /**
     * @return the pathAnimation
     */
    public PathTransition getPathAnimation() {
        return pathAnimation;
    }
    
}
