/*

 */
package main.java.ia;

public class HeuristicPiece {

    int id, neighbors, nbMoves, isPinned, isInGame;

    public HeuristicPiece(int id) {
        this.id = id;
    }

    public void getValuesOnBoard(int nbNeighbors, int nbMove) {
        isPinned = (nbMove == 0 ? 1 : 0);
        isInGame = 1;
        this.nbMoves = nbMove;
        this.neighbors = nbNeighbors;
    }

    public void getValuesInHand(int nbMove) {
        isPinned = 0;
        isInGame = 0;
        neighbors = 0;
        nbMoves = nbMove;
    }
    
    public void resetValues(){
        isPinned = 0;
        isInGame = 0;
        neighbors = 0;
        nbMoves = 0;
    }
}