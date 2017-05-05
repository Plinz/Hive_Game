package main.java.model;

import main.java.utils.Coord;
import main.java.view.BoardDrawer;

public class Core {

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

	public void addPiece(Piece piece, Coord coord) {
		this.history.saveState(this.currentState);
		this.currentState.getBoard().addPiece(piece, coord);
		this.currentState.getPlayers()[this.currentState.getCurrentPlayer()].removePiece(piece);
		this.currentState.setCurrentPlayer(1 - this.currentState.getCurrentPlayer());
	}

	public void movePiece(Coord source, Coord target) {
		this.history.saveState(this.currentState);
		this.currentState.getBoard().movePiece(source, target);
		this.currentState.setCurrentPlayer(1 - this.currentState.getCurrentPlayer());
		
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
