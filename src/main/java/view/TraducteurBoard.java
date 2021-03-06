/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package main.java.view;

import main.java.utils.Consts;
import main.java.utils.CoordGene;
import main.java.utils.Cube;

/**
 *
 * @author duvernet
 */
public class TraducteurBoard {
    //private double Consts.SIDE_SIZE = Consts.SIDE_SIZE;
    private CoordGene<Double> centerPix;
    private CoordGene<Double> centerAxial;
    private CoordGene<Double> moveOrigin; 
    
    public TraducteurBoard(){
        Consts.SIDE_SIZE = 50;
        moveOrigin = new CoordGene<Double>(Consts.SIDE_SIZE,Consts.SIDE_SIZE);
    }
 
    private CoordGene<Double> cubeToAxial(Cube<Double> cube){
        double q = cube.getX();
        double r =  cube.getZ();
                
        return  new CoordGene<>(q,r);
    }

    private Cube<Double> axialToCube(CoordGene<Double> coord){
        double x = coord.getX();
        double z =  coord.getY();
        double y =  -x-z;
         
        return  new Cube<>(x,y,z);
    }
    
    public CoordGene<Double> axialToPixel(CoordGene<Double> hex){
       
        double x = Consts.SIDE_SIZE * Math.sqrt(3) * (hex.getX() + hex.getY()/2);
        double y = Consts.SIDE_SIZE * 3/2 * hex.getY();
        
        return new CoordGene<>(x,y);
        
    }
    
    public CoordGene<Double> pixelToAxial(CoordGene<Double> pix){
      
        double q =  (((pix.getX()-getMoveOrigin().getX())* Math.sqrt(3)/3 - (pix.getY()-getMoveOrigin().getY())/ 3) / Consts.SIDE_SIZE);
        double r =  ((pix.getY()-getMoveOrigin().getY())*  2/3 / Consts.SIDE_SIZE);

        CoordGene<Double> hex = new CoordGene<>(q,r); 
        hex = hexRound(hex);
            
        //System.out.println("RX :"+(hex.getX() )+ " RY " + (hex.getY()));
            
        return hex;
    }
   
            
    private Cube<Double> cubeRound(Cube<Double> cube){
        double rx = Math.round(cube.getX());
        double ry = Math.round(cube.getY());
        double rz = Math.round(cube.getZ());
                           
        double x_diff = Math.abs(rx - cube.getX());
        double y_diff = Math.abs(ry - cube.getY());
        double z_diff = Math.abs(rz - cube.getZ());
                    
        if(x_diff > y_diff && x_diff > z_diff)
            rx = -ry-rz;
        else if(y_diff > z_diff)
            ry = -rx-rz;
        else
            rz = -rx-ry;
        
        return new Cube<>(rx,ry,rz);           
    }
    
    private CoordGene<Double> hexRound(CoordGene<Double> coord){
        return cubeToAxial(cubeRound(axialToCube(coord)));
    }

    /**
     * @return the moveOrigin
     */
    public CoordGene<Double> getMoveOrigin() {
        return moveOrigin;
    }

    /**
     * @param moveOrigin the moveOrigin to set
     */
    public void setMoveOrigin(CoordGene<Double> moveOrigin)  {
        //if(moveOrigin.getX()>=Consts.SIDE_SIZE && moveOrigin.getY()>=Consts.SIDE_SIZE)
            this.moveOrigin = moveOrigin;
        //else
          //  throw new IndexOutOfBoundsException("Attention la carte risque d'être mal affichée");
    }
    
}
