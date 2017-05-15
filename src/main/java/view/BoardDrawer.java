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
        traducteur.setMoveOrigin(new CoordGene<Double>(can.getWidth()/2,can.getHeight()/2));
    }
     
    public boolean visit(Board b){
        gc.clearRect(0, 0, can.getWidth(), can.getHeight());   
        String name = getClass().getClassLoader().getResource("main/resources/img/misc/metal.jpg").toString();
        gc.setFill(new ImagePattern(new Image(name)));
        gc.strokeRect(0, 0, can.getWidth() , can.getHeight());
        gc.fillRect(0, 0, can.getWidth() , can.getHeight());
        return false;
    }
    
    public boolean visit(Tile t){
              
              double sizeHex = traducteur.getSizeHex();
              
              double a = Math.sqrt((sizeHex*sizeHex)- ((sizeHex/2)*(sizeHex/2)));             
                    
              CoordGene<Double> coord = new CoordGene((double)t.getX(),(double)t.getY()); 
              CoordGene<Double> coordPix =traducteur.axialToPixel(coord);
              
              
              double X = coordPix.getX()+traducteur.getMoveOrigin().getX();
              double Y = coordPix.getY()+traducteur.getMoveOrigin().getY();
              
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
              
              double X = coordPix.getX()+traducteur.getMoveOrigin().getX();
              double Y = coordPix.getY()+traducteur.getMoveOrigin().getY();
              
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
}