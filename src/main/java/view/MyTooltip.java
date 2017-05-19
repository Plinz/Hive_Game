/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package main.java.view;

import java.util.List;
import javafx.scene.image.Image;
import javafx.scene.layout.Pane;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.Rectangle;
import main.java.model.Tile;

/**
 *
 * @author duvernet
 */
public class MyTooltip {
    private List<Polygon> polygons;
    private Hexagon hex;
    private List<Tile> listTiles;
    private Rectangle box;
    private Pane panCanvas;
    
    public MyTooltip(List<Tile> listTiles,Pane panCanvas){
       this.listTiles = listTiles;
       this.panCanvas = panCanvas;
       hex = new Hexagon();
       hex.setxPixel(0.0);
       hex.setyPixel(0.0);
       hex.calculHex(); 
       
       
        double x[] =  hex.getListXCoord();
        double y[] =  hex.getListYCoord();
        
        for(Tile tile : listTiles){
            Polygon polygon = new Polygon();    
            polygon.getPoints().addAll(new Double[]{
            x[0], y[0],
            x[1], y[1],
            x[2], y[2],
            x[3], y[3],
            x[4], y[4],
            x[5], y[5] }); 
            
            Image image = new Image (getClass().getClassLoader().getResource("main/resources/img/tile/"+tile.getPiece().getName()+tile.getPiece().getTeam()+".png").toString());
            polygon.setFill(new ImagePattern(image));
            panCanvas.getChildren().add(polygon);
            //panCanvas.getChildren().
        }
                                

         
         
    }
    
    
}
