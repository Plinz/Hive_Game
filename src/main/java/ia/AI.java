/*

 */
package main.java.ia;

import java.util.List;
import java.util.Random;
import main.java.engine.Core;
import main.java.engine.Notation;
import main.java.model.Piece;
import main.java.utils.CoordGene;

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

    public Piece chooseAPiece(double[] proportions) {
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
    
    public String addPieceWherever(Piece piece){
        List<CoordGene<Integer>> possibleAddCoords = core.getPossibleAdd(core.getCurrentPlayer());
        Random random = new Random();
        int rand = random.nextInt(possibleAddCoords.size());
        String move = Notation.getMoveNotation(core.getBoard(), piece, possibleAddCoords.get(rand));
        String unMove = Notation.getInverseMoveNotation(core.getBoard(), piece);
        return move+";"+unMove;
    }
    
    
}
