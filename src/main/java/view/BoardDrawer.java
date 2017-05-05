/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package main.java.view;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import main.java.model.Board;
import main.java.model.Core;
import main.java.model.Tile;
import main.java.utils.Consts;

/**
 *
 * @author gontardb
 */
public class BoardDrawer {
    Canvas can;
    GraphicsContext gc;
    
    public BoardDrawer(Canvas c){
        this.can = c;
        this.gc = can.getGraphicsContext2D();   
    }
    
    public boolean visit(Core c){
        gc.clearRect(0, 0, can.getWidth(), can.getHeight());
        return false;
    }
    
    public boolean visit(Board b){
        for(int i = 0;i<b.getBoard().size();i++){
            for(int j = 0;j<b.getBoard().get(i).size();j++){
                if(b.getBoard().get(i).get(j).size() != 0){
                    if (b.getBoard().get(i).get(j).get(0).getPiece() != null){
                        if(b.getBoard().get(i).get(j).get(0).getPiece().getTeam() == 0)
                            drawPolygon(Consts.X_ORIGIN, Consts.Y_ORIGIN, i, j,Consts.SIDE_SIZE,"Tile_Empty_White.png");
                        else 
                            drawPolygon(Consts.X_ORIGIN, Consts.Y_ORIGIN, i, j,Consts.SIDE_SIZE,"Tile_Empty_Black.png");
                    }
                   else{
                        drawPolygon(Consts.X_ORIGIN, Consts.Y_ORIGIN, i, j, Consts.SIDE_SIZE,"Placable");
                   }
                }
                else{
                    drawPolygon(Consts.X_ORIGIN, Consts.Y_ORIGIN, i, j, Consts.SIDE_SIZE,"Vide");
                }
            }
        } 
        return false;
    }
    
    public boolean visit(Tile t){
        
        
        return false;
    }
    
    public void drawPolygon(double x, double y, double i, double j, double size,String name){
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
        
        xd[0] = originX; yd[0] = originY;
        xd[1] = originX+size;yd[1] = originY+size/2;
        xd[2] = originX+size;yd[2] = (originY+size)+size/2;
        xd[3] = originX;yd[3] = originY+2*size;
        xd[4] = originX-size;yd[4] = (originY+size)+size/2;
        xd[5] = originX-size;yd[5] = originY+size/2;
        switch (name) {
            case "Placable":
                gc.setStroke(Color.BLUE);
                gc.strokePolygon(xd, yd, 6);
                break;
            case "Vide":
                gc.setStroke(Color.GREY);
                gc.strokePolygon(xd, yd, 6);
                break;
            default:
                gc.setFill(new ImagePattern(new Image(name)));
                gc.setStroke(Color.RED);
                gc.strokePolygon(xd, yd, 6);
                gc.fillPolygon(xd, yd, 6);
                break;
        }
    }
}
