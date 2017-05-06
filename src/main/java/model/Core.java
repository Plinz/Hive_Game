package main.java.model;

import java.io.Serializable;

import main.java.utils.Consts;
import main.java.utils.Coord;
import main.java.view.BoardDrawer;

public class Core implements Serializable{

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
		b.visit(this);
		this.currentState.getBoard().accept(b);
		return false;
	}

	public boolean addPiece(Piece piece, Coord coord) {
		if ((this.currentState.getTurn() != 6 && this.currentState.getTurn() != 7) || checkQueenRule() || piece.getName()==Consts.QUEEN_NAME){
			this.history.saveState(this.currentState);
			this.currentState.getBoard().addPiece(piece, coord);
			this.currentState.getPlayers()[this.currentState.getCurrentPlayer()].removePiece(piece);
			this.currentState.setCurrentPlayer(1 - this.currentState.getCurrentPlayer());
			this.currentState.nextTurn();
			this.currentState.getBoard().clearPossibleMovement();
			return true;
		} else {
			return false;
		}
	}

	public boolean movePiece(Coord source, Coord target) {
		if (this.currentState.getTurn() > 4 || checkQueenRule()){
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
		for(Piece p : this.currentState.getPlayers()[this.currentState.getCurrentPlayer()].getInventory()){
            System.out.println("Piece dans l'inventaire"+p.getName());
			if(p.getName() == Consts.QUEEN_NAME)
				return false;
		}
		return true;
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
