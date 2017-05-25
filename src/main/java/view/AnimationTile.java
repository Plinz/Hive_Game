/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package main.java.view;

import javafx.animation.PathTransition;
import javafx.scene.image.Image;
import javafx.scene.layout.Pane;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.LineTo;
import javafx.scene.shape.MoveTo;
import javafx.scene.shape.Path;
import javafx.scene.shape.Polygon;
import javafx.util.Duration;
import main.java.engine.OptionManager;
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
    private TraducteurBoard traductor;
    private Hexagon hex;
    
    public AnimationTile(Pane pane, TraducteurBoard trad){
        hex = new Hexagon(); 
        polygon = new Polygon();
        calculPolygon();        
        pathAnimation = new PathTransition();
        if(OptionManager.isAnimationsEnable())
            pathAnimation.setDuration(Duration.seconds(1));
        else
            pathAnimation.setDuration(Duration.seconds(0));
        pathAnimation.setNode(polygon);
        panCanvas = pane;
        traductor = trad;
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
        polygon.getPoints().remove(0, polygon.getPoints().size());
        
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
    
    public void initMovingAnimation(Image image,CoordGene<Integer> coordStart, CoordGene<Integer> coordEnd){
        CoordGene<Double> start = new CoordGene<>((double) coordStart.getX(), (double) coordStart.getY());
        CoordGene<Double> end = new CoordGene<>((double) coordEnd.getX(), (double) coordEnd.getY());
        start = traductor.axialToPixel(start);
        end = traductor.axialToPixel(end);

        panCanvas.getChildren().add(this.getPolygon());

        this.setImagePolygon(image);
        this.setPath(new Path(
                new MoveTo(start.getX() + traductor.getMoveOrigin().getX(), start.getY() + traductor.getMoveOrigin().getY()),
                new LineTo(end.getX() + traductor.getMoveOrigin().getX(), end.getY() + traductor.getMoveOrigin().getY())));  
    }
    
    public void initPlacingAnimation(Image image,CoordGene<Integer> coordEnd){
        CoordGene<Double> start = new CoordGene<>((double) coordEnd.getX(), (double) 0);
        CoordGene<Double> end = new CoordGene<>((double) coordEnd.getX(), (double) coordEnd.getY());
        start = traductor.axialToPixel(start);
        end = traductor.axialToPixel(end);

        panCanvas.getChildren().add(this.getPolygon());
        this.setImagePolygon(image);
        this.setPath(new Path(
                new MoveTo(start.getX() + traductor.getMoveOrigin().getX(), 0),
                new LineTo(end.getX() + traductor.getMoveOrigin().getX(), end.getY() + traductor.getMoveOrigin().getY())));
    }
}