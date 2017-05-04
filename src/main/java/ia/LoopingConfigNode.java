/*

 */
package main.java.ia;

public class LoopingConfigNode {
    
    byte x, y, z, piece;
    boolean stuck, isVisited, bool3 ;
    LoopingConfigNode next;
    
    public LoopingConfigNode(StoringConfig config, int index){
        x = config.getX(index);
        y = config.getY(index);
        z = config.getZ(index);
        piece = (byte) index;
        stuck = config.getStuck(index);
        isVisited = config.getIsVisited(index);
        bool3 = config.getBool3(index);
        next = null;
    }
}
