/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package main.java.view;

import main.java.utils.Consts;

/**
 *
 * @author duvernet
 */
public class Hexagon {
    
    private double xPixel;
    private double yPixel;
    private double[] listXCoord = new double[6];
    private double[] listYCoord = new double[6];
    private double a;
    
    public Hexagon(Double xPixel, Double yPixel){
        
        listXCoord = new double[6];
        listYCoord = new double[6];
        a = Math.sqrt((Consts.SIDE_SIZE*Consts.SIDE_SIZE)- ((Consts.SIDE_SIZE/2)*(Consts.SIDE_SIZE/2))); 
        this.xPixel = xPixel;
        this.yPixel = yPixel;
    }

    public Hexagon() {
        listXCoord = new double[6];
        listYCoord = new double[6];      
    }

    /**
     * @return the Consts.SIDE_SIZE
     */
    public double getSizeHex() {
        return Consts.SIDE_SIZE;
    }

    /**
     * @return the listXCoord
     */
    public double[] getListXCoord() {
        return listXCoord;
    }

    /**
     * @return the listYCoord
     */
    public double[] getListYCoord() {
        return listYCoord;
    }

    /**
     * @param xPixel the xPixel to set
     */
    public void setxPixel(double xPixel) {
        this.xPixel = xPixel;
    }

    /**
     * @param yPixel the yPixel to set
     */
    public void setyPixel(double yPixel) {
        this.yPixel = yPixel;
    }
    
    /**
     * Calcul a list of X coordinates and Y coordinates for the points of hexagon
     */
    public void calculHex(){
        a = Math.sqrt((Consts.SIDE_SIZE*Consts.SIDE_SIZE)- ((Consts.SIDE_SIZE/2)*(Consts.SIDE_SIZE/2)));
        listXCoord[0] = xPixel;
        listYCoord[0] = yPixel - Consts.SIDE_SIZE;
        listXCoord[1] = xPixel + a;
        listYCoord[1] = yPixel - (Consts.SIDE_SIZE / 2);
        listXCoord[2] = xPixel + a;
        listYCoord[2] = yPixel + (Consts.SIDE_SIZE / 2);
        listXCoord[3] = xPixel;
        listYCoord[3] = yPixel + Consts.SIDE_SIZE;
        listXCoord[4] = xPixel - a;
        listYCoord[4] = yPixel + (Consts.SIDE_SIZE / 2);
        listXCoord[5] = xPixel - a;
        listYCoord[5] = yPixel - (Consts.SIDE_SIZE / 2); 
        
    }
    
    
}
