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
        this.maxdepth = Consts.DEPTH_MEDIUM;
    }

    @Override
    public double getHeuristicsValue() {
        getGeneralValues();
        double value = 0;
        value += heuristicData[0][5][0] * (nbMovesAI + nbPlacementAI);
        value += heuristicData[0][5][1] * nbPlacementAI;
        value += heuristicData[0][5][2] * nbMovesAI;
        value += heuristicData[0][5][3] * nbPieceInHandAI;
        value += heuristicData[0][5][4] * nbPieceOnBoardAI;
        value += heuristicData[0][5][5] * nbPlacementAI;
        value += heuristicData[0][5][6] * nbPinnedAI;
        System.out.println("\tValue apres ajout général pour les blanc :" + value);
        value += heuristicData[1][5][0] * (nbMovesOpponent + nbPlacementOpponent);
        value += heuristicData[1][5][1] * nbPlacementOpponent;
        value += heuristicData[1][5][2] * nbMovesOpponent;
        value += heuristicData[1][5][3] * nbPieceInHandOpponent;
        value += heuristicData[1][5][4] * nbPieceOnBoardOpponent;
        value += heuristicData[1][5][5] * nbPlacementOpponent;
        value += heuristicData[1][5][6] * nbPinnedOpponent;
        System.out.println("\tValue apres ajout général pour les noir :" + value);
        for (int player = 0; player < 2; player++) {
            //value += Math.pow(HeuristicConst.QUEEN_NEIGHBOR_FACTOR, heuristicData[player][Consts.QUEEN_TYPE][3]) * heuristicData[player][Consts.QUEEN_TYPE][3];
            for (int pieceId = 0; pieceId < Consts.NB_PIECES_PER_PLAYER; pieceId++) {
                value += getPieceValue(player, pieceId);
                //System.out.println("Value ajout player"+player+", piece"+pieceId+",value"+getPieceValue(player, pieceId));
            }
            if (pieces[player][Consts.QUEEN].neighbors == 6) {
                value += heuristicData[player][Consts.QUEEN_TYPE][3] * 10;
                System.out.println("Victoire");
                isVictory = true;
            }
        }

        //System.out.println("heuristic value : "+value);
        return value;
    }

    public double getPieceValue(int player, int pieceId) {

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
                temp = heuristicData[player][pieceType][0] / dist_to_queen;
                value += temp;
                if (Math.floor(temp) > Math.floor(maxFieldValue)) {
                    maxFieldValue = temp;
                    maxIndex = 0;
                }
            }
        } else if (pieceType != Consts.BEETLE_TYPE) {
            temp = heuristicData[player][pieceType][0] * pieces[player][pieceId].nbMoves;
            value += temp;
            if (Math.floor(temp) > Math.floor(maxFieldValue)) {
                maxFieldValue = temp;
                maxIndex = 0;
            }
        } else {
            value += 0;
        }

        temp = heuristicData[player][pieceType][1] * pieces[player][pieceId].nbMoves * (1 - pieces[player][pieceId].isInGame);
        if (Math.floor(temp) > Math.floor(maxFieldValue)) {
            maxFieldValue = temp;
            maxIndex = 1;
        }
        temp = heuristicData[player][pieceType][2] * pieces[player][pieceId].nbMoves * pieces[player][pieceId].isInGame;
        if (Math.floor(temp) > Math.floor(maxFieldValue)) {
            maxFieldValue = temp;
            maxIndex = 1;
        }
        temp = heuristicData[player][pieceType][3] * pieces[player][pieceId].neighbors;
        if (Math.floor(temp) > Math.floor(maxFieldValue)) {
            maxFieldValue = temp;
            maxIndex = 1;
        }
        temp = heuristicData[player][pieceType][4] * (1 - pieces[player][pieceId].isInGame);
        if (Math.floor(temp) > Math.floor(maxFieldValue)) {
            maxFieldValue = temp;
            maxIndex = 1;
        }
        temp = heuristicData[player][pieceType][5] * pieces[player][pieceId].isInGame;
        if (Math.floor(temp) > Math.floor(maxFieldValue)) {
            maxFieldValue = temp;
            maxIndex = 1;
        }
        value += heuristicData[player][pieceType][6] * pieces[player][pieceId].isPinned;
        System.out.println("\t\tpièce " + Consts.getName(pieceId) +" "+ (player == 0 ? "blanc" : "noir") + ", valeur totale=" + value);
        System.out.println("\t\t\ten particulier le champ" + maxIndex + "qui vaut" + maxFieldValue);
        return value;
    }

}
