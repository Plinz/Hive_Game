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

    public IaMove(int piece, CoordGene<Integer> destination) {
        this.piece = piece;
        this.destination = destination;
    }

    public IaMove(CoordGene<Integer> source, CoordGene<Integer> destination) {
        this.source = source;
        this.destination = destination;
    }

    //Constructor -> determines the move from the diff between the 2 configs
    public IaMove(StoringConfig origin, StoringConfig arrival) {
        int piece;
        CoordGene<Integer> source, destination;
        int piecesOnBoardBefore = 0, piecesOnBoardAfter = 0;
        //couting pieces on board before and after
        for (int i = 0; i < origin.config.length; i++) {
            if (origin.isOnBoard(i)) {
                piecesOnBoardBefore++;
            }
            if (arrival.isOnBoard(i)) {
                piecesOnBoardAfter++;
            }
        }

        if (piecesOnBoardAfter == piecesOnBoardBefore) {
            for (int i = 0; i < origin.config.length; i++) {
                if (!origin.isOnBoard(i) && arrival.isOnBoard(i)) {
                    this.piece = i;
                    Integer destX, destY;
                    destX = (int) arrival.getX(i);
                    destY = (int) arrival.getY(i);
                    this.destination = new CoordGene<Integer>(destX, destY);
                    this.AddNewTile = true;
                    break;
                }
            }
        } else {
            for (int i = 0; i < origin.config.length; i++) {
                if ((origin.getX(i) != arrival.getX(i)) || (origin.getY(i) != arrival.getY(i))) {
                    Integer sourceX = (int) origin.getX(i);
                    Integer sourceY = (int) origin.getY(i);
                    Integer destX = (int) arrival.getX(i);
                    Integer destY = Integer.valueOf((int) arrival.getY(i));
                    this.source = new CoordGene<>(sourceX, sourceY);
                    this.destination = new CoordGene<>(destX, destY);
                    this.AddNewTile = false;
                    break;
                }
            }
        }
    }

    public void play() {
        if (AddNewTile) {
            core.addPiece(piece, destination);
        } else {
            core.movePiece(source, destination);
        }
    }
}
