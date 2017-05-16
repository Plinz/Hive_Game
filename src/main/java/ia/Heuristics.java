/*

 */
package main.java.ia;

import main.java.utils.Consts;
import main.java.utils.Coord;

public class Heuristics {

    GameConfig gameConfig;

    /*
     *                  CONSTUCTOR
     */
    public Heuristics(GameConfig gameConfig) {
        this.gameConfig = gameConfig;
    }

    /*
     *                  GETTERS
     */
    public int getNbPiecesAroundQueen(int player) {
        int QueenId = gameConfig.getNbPiecesPerColor() * player;
        PieceNode QueenNode = gameConfig.getNode(QueenId);
        return gameConfig.getNeighborsInArrayList(QueenNode).size();
    }

    public int getHeuristicsValue() {
        System.err.println("Erreur : getHeuristics value ne doit as être appelée depuis la classe mère");
        return 0;
    }

    public int getNbPiecesOnBoard(int player) {
        int result = 0;
        int start = player * gameConfig.nbPiecesPerColor;
        int finish = start + gameConfig.nbPiecesPerColor;
        for (int i = start; i < finish; i++) {
            if (gameConfig.getNode(i).isOnBoard) {
                result++;
            }
        }
        return result;
    }
    
    public int getMobility(int player)
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