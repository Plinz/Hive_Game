package main.java.model;

import java.util.ArrayList;
import main.java.utils.Coord;
import main.java.view.BoardDrawer;

public class Core {

	private History history;
	private State currentState;
	private int mode;
        private ArrayList<Coord> destination;
        private Coord pieceChoose;
        private Piece pieceToPlace;

    public Coord getPieceChoose() {
        return pieceChoose;
    }

    public void setPieceChoose(Coord pieceChoose) {
        this.pieceChoose = pieceChoose;
    }

    public Piece getPieceToPlace() {
        return pieceToPlace;
    }

    public void setPieceToPlace(Piece pieceToPlace) {
        this.pieceToPlace = pieceToPlace;
    }

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
                if(pieceChoose != null)
                    b.visit(pieceChoose);
		return false;
	}

	public void addPiece(Piece piece, Coord coord) {
		this.history.saveState(this.currentState);
		this.currentState.getBoard().addPiece(piece, coord);
		this.currentState.getPlayers()[this.currentState.getCurrentPlayer().intValue()].removePiece(piece);
		this.currentState.setCurrentPlayer(1 - this.currentState.getCurrentPlayer().intValue());
                this.clearPiecesAndDest();
      
	}

	public void movePiece(Coord source, Coord target) {
		this.history.saveState(this.currentState);
		this.currentState.getBoard().movePiece(source, target);
		this.currentState.setCurrentPlayer(1 - this.currentState.getCurrentPlayer().intValue());
		this.destination = null;
                this.clearPiecesAndDest();
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
        
        public void initPieceChoose(Coord coord){
            this.setPieceChoose(coord);
            this.setDestination((ArrayList<Coord>) this.getCurrentState().getBoard().getTile(coord).getPiece().getPossibleMovement(this.getCurrentState().getBoard().getTile(coord), this.getCurrentState().getBoard()));
            this.setPieceToPlace(null);
        }
        
        public void clearPiecesAndDest(){
            this.setPieceToPlace(null);
            this.setPieceChoose(null);
            this.setDestination(null);
        }
}
