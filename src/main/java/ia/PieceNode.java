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

import java.util.ArrayList;
import main.java.utils.Coord;
import main.java.utils.Cube;

public class PieceNode {

    int x, y, z, piece;
    boolean stuck, isVisited, isOnBoard, possibleDestinationsCalculated;
    PieceNode pieceAbove;
    ArrayList<Coord> possibleDestinations;
    ArrayList<Cube<Integer>> possibleCubeDestinations; //for beetle & mosquito -> we need cube coords

    public PieceNode(StoringConfig config, int pieceId) {
        this.x = config.getX(pieceId);
        this.y = config.getY(pieceId);
        this.z = config.getZ(pieceId);
        this.piece = pieceId;
        this.stuck = config.isStuck(pieceId);
        this.isOnBoard = config.isOnBoard(pieceId);
        this.isVisited = false;
        this.pieceAbove = null;
        this.possibleDestinations = new ArrayList<>();
        this.possibleCubeDestinations = new ArrayList<>();
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

    @Override
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
        if (isVisited())
        {
            result += "Is visited.";
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
    
    public void setIsVisited(boolean visited)
    {
        this.isVisited = visited;
    }
    
    @Override
    public boolean equals(Object object)
    {
        if (this == object)
            return true;
        
        if (object == null)
            return false;
        
        if (this.getClass() != object.getClass())
            return false;
        
        PieceNode obj = (PieceNode) object;
        return ((this.x == obj.x) && (this.y == obj.y) && (this.z == obj.z)
                && (this.piece == obj.piece) && (this.stuck == obj.stuck)
                && (this.isOnBoard == obj.isOnBoard) && (this.pieceAbove.equals(obj.pieceAbove))
                && (this.possibleDestinations.equals(obj.possibleDestinations)));
    }
    
    @Override
    public int hashCode()
    {
        final int prime = 83;
        int result = 5;
        result += prime * (this.x << 1);
        result += (prime+1) * (this.y << 2);
        result += (prime+2) * (this.z << 3);
        result += prime * this.piece & 11;
        result += result * (this.stuck ? 3 : 5);
        result += result * (this.isOnBoard ? 7 : 9);
        result += prime + ((this.pieceAbove == null) ? 0 : this.pieceAbove.hashCode());
        result += prime + ((this.possibleDestinations == null) ? 0 : this.possibleDestinations.hashCode());
        return result;
    }
    
}
