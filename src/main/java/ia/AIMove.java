/*
Method used as return type for IA to communicate action to core
If AddNewTile = true -> uses only piece & destination
               false -> uses only source & destination
 */
package main.java.ia;

import java.util.List;
import main.java.model.Core;
import main.java.model.Piece;
import main.java.model.State;
import main.java.utils.CoordGene;

public class AIMove {

    boolean AddNewTile; //true -> new tile added ; false -> tile in place moved
    CoordGene<Integer> source, destination;
    int piece;
    Core core;
    State state;
    StoringConfig originalConfig;
    //Constructor -> determines the move from the diff between the 2 configs
    public AIMove(StoringConfig origin, StoringConfig arrival, State state) {
        this.state = state;
        this.originalConfig = origin;
        
        int piece;
        CoordGene<Integer> source, destination;
        int nbPiecesOnBoardBefore = 0, nbPiecesOnBoardAfter = 0;
        //couting pieces on board before and after
        for (int i = 0; i < origin.config.length; i++) {
            if (origin.isOnBoard(i)) {
                nbPiecesOnBoardBefore++;
            }
            if (arrival.isOnBoard(i)) {
                nbPiecesOnBoardAfter++;
            }
        }
        //different number of tiles before & after -> means  a new tile was added
        if (nbPiecesOnBoardAfter != nbPiecesOnBoardBefore) {
            for (int i = 0; i < origin.config.length; i++) {
                if (!origin.isOnBoard(i) && arrival.isOnBoard(i)) {
                    this.piece = i;
                    if(this.piece > (originalConfig.config.length/2)){
                        this.piece -= originalConfig.config.length/2;
                    }
                    Integer destX, destY;
                    destX = (int) arrival.getX(i);
                    destY = (int) arrival.getY(i);
                    this.destination = new CoordGene<Integer>(destX, destY);
                    this.AddNewTile = true;
                    break;
                }
            }
        } else {
            //same number of tiles on board before & after -> means one tile moved
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

    public void setCore(Core newCore) {
        this.core = newCore;
    }

    public boolean play() {
        if (AddNewTile) {
            return core.addPiece(piece, destination);
        } else {
            return core.movePiece(source, destination);
        }
    }

    //translates the pieceID into the index of the piece in the player's inventory
    public int getPositionInInventory(int pieceId) {
        if (pieceId >= (originalConfig.config.length/2)){
            System.err.println("getPosInInv : avant modif pieceId="+pieceId);
            pieceId -= originalConfig.config.length/2;
            System.err.println("getPosInInv : apres modif pieceId="+pieceId);
        }
            
        List<Piece> inventory = state.getPlayers()[state.getCurrentPlayer()].getInventory();
        for (int i = 0; i < inventory.size(); i++) {
            if (inventory.get(i).getId() == pieceId) {
                return i;
            }
        }
        System.err.println("Erreur : impossible de trouver la piece " + pieceId + " dans l'inventaire du joueur " + state.getCurrentPlayer());
        return 0;
    }

    /*tostring a moitié foireux, il faut test les null avant de les  apppeler
    public String toString(){
        String result = "AIMove :\n"+"\tbool AddNewTile : "+AddNewTile+"\tpiece : "+piece+"\n\tSource :"+source.toString()+"\tDest :"+destination.toString()
                ;
        return result;
        
    }*/
}
