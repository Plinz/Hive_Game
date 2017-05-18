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

    public int getHeuristicsValue() {
        nbConfigsStudied++;
        int result = getNbPiecesAroundQueen(1 - AIPlayer) - getNbPiecesAroundQueen(AIPlayer);
        System.out.println("heuristics = "+result);
        return result;
    }
}
