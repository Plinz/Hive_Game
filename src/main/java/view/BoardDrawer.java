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
import main.java.model.Piece;
import main.java.model.Tile;
import main.java.model.Visitor;
import main.java.utils.CoordGene;


public class BoardDrawer extends Visitor {
    Canvas can;
    GraphicsContext gc;
    TraducteurBoard traducteur;
    
    public BoardDrawer(Canvas c){
        this.can = c;
        this.gc = can.getGraphicsContext2D(); 
        traducteur = new TraducteurBoard();
    }
    
    public BoardDrawer(Canvas c, TraducteurBoard t){
        this.can = c;
        this.gc = can.getGraphicsContext2D(); 
        traducteur = t;
    }
     
    public boolean visit(Board b){
        gc.clearRect(0, 0, can.getWidth(), can.getHeight());
        
        return false;
    }
    
    public boolean visit(Tile t){
              
              double sizeHex = traducteur.getSizeHex();
              
              double a = Math.sqrt((sizeHex*sizeHex)- ((sizeHex/2)*(sizeHex/2)));             
                    
              CoordGene<Double> coord = new CoordGene((double)t.getX(),(double)t.getY()); 
              CoordGene<Double> coordPix =traducteur.axialToPixel(coord);
              
              double X = coordPix.getX()+sizeHex;
              double Y = coordPix.getY()+sizeHex;
              
              double[] x = new double[6];
              double[] y = new double[6];
              
              x[0] = X ; y[0] = Y-sizeHex;
              x[1] = X +a; y[1] = Y - (sizeHex/2);
              x[2] = X +a; y[2] = Y+ (sizeHex/2);
              x[3] = X; y[3] = Y+ sizeHex;
              x[4] = X - a; y[4] = Y + (sizeHex/2);
              x[5] = X - a; y[5] = Y-(sizeHex/2);
              
                Piece piece = t.getPiece(); 
                if(piece !=null){
                String name = getClass().getClassLoader().getResource("main/resources/img/tile/"+piece.getName()+piece.getTeam()+".png").toString();
                gc.setFill(new ImagePattern(new Image(name)));
                gc.setStroke(Color.RED);
                gc.strokePolygon(x, y, 6);
                gc.fillPolygon(x, y, 6);      
              }else{
                  gc.setStroke(Color.BLACK);   
                  gc.strokePolygon(x, y, 6);
              }
              
        return false;
    }

    @Override
    public boolean visit(CoordGene<Integer> c) {
             double sizeHex = traducteur.getSizeHex();
              
              double a = Math.sqrt((sizeHex*sizeHex)- ((sizeHex/2)*(sizeHex/2)));
              gc.setStroke(Color.BLACK);
                    
              CoordGene<Double> coord = new CoordGene((double)c.getX(),(double)c.getY()); 
              CoordGene<Double> coordPix =traducteur.axialToPixel(coord);
              
              double X = coordPix.getX()+sizeHex;
              double Y = coordPix.getY()+sizeHex;
              
              double[] x = new double[6];
              double[] y = new double[6];
              
              x[0] = X ; y[0] = Y-sizeHex;
              x[1] = X +a; y[1] = Y - (sizeHex/2);
              x[2] = X +a; y[2] = Y+ (sizeHex/2);
              x[3] = X; y[3] = Y+ sizeHex;
              x[4] = X - a; y[4] = Y + (sizeHex/2);
              x[5] = X - a; y[5] = Y-(sizeHex/2);
              

              gc.setGlobalAlpha(0.5);
              gc.setFill(Color.BLUE);
              gc.fillPolygon(x, y, 6);
              gc.setGlobalAlpha(1);
              gc.strokePolygon(x, y, 6);       
        return false;
    }

    @Override
    protected boolean visit(Piece p) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    /*
    private List<List<Double>> drawPolygon(){
         double sizeHex = traducteur.getSizeHex();
              
              double a = Math.sqrt((sizeHex*sizeHex)- ((sizeHex/2)*(sizeHex/2)));
              gc.setStroke(Color.BLACK);
                    
              CoordGene<Double> coord = new CoordGene((double)c.getX(),(double)c.getY()); 
              CoordGene<Double> coordPix =traducteur.axialToPixel(coord);
              
              double X = coordPix.getX()+sizeHex;
              double Y = coordPix.getY()+sizeHex;
              
              double[] x = new double[6];
              double[] y = new double[6];
              
              x[0] = X ; y[0] = Y-sizeHex;
              x[1] = X +a; y[1] = Y - (sizeHex/2);
              x[2] = X +a; y[2] = Y+ (sizeHex/2);
              x[3] = X; y[3] = Y+ sizeHex;
              x[4] = X - a; y[4] = Y + (sizeHex/2);
              x[5] = X - a; y[5] = Y-(sizeHex/2);
              
              List<List<Double>> polygonPoints = new ArrayList<List<Double>>();
              polygonPoints.
        return polygonPoints;
           
    }*/

}

/*
package main.java.view;

import java.util.ArrayList;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import main.java.model.Board;
import main.java.model.Core;
import main.java.model.Piece;
import main.java.model.Tile;
import main.java.model.Visitor;
import main.java.utils.Consts;
import main.java.utils.Coord;
import main.java.utils.CoordGene;


public class BoardDrawer {
    Canvas can;
    GraphicsContext gc;
    TraducteurBoard traducteur;
    
    public BoardDrawer(Canvas c){
        this.can = c;
        this.gc = can.getGraphicsContext2D(); 
        traducteur = new TraducteurBoard();
    }
    
    public boolean visit(Core c){
        return false;
    }
    
    public boolean visit(Board b){
        gc.clearRect(0, 0, can.getWidth(), can.getHeight());
        return false;
    }
    
    
    public boolean visit(Tile t){
        Piece piece = t.getPiece();
        if (piece != null){
            drawPolygon(Consts.X_ORIGIN, Consts.Y_ORIGIN, t.getX(), t.getY(),Consts.SIDE_SIZE,piece.getName()+piece.getTeam()+".png");
        }
        else{
            drawPolygon(Consts.X_ORIGIN, Consts.Y_ORIGIN, t.getX(), t.getY(), Consts.SIDE_SIZE,"Placable");
        }
        
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
            case "Opaque" : 
                gc.setGlobalAlpha(0.5);
                gc.setFill(Color.BLUE);
                gc.fillPolygon(xd, yd, 6);
                gc.setGlobalAlpha(1);
                break;
            case "Selected" :
                gc.setGlobalAlpha(0.5);
                gc.setFill(Color.RED);
                gc.fillPolygon(xd, yd, 6);
                gc.setGlobalAlpha(1);
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
*/
