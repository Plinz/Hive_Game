/*

 */
package main.java.ia;

import main.java.engine.Core;
import main.java.utils.Consts;

public class MediumHeuristics extends Heuristics{
    
    public MediumHeuristics(Core core) {
        super(core);
        this.difficulty = Consts.MEDIUM;
        this.maxdepth = Consts.DEPTH_MEDIUM;
    }
    
    @Override
    public double getHeuristicsValue(){
        getGeneralValues();
        double value =0;
        value += heuristicData[0][5][0] * (nbMovesAI + nbPlacementAI);
        value += heuristicData[0][5][1] * nbPlacementAI;
        value += heuristicData[0][5][2] * nbMovesAI;
        value += heuristicData[0][5][3] * nbPieceInHandAI;
        value += heuristicData[0][5][4] * nbPieceOnBoardAI;
        value += heuristicData[0][5][5] * nbPlacementAI;
        value += heuristicData[0][5][6] * nbPinnedAI;
        //System.out.println("Value apres ajout général IA :"+value);
        value += heuristicData[1][5][0] * (nbMovesOpponent + nbPlacementOpponent);
        value += heuristicData[1][5][1] * nbPlacementOpponent;
        value += heuristicData[1][5][2] * nbMovesOpponent;
        value += heuristicData[1][5][3] * nbPieceInHandOpponent;
        value += heuristicData[1][5][4] * nbPieceOnBoardOpponent;
        value += heuristicData[1][5][5] * nbPlacementOpponent;
        value += heuristicData[1][5][6] * nbPinnedOpponent;
        //System.out.println("Value apres ajout général human :"+value);
        for (int player = 0 ; player <2 ; player ++){
            for (int pieceId = 0 ; pieceId < Consts.NB_PIECES_PER_PLAYER ; pieceId++){
                value += getPieceValue(player, pieceId);
                //System.out.println("Value ajout player"+player+", piece"+pieceId+",value"+getPieceValue(player, pieceId));
            }
        }
        System.out.println("heuristic value : "+value);
        return value;
    }
    
    public double getPieceValue(int player, int pieceId){
        if (player == AIPlayer){
            player = 0;
        } else {
            player = 1;
        }
        int pieceType = Consts.getType(pieceId);
        double value = heuristicData[player][pieceType][0] * pieces[player][pieceId].nbMoves;
        value += heuristicData[player][pieceType][1] * pieces[player][pieceId].nbMoves * (1-pieces[player][pieceId].isInGame);
        value += heuristicData[player][pieceType][2] * pieces[player][pieceId].nbMoves * pieces[player][pieceId].isInGame;
        value += heuristicData[player][pieceType][3] * pieces[player][pieceId].neighbors;
        value += heuristicData[player][pieceType][4] * (1-pieces[player][pieceId].isInGame);
        value += heuristicData[player][pieceType][5] * pieces[player][pieceId].isInGame;
        value += heuristicData[player][pieceType][6] * pieces[player][pieceId].isPinned;
        return value;
    }
    
}