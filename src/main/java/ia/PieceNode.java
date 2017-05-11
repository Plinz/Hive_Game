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

public class PieceNode {

    int x, y, z, piece;
    boolean stuck, isVisited, isOnBoard;
    PieceNode pieceAbove;

    public PieceNode(StoringConfig config, int index) {
        this.x = config.getX(index);
        this.y = config.getY(index);
        this.z = config.getZ(index);
        this.piece = index;
        this.stuck = config.isStuck(index);
        this.isOnBoard = config.isOnBoard(index);
        this.isVisited = false;
        this.pieceAbove = null;
    }

    public int getX() {
        return this.x;
    }

    public int getY() {
        return this.y;
    }

    public int getZ() {
        return this.z;
    }

    public int getPiece() {
        return this.piece;
    }

    public boolean isStuck() {
        return this.stuck;
    }

    public boolean isOnBoard() {
        return this.isOnBoard;
    }

    public boolean isVisited() {
        return this.isVisited;
    }

    public PieceNode getPieceAbove() {
        return this.pieceAbove;
    }

    public String toString() {
        String result = "Piece : " + piece + ";(x,y,z)=" + x + "," + y + "," + z + ".";
        if (isOnBoard) {
            result += "On board.";
        } else {
            result += "In hand.";
        }
        if (isStuck()) {
            result += "Is stuck.";
        }
        result += "\n";
        return result;
    }

    public void setStuck(boolean stuck) {
        this.stuck = stuck;
    }

    public void setX(int newX){
        this.x = newX;
    }
    
    public void setY(int newY){
        this.y = newY;
    }
    
    public void setZ(int newZ){
        this.z = newZ;
    }
}
