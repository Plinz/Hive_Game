package main.java.model;

import java.util.ArrayList;
import main.java.utils.Coord;
import main.java.view.BoardDrawer;

public class Core {

	private History history;
	private State currentState;
	private int mode;
        private ArrayList<Coord> destination;

	public Core(int mode) {
		this.history = new History();
		this.currentState = new State();
		this.mode = mode;
                destination = new ArrayList<>();
	}

	public Core(History history, State currentState, int mode) {
		this.history = history;
		this.currentState = currentState;
		this.mode = mode;
	}

	public boolean accept(BoardDrawer b) {
		b.visit(this);
		this.currentState.getBoard().accept(b);
                if(destination != null)
                    b.visit(destination);
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
		this.destination = null;
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
        
        public void setDestination(ArrayList<Coord> d) {
		this.destination = d;
	}
        
        public ArrayList<Coord> getDestination(){
            return this.destination;
        }

}
