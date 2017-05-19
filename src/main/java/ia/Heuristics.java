/*
Heuristic Data : heuristicData[player][insect][typeHeuristique]
    avec player 0 -> IA
         player 1 -> opponent
                insect -> 0 queen, 1 spider, 2 grasshopper, 3 beetle, 4 ant, 5 general
                        et type ->  0 valid move (deplacement et placement)
                                    1 valid placement
                                    2 valid movement (deplacement)
                                    3 voisins (encerclement)
                                    4 en main
                                    5 en jeu
                                    6 pinned

 */
package main.java.ia;

import java.util.ArrayList;
import java.util.List;
import main.java.engine.Core;
import main.java.model.Board;
import main.java.model.Box;
import main.java.model.Column;
import main.java.model.Piece;
import main.java.model.Player;
import main.java.model.Tile;
import main.java.utils.Consts;

public class Heuristics {

    int maxdepth;
    Core core;
    int AIPlayer;
    int nbConfigsStudied;
    int mobilityAI, mobilityOpponent, possiblePlacements;
    List<List<List<Double>>> heuristicData;

    /*
     *                  CONSTUCTOR
     */
    public Heuristics(Core core) {
        this.core = core;
        
        heuristicData = new ArrayList<>(2);
        for (List<List<Double>> listListDouble : heuristicData){
            listListDouble = new ArrayList<>(6);
            for (List<Double> listDouble : listListDouble){
                listDouble = new ArrayList<>(7);
            }
        }
    }

    /*
     *                  METHODE OVERIDE IN CHILD HEURISTICS
     */
    public int getHeuristicsValue() {
        System.err.println("Erreur : getHeuristics value ne doit as être appelée depuis la classe mère");
        return 0;
    }

    public int getValuePieceInHand(int pieceId, int player) {
        System.err.println("Erreur : getHeuristics value ne doit as être appelée depuis la classe mère");
        return 0;
    }

    public int getValuePieceOnBoard(int pieceId, int player) {
        System.err.println("Erreur : getHeuristics value ne doit as être appelée depuis la classe mère");
        return 0;
    }

    /*
     *                  GETTERS
     */
    public int getNbPiecesAroundQueen(int player) {
        if (core.getPlayers()[player].getInventory().stream().noneMatch(piece -> piece.getId() == Consts.QUEEN)) {
            return core.getBoard().getPieceNeighbors(getTile(Consts.QUEEN, player)).size();
        } else {
            return 0;
        }
    }

    public Tile getTile(int pieceId, int player) {
        Board board = core.getBoard();
        for (Column column : board.getBoard()) {
            for (Box box : column) {
                for (Tile tile : box) {
                    if (tile.getPiece() != null && tile.getPiece().getId() == pieceId && tile.getPiece().getTeam() == player) {
                        return tile;
                    }
                }
            }
        }
        return null;
    }

    public int getMobility(Player player) {
        int result = 0;
        for (Column column : this.core.getBoard().getBoard()) {
            for (Box box : column) {
                for (Tile tile : box) {
                    if ((tile.getPiece() != null) && (tile.getPiece().getTeam() == player.getTeam())) {
                        result++;
                    }
                }
            }
        }
        return result;
    }

    public int getValue(int pieceId, int player) {
        int value = 0;
        if (isInHand(pieceId, player)) {
            value += getValuePieceInHand(pieceId, player);
        } else {
            value += getValuePieceOnBoard(pieceId, player);
        }
        return value;
    }

    public boolean isPinned(int PieceId) {

        //bug on the ground (ie not beetle or mosquito)
        for (Column column : this.core.getBoard().getBoard()) {
            for (Box box : column) {
                for (Tile tile : box) {
                    if ((tile.getPiece() != null) && (tile.getPiece().getId() == PieceId)) {
                        if (tile.getPiece().getPossibleMovement(tile, this.core.getBoard()).isEmpty()) {
                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }

    public boolean isInHand(int pieceId, int player) {
        for (Piece piece : core.getPlayers()[player].getInventory()) {
            if (piece.getId() == pieceId) {
                return true;
            }
        }
        return false;
    }

    public void resetValues(){
        mobilityAI =0;
        mobilityOpponent=0;
        possiblePlacements =0;
        
    }
    
    public void incrementMobility(int player){
        if (core.getCurrentPlayer() == AIPlayer){
            mobilityAI ++;
        } else {
            mobilityOpponent ++;
        }
    }
    /*
    public boolean isPinned(int PieceId){
        if (gameConfig.getPieces()[PieceId].stuck) return true;
        //bug on the ground (ie not beetle or mosquito)
        if (gameConfig.IdToType(PieceId) != Consts.BEETLE_TYPE
                && gameConfig.IdToType(PieceId) != Consts.MOSQUITO_TYPE)
            return (gameConfig.getPieces()[PieceId].PossibleDestinationsCalculated &&
                    gameConfig.getPieces()[PieceId].PossibleDestinations.size() == 0);
        else
            return (gameConfig.getPieces()[PieceId].PossibleDestinationsCalculated &&
                    gameConfig.getPieces()[PieceId].PossibleCubeDestinations.size() == 0);
    }*/
}
