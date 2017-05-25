/*

 */
package main.java.ia;

import main.java.engine.Core;

public class EasyHeuristics extends Heuristics {

    public EasyHeuristics(Core core) {
        super(core);
        this.maxdepth = HeuristicConst.DEPTH_EASY;
    }

    public double getHeuristicsValue() {
        nbConfigsStudied++;
        if (!core.isQueenOnBoard(AIPlayer) && !core.isQueenOnBoard(1-AIPlayer)){
            return 0;
        }
        int a = getNbPiecesAroundQueen(1 - AIPlayer);
        int b = getNbPiecesAroundQueen(AIPlayer);
        int result = a*4 - b*3;
        System.out.println("heuristics = "+result + "queen opo +"+a+"queen friend "+b);
        return result;
    }
}
