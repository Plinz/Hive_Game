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
        this.x = config.getX(index);
        this.y = config.getY(index);
        this.z = config.getZ(index);
        this.piece = (byte) index;
        this.stuck = config.isStuck(index);
        this.isOnBoard = config.isOnBoard(index);
        this.isVisited = false;
        this.next = null;
    }
    
    public byte getX()
    {
        return this.x;
    }
    
    public byte getY()
    {
        return this.y;
    }
    
    public byte getZ()
    {
        return this.z;
    }
    
    public byte getPiece()
    {
        return this.getPiece();
    }
    
    public boolean isStuck()
    {
        return this.stuck;
    }
 
    public boolean isOnBoard()
    {
        return this.isOnBoard;
    }
    
    public boolean isVisited()
    {
        return this.isVisited;
    }
    
    public LoopingConfigNode getNext()
    {
        return this.next;
    }
    
}
