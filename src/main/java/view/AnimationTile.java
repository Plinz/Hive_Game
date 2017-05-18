/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package main.java.view;

import javafx.animation.PathTransition;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.image.Image;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
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
    private Hexagon hex;
    
    public AnimationTile(){
        hex = new Hexagon(); 
        polygon = new Polygon();
        calculPolygon();        
        pathAnimation = new PathTransition();
        pathAnimation.setDuration(Duration.seconds(0));
        pathAnimation.setNode(polygon);
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
    
    public void setImagePolygon(Image image){     
        polygon.setFill(new ImagePattern(image));
    }

    public PathTransition getPathAnimation() {
        return pathAnimation;
    }

    public void setPathAnimation(PathTransition pathAnimation) {
        this.pathAnimation = pathAnimation;
    }      
        
    public void play(){
        calculPolygon();
        pathAnimation.setPath(path);
        pathAnimation.play();
    }
    private void calculPolygon(){
        
        hex.setxPixel(0.0);
        hex.setyPixel(0.0);
        hex.calculHex(); 
        
        double x[] =  hex.getListXCoord();
        double y[] =  hex.getListYCoord();
        
        polygon.getPoints().addAll(new Double[]{
                    x[0], y[0],
                    x[1], y[1],
                    x[2], y[2],
                    x[3], y[3],
                    x[4], y[4],
                    x[5], y[5] }); 
    }
    
}