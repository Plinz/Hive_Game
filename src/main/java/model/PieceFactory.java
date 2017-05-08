/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package main.java.model;

import main.java.model.piece.*;

/**
 *
 * @author hyperu
 */
public class PieceFactory {
    
    public static Piece create(String pieceToCreate){
        switch(pieceToCreate){
            case "Queen0":
                return new Queen(0);
            case "Queen1":
                return new Queen(1);
            case "Spider0":
                return new Spider(0);
            case "Spider1":
                return new Spider(1);
            case "Beetle0":
                return new Beetle(0);
            case "Beetle1":
                return new Beetle(1);
            case "Ant0":
                return new Ant(0);
            case "Ant1":
                return new Ant(1);
            case "Grasshopper0":
                return new Grasshopper(0);
            case "Grasshopper1":
                return new Grasshopper(1);    
            default: 
                break;
        }
        return null;
    }
    
}
