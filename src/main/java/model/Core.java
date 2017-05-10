package main.java.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import main.java.utils.Consts;
import main.java.utils.CoordGene;
import main.java.view.BoardDrawer;

public class Core implements Serializable {

    private static final long serialVersionUID = 4165722506890173058L;
    private History history;
    private State currentState;
    private int mode;

    public Core(int mode) {
        this.history = new History();
        this.currentState = new State();
        this.mode = mode;
    }

    public Core(History history, State currentState, int mode) {
        this.history = history;
        this.currentState = currentState;
        this.mode = mode;
    }

    public boolean accept(BoardDrawer b) {
        //b.visit(this);
        this.currentState.getBoard().accept(b);
        return false;
    }

    public boolean addPiece(int piece, CoordGene<Integer> coord) {
        if ((this.currentState.getTurn() != 6 && this.currentState.getTurn() != 7) || checkQueenRule()
                || piece == Consts.QUEEN) {
            this.history.saveState(this.currentState);
            this.currentState.getBoard().addPiece(this.currentState.getPlayers()[this.currentState.getCurrentPlayer()].removePiece(piece), coord);
            this.currentState.setCurrentPlayer(1 - this.currentState.getCurrentPlayer());
            this.currentState.nextTurn();
            this.currentState.getBoard().clearPossibleMovement();
            return true;
        } else {
            return false;
        }
    }

    public boolean movePiece(CoordGene<Integer> source, CoordGene<Integer> target) {
        if (checkQueenRule()) {
            this.history.saveState(this.currentState);
            this.currentState.getBoard().movePiece(source, target);
            this.currentState.setCurrentPlayer(1 - this.currentState.getCurrentPlayer());
            this.currentState.nextTurn();
            this.currentState.getBoard().clearPossibleMovement();
            return true;
        } else {
            return false;
        }
    }

    private boolean checkQueenRule() {
        for (Piece p : this.currentState.getPlayers()[this.currentState.getCurrentPlayer()].getInventory()) {
            if (p.getName() == Consts.QUEEN_NAME) {
                return false;
            }
        }
        return true;
    }

    public List<CoordGene<Integer>> getPossibleMovement(CoordGene<Integer> coord) {
        if (checkQueenRule()) {
            Board board = this.currentState.getBoard();
            Tile tile = board.getTile(coord);
            return tile.getPiece().getPossibleMovement(tile, board);
        }
        return new ArrayList<CoordGene<Integer>>();
    }

    public List<CoordGene<Integer>> getPossibleAdd() {
        List<CoordGene<Integer>> pos = new ArrayList<CoordGene<Integer>>();
        switch (this.currentState.getTurn()) {
            case 0:
                pos.add(new CoordGene<Integer>(0, 0));
                break;
            case 1:
                pos.addAll(new CoordGene<Integer>(1, 1).getNeighbors());
                break;
            default:
                Board b = this.currentState.getBoard();
                Tile current;
                List<Tile> neighbors;
                Boolean sameTeam;
                for (List<List<Tile>> column : b.getBoard()) {
                    for (List<Tile> box : column) {
                        sameTeam = true;
                        if (!box.isEmpty() && (current = box.get(0)) != null && current.getPiece() == null
                                && !(neighbors = b.getPieceNeighbors(current.getCoord())).isEmpty()) {
                            for (Tile neighbor : neighbors) {
                                if (neighbor.getPiece().team != this.currentState.getCurrentPlayer()) {
                                    sameTeam = false;
                                    break;
                                }
                            }
                            if (sameTeam) {
                                pos.add(current.getCoord());
                            }
                        }
                    }
                }
        }
        return pos;
    }

    public History getHistory() {
        return history;
    }

    public void setHistory(History history) {
        this.history = history;
    }

    public State getCurrentState() {
        return currentState;
    }

    public void setCurrentState(State currentState) {
        this.currentState = currentState;
    }

    public int getMode() {
        return mode;
    }

    public void setMode(int mode) {
        this.mode = mode;
    }

}
