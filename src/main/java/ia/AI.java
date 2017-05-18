/*

 */
package main.java.ia;

import java.util.Random;
import main.java.engine.Core;
import main.java.model.Piece;

public abstract class AI {

    Core core;
    Heuristics heuristics;
    
    public AI(Core core) {
        this.core = core;
    }

    public String getNextMove() {
        System.err.println("Erreur : AI abstraite");
        return null;
    }

    public Piece addSomePiece(double[] proportions) {
        Piece piece = null;
        do{ 
            Random random = new Random();
            float rand = random.nextFloat();
            int i = -1;
            while (rand > 0) {
                i++;
                rand -= proportions[i];
            }
            
            for (Piece pieceFromInventory : core.getPlayers()[core.getMode()-1].getFirstPieceOfEachType()){
                if (piece.getId() == i){
                    piece = pieceFromInventory;
                    break;
                }
            }
        } while (piece == null);
        return piece;
    }
    
    
}
