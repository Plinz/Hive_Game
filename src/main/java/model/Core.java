package main.java.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import main.java.ia.AI;
import main.java.ia.AIFactory;
import main.java.utils.Consts;
import main.java.utils.CoordGene;
import main.java.view.BoardDrawer;

public class Core implements Serializable {

	private static final long serialVersionUID = 4165722506890173058L;
	private History history;
	private State currentState;
	private AI ai;
	private int mode;
	private int status;

	public Core(int mode, int difficulty) {
		this.history = new History();
		this.currentState = new State();
		this.ai = AIFactory.buildAI(difficulty);
		this.mode = mode;
		this.status = Consts.INGAME;
	}

	public Core(History history, State currentState, int mode, int difficulty, int status) {
		this.history = history;
		this.currentState = currentState;
		this.ai = AIFactory.buildAI(difficulty);
		this.mode = mode;
		this.status = status;
	}

	public boolean accept(BoardDrawer b) {
		this.currentState.getBoard().accept(b);
		return false;
	}

	public boolean addPiece(int piece, CoordGene<Integer> coord) {
		if ((this.currentState.getTurn() == 6 || this.currentState.getTurn() == 7) && !checkQueenRule()
				&& this.currentState.getPlayers()[this.currentState.getCurrentPlayer()].getInventory().get(piece)
						.getId() != Consts.QUEEN)
			return isGameFinish();
		this.history.saveState(this.currentState);
		this.currentState.getBoard().addPiece(
				this.currentState.getPlayers()[this.currentState.getCurrentPlayer()].removePiece(piece), coord);
		this.currentState.setCurrentPlayer(1 - this.currentState.getCurrentPlayer());
		this.currentState.nextTurn();
		this.currentState.getBoard().clearPossibleMovement();
		return playNextTurn();
	}

	private boolean playNextTurn() {
		if (isGameFinish())
			return true;
		if (isPlayerStuck())
			this.currentState.setCurrentPlayer(1 - this.currentState.getCurrentPlayer());
		if (this.mode == Consts.PVAI && this.currentState.getCurrentPlayer() == 1)
			return this.ai.getNextMove(currentState).play();
		return false;
	}

	public boolean movePiece(CoordGene<Integer> source, CoordGene<Integer> target) {
		this.history.saveState(this.currentState);
		this.currentState.getBoard().movePiece(source, target);
		this.currentState.setCurrentPlayer(1 - this.currentState.getCurrentPlayer());
		this.currentState.nextTurn();
		this.currentState.getBoard().clearPossibleMovement();
		return playNextTurn();
	}

	public boolean checkQueenRule() {
		for (Piece p : this.currentState.getPlayers()[this.currentState.getCurrentPlayer()].getInventory()) {
			if (p.getName() == Consts.QUEEN_NAME) {
				return false;
			}
		}
		return true;
	}

	private boolean isGameFinish() {
		Board board = this.currentState.getBoard();
		List<Tile> queenStuck = new ArrayList<Tile>();
		board.getBoard().stream().forEach(column -> column.stream().forEach(box -> queenStuck.addAll(box.stream()
				.filter(tile -> tile.getPiece() != null).filter(tile -> tile.getPiece().getId() == Consts.QUEEN)
				.filter(tile -> board.getPieceNeighbors(tile.getCoord()).size() == 6).collect(Collectors.toList()))));
		if (queenStuck.size() == 1){
			this.status = queenStuck.get(0).getPiece().getTeam();
                }
		else if (queenStuck.size() == 2){
			this.status = Consts.NUL;
                }
		return !queenStuck.isEmpty();
	}

	private boolean isPlayerStuck() {
		int team = this.currentState.getPlayers()[this.currentState.getCurrentPlayer()].getTeam();
		Board board = this.currentState.getBoard();
		List<CoordGene<Integer>> possibleMovement = this.currentState.getPlayers()[this.currentState.getCurrentPlayer()]
				.getInventory().isEmpty() ? new ArrayList<CoordGene<Integer>>() : getPossibleAdd();
		board.getBoard().stream()
				.forEach(column -> column.stream().forEach(box -> box.stream()
						.filter(tile -> tile.getPiece() != null).filter(tile -> tile.getPiece().getTeam() == team)
						.forEach(tile -> possibleMovement.addAll(tile.getPiece().getPossibleMovement(tile, board)))));
		return possibleMovement.isEmpty();
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
			Board board = this.currentState.getBoard();
			Tile current;
			List<Tile> neighbors;
			for (List<List<Tile>> column : board.getBoard())
				for (List<Tile> box : column)
					if (!box.isEmpty() && (current = box.get(0)) != null && current.getPiece() == null
							&& !(neighbors = board.getPieceNeighbors(current.getCoord())).isEmpty()
							&& !neighbors.stream()
									.anyMatch(it -> it.getPiece().team != this.currentState.getCurrentPlayer()))
						pos.add(current.getCoord());
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

	public AI getAi() {
		return ai;
	}

	public void setAi(AI ai) {
		this.ai = ai;
	}

	public int getMode() {
		return mode;
	}

	public void setMode(int mode) {
		this.mode = mode;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

}
