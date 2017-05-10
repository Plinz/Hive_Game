/*
Method used as return type for IA to communicate action to core
If AddNewTile = true -> uses only piece & destination
               false -> uses only source & destination
 */
package main.java.ia;

import main.java.model.Core;
import main.java.utils.CoordGene;

public class IaMove {
    boolean AddNewTile; //true -> new tile added ; false -> tile in place moved
    CoordGene<Integer> source, destination;
    int piece;
    Core core;
    
    public IaMove(int piece, CoordGene<Integer> destination){
        this.piece = piece;
        this.destination = destination;
    }
    
    public IaMove(CoordGene<Integer> source, CoordGene<Integer>destination){
        this.source = source;
        this.destination = destination;
    }
    
    public void play(){
        if (AddNewTile){
            core.addPiece(piece, destination);
        } else {
            core.movePiece(source, destination);
        }
    }
}
