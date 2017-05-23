/*

 */
package main.java.ia;

import main.java.engine.Core;
import main.java.utils.Consts;

public class EasyHeuristics extends Heuristics {

    public EasyHeuristics(Core core) {
        super(core);
        this.maxdepth = Consts.DEPTH_EASY;
    }

    public double getHeuristicsValue() {
        nbConfigsStudied++;
        if (!core.isQueenOnBoard(AIPlayer) && !core.isQueenOnBoard(1-AIPlayer)){
            return 0;
        }
        int result = getNbPiecesAroundQueen(1 - AIPlayer)*4 - getNbPiecesAroundQueen(AIPlayer)*3;
        System.out.println("heuristics = "+result);
        return result;
    }
}
