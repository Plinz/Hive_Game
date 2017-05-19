/*

 */
package main.java.ia;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import main.java.engine.Core;
import main.java.engine.Notation;
import main.java.model.Piece;
import main.java.model.Tile;
import main.java.utils.Consts;
import main.java.utils.CoordGene;

public abstract class AI {

    Core core;
    Heuristics heuristics;
    int AIPlayer;
    int[] choiceDuringOpening;
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
            System.out.println("Piece choisie : type= "+i+","+core.getTurn());
            for (Piece pieceFromInventory : core.getPlayers()[AIPlayer].getFirstPieceOfEachType()){
                if (pieceFromInventory.getId() == i){
                    piece = pieceFromInventory;
                    break;
                }
            }
            this.choiceDuringOpening[core.getTurn()/2] = i;
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
    
    public Tile chooseWhateverFreeTileOnBoard (int currentPlayer){
        List<Tile> tilesOnBoard = core.getBoard().getFreePiecesOnBoard(core.getCurrentPlayer());
        Random random = new Random();
        int rand = random.nextInt(tilesOnBoard.size());
        return tilesOnBoard.get(rand);
    }
    
    public String movePieceNearOpponent(Tile tile){
        List<CoordGene<Integer>> possibleDestinations = tile.getPiece().getPossibleMovement(tile, core.getBoard());
        List<CoordGene<Integer>> possibleDestinationsNearOpponent = new ArrayList<>();
        for (CoordGene<Integer> destination : possibleDestinations){
            for (Tile destinationNeighbor : core.getBoard().getPieceNeighbors(destination)){
                if (tile.getPiece().getTeam() == core.getCurrentPlayer()){
                    possibleDestinationsNearOpponent.add(destination);
                    break;                
                }
            }
        }
        if (!possibleDestinationsNearOpponent.isEmpty()){
            Random random = new Random();
            int rand = random.nextInt(possibleDestinationsNearOpponent.size());
            String move = Notation.getMoveNotation(core.getBoard(), tile.getPiece(), possibleDestinationsNearOpponent.get(rand));
            String unMove = Notation.getInverseMoveNotation(core.getBoard(), tile.getPiece());
            return move+";"+unMove;
        } else {
            return addPieceWherever(chooseAPiece(Consts.CHOOSE_WHATEVER));
        }
    }
}
