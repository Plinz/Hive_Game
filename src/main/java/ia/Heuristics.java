/*

 */
package main.java.ia;

import main.java.engine.Core;
import main.java.model.Board;
import main.java.model.Box;
import main.java.model.Column;
import main.java.model.Piece;
import main.java.model.Tile;
import main.java.utils.Consts;



public class Heuristics {
    
    int maxdepth;
    Core core;

    /*
     *                  CONSTUCTOR
     */
    public Heuristics(Core core) {
        this.core = core;
    }

    /*
     *                  GETTERS
     */
    public int getNbPiecesAroundQueen(int player) {
        if (core.getPlayers()[player].getInventory().stream().noneMatch(piece -> piece.getId() == Consts.QUEEN))
            return core.getBoard().getPieceNeighbors(getTile(Consts.QUEEN, player)).size();
        else 
            return 0;
    }

    public Tile getTile(int pieceId, int player){
        Board board = core.getBoard();
        for (Column column : board.getBoard()){
            for (Box box : column) {
                for (Tile tile : box){
                    if (tile.getPiece() != null && tile.getPiece().getId() == pieceId && tile.getPiece().getTeam() == player){
                        return tile;
                    }
                }
            }
        }
        return null;
    }
    public int getHeuristicsValue() {
        System.err.println("Erreur : getHeuristics value ne doit as être appelée depuis la classe mère");
        return 0;
    }

    
    /*public int getMobility(int player)
    {
        int result = 0;
        int start = player * gameConfig.nbPiecesPerColor;
        int finish = start + gameConfig.nbPiecesPerColor;
        for (int i = start; i < finish; i++) 
        {
            if ((gameConfig.getNode(i).isOnBoard) && (!(gameConfig.getPossibleDestinations(gameConfig.getNode(i)).isEmpty()))) 
            {
                result++;
            }
        }
        return result;
    }
*/
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