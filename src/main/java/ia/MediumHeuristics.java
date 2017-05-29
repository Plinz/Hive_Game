/*

 */
package main.java.ia;

import main.java.engine.Core;
import main.java.model.Tile;
import main.java.utils.Consts;

public class MediumHeuristics extends Heuristics {

    public MediumHeuristics(Core core) {
        super(core);
        this.difficulty = Consts.MEDIUM;
        this.maxdepth = HeuristicConst.DEPTH_MEDIUM;
    }

    //constructor for hard AI
    public MediumHeuristics(Core core, int difficulty) {
        super(core);
        if (difficulty == Consts.MEDIUM) {
            this.difficulty = Consts.MEDIUM;
            this.maxdepth = HeuristicConst.DEPTH_MEDIUM;
        } else { //case -> Hard
            this.difficulty = Consts.HARD;
            this.maxdepth = HeuristicConst.DEPTH_HARD;
        }

    }

    @Override
    public double getHeuristicsValue() {
        getGeneralValues();
        double value = 0, temp;
        value += heuristicData[0][5][0] * (nbMovesAI + nbPlacementAI);
        value += heuristicData[0][5][1] * nbPlacementAI;
        value += heuristicData[0][5][2] * nbMovesAI;
        value += heuristicData[0][5][3] * nbPieceInHandAI;
        value += heuristicData[0][5][4] * nbPieceOnBoardAI;
        value += heuristicData[0][5][5] * nbPlacementAI;
        value += heuristicData[0][5][6] * nbPinnedAI;

        value += heuristicData[1][5][0] * (nbMovesOpponent + nbPlacementOpponent);
        value += heuristicData[1][5][1] * nbPlacementOpponent;
        value += heuristicData[1][5][2] * nbMovesOpponent;
        value += heuristicData[1][5][3] * nbPieceInHandOpponent;
        value += heuristicData[1][5][4] * nbPieceOnBoardOpponent;
        value += heuristicData[1][5][5] * nbPlacementOpponent;
        value += heuristicData[1][5][6] * nbPinnedOpponent;
        for (int player = 0; player < 2; player++) {
            for (int pieceId = 0; pieceId < Consts.NB_PIECES_PER_PLAYER; pieceId++) {
                temp = getPieceValue(player, pieceId);
                value += temp;
            }
            if (core.getStatus() == Consts.WIN_TEAM2) {
                value += HeuristicConst.WHITE_VICTORY;
            } else if (core.getStatus() == Consts.WIN_TEAM1) {
                value += HeuristicConst.BLACK_VICTORY;
            }
        }
        return value;
    }

    private double getPieceValue(int player, int pieceId) {

        int pieceType = Consts.getType(pieceId);
        double value = 0;
        int maxIndex = 0;
        double temp, maxFieldValue = 0;
        if (pieceType == Consts.BEETLE_TYPE && pieces[player][pieceId].isInGame == 1) {
            Tile beetleTile = getTile(pieceId, player);
            int dist_to_queen = distanceToOpposingQueen(beetleTile) + 1;
            //System.out.println("Distance to queen : "+dist_to_queen);
            if (dist_to_queen == -1) {
                System.out.println("Probleme appel distance to opp queen");
            } else {
                if (dist_to_queen == 0) {
                    int sign = 1 - 2 * player;
                    value += sign * HeuristicConst.BEETLE_ON_ENEMY_QUEEN_BONUS;
                }
                temp = heuristicData[player][pieceType][0] / dist_to_queen;
                value += temp;
                if (Math.abs(temp) > Math.abs(maxFieldValue)) {
                    maxFieldValue = temp;
                    maxIndex = 0;
                }
            }
        } else if (pieceType != Consts.BEETLE_TYPE) {
            value += heuristicData[player][pieceType][0] * pieces[player][pieceId].nbMoves;;
        } else {
            value += 0;
        }

        value += heuristicData[player][pieceType][1] * pieces[player][pieceId].nbMoves * (1 - pieces[player][pieceId].isInGame);
        value += heuristicData[player][pieceType][2] * pieces[player][pieceId].nbMoves * pieces[player][pieceId].isInGame;
        value += heuristicData[player][pieceType][3] * pieces[player][pieceId].neighbors;
        value += heuristicData[player][pieceType][4] * (1 - pieces[player][pieceId].isInGame);
        value += heuristicData[player][pieceType][5] * pieces[player][pieceId].isInGame;
        value += heuristicData[player][pieceType][6] * pieces[player][pieceId].isPinned;
        return value;
    }

}
