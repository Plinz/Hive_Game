/*
This class is more or less a linked list node,
representing a tile.
Convention ->   piece : see Consts
                team : white -> piece value is unchanged ; 
                black -> piece value + nb of white pieces
                x,y,z : coords of the tile on the board
                piece in players hand -> x=loopingconfig array size (least visited) ; 
                                         y=-1 (impossible value); z whatever
                rest is kind of obvious
 */
package main.java.ia;

public class LoopingConfigNode {
    
    byte x, y, z, piece;
    boolean stuck, isVisited, isOnBoard ;
    LoopingConfigNode next;
    
    public LoopingConfigNode(StoringConfig config, int index){
        x = config.getX(index);
        y = config.getY(index);
        z = config.getZ(index);
        piece = (byte) index;
        stuck = config.isStuck(index);
        isOnBoard = config.isOnBoard(index);
        isVisited = false;
        next = null;
    }
}
