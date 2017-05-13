package main.java.model;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

import main.java.ia.AI;
import main.java.ia.AIFactory;
import main.java.ia.AIMove;
import main.java.utils.Consts;
import main.java.utils.CoordGene;
import main.java.view.BoardDrawer;

@XmlRootElement(name = "core")
@XmlAccessorType(XmlAccessType.FIELD)
public class Core {

	@XmlElement(name = "history")
	private History history;
	@XmlElement(name = "currentState")
	private State currentState;
	@XmlTransient
	private AI ai;
	@XmlElement(name = "mode")
	private int mode;
	@XmlTransient
	private int status;

	public Core() {
		this.mode = -1;
		this.status = -1;
	}

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
		currentState.getBoard().accept(b);
		return false;
	}

	public boolean isTile(CoordGene<Integer> coord) {
		return currentState.getBoard().getTile(coord) != null;
	}

	public boolean isPiece(CoordGene<Integer> coord) {
		return isTile(coord) && currentState.getBoard().getTile(coord).getPiece() != null;
	}

	public boolean isPieceOfCurrentPlayer(CoordGene<Integer> coord) {
		return isTile(coord) && isPiece(coord) && currentState.getCurrentPlayerObject().getTeam() == currentState
				.getBoard().getTile(coord).getPiece().getTeam();
	}

	public boolean addPiece(int piece, CoordGene<Integer> coord) {
		if (!canAddPiece(piece))
			return isGameFinish();
		if (mode == Consts.PVP || currentState.getCurrentPlayer() == Consts.PLAYER1)
			history.saveState(currentState);
		System.out.println(Notation.getHumanDescription(Notation.getMoveNotation(currentState.getBoard(), currentState.getCurrentPlayerObject().getInventory().get(piece), coord), true));
		history.saveMove(currentState.getBoard(), currentState.getCurrentPlayerObject().getInventory().get(piece), coord);
		currentState.getBoard().addPiece(currentState.getCurrentPlayerObject().removePiece(piece), coord);
		currentState.nextTurn();
		return playNextTurn();
	}

	public boolean movePiece(CoordGene<Integer> source, CoordGene<Integer> target) {
		if (this.mode == Consts.PVP || currentState.getCurrentPlayer() == Consts.PLAYER1)
			history.saveState(currentState);
		System.out.println(Notation.getHumanDescription(Notation.getMoveNotation(currentState.getBoard(), currentState.getBoard().getTile(source).getPiece(), target), false));
		history.saveMove(currentState.getBoard(), currentState.getBoard().getTile(source).getPiece(), target);
		currentState.getBoard().movePiece(source, target);
		currentState.nextTurn();
		return playNextTurn();
	}

	private boolean playNextTurn() {
		if (isGameFinish())
			return true;
		if (isPlayerStuck())
			currentState.setCurrentPlayer(1 - currentState.getCurrentPlayer());
		if (mode == Consts.PVAI && currentState.getCurrentPlayer() == Consts.AI_PLAYER) {
			AIMove aiMove = ai.getNextMove(currentState);
			aiMove.setCore(this);
			return aiMove.play();
		}

		return false;
	}

	private boolean canAddPiece(int piece) {
		return (currentState.getTurn() != 6 && currentState.getTurn() != 7) || isQueenOnBoard()
				|| currentState.getCurrentPlayerObject().getInventory().get(piece).getId() == Consts.QUEEN;
	}

	private boolean isQueenOnBoard() {
		return currentState.getCurrentPlayerObject().getInventory().stream()
				.noneMatch(piece -> piece.getId() == Consts.QUEEN);
	}

	private boolean isGameFinish() {
		Board board = currentState.getBoard();
		List<Tile> queenStuck = new ArrayList<Tile>();
		board.getBoard().stream().forEach(column -> column.stream().forEach(box -> queenStuck.addAll(box.stream()
				.filter(tile -> tile.getPiece() != null).filter(tile -> tile.getPiece().getId() == Consts.QUEEN)
				.filter(tile -> board.getPieceNeighbors(tile.getCoord()).size() == 6).collect(Collectors.toList()))));
		if (queenStuck.size() == 1)
			status = queenStuck.get(0).getPiece().getTeam();
		else if (queenStuck.size() == 2)
			status = Consts.NUL;
		return !queenStuck.isEmpty();
	}

	private boolean isPlayerStuck() {
		int team = currentState.getCurrentPlayerObject().getTeam();
		Board board = currentState.getBoard();
		List<CoordGene<Integer>> possibleMovement = currentState.getCurrentPlayerObject().getInventory().isEmpty()
				? new ArrayList<CoordGene<Integer>>() : getPossibleAdd();
		board.getBoard().stream()
				.forEach(column -> column.stream().forEach(box -> box.stream()
						.filter(tile -> tile.getPiece() != null).filter(tile -> tile.getPiece().getTeam() == team)
						.forEach(tile -> possibleMovement.addAll(tile.getPiece().getPossibleMovement(tile, board)))));
		return possibleMovement.isEmpty();
	}

	public List<CoordGene<Integer>> getPossibleMovement(CoordGene<Integer> coord) {
		if (isQueenOnBoard()) {
			Board board = currentState.getBoard();
			Tile tile = board.getTile(coord);
			return tile.getPiece().getPossibleMovement(tile, board);
		}
		return new ArrayList<CoordGene<Integer>>();
	}

	public List<CoordGene<Integer>> getPossibleAdd() {
		List<CoordGene<Integer>> pos = new ArrayList<CoordGene<Integer>>();
		switch (currentState.getTurn()) {
		case 0:
			pos.add(new CoordGene<Integer>(0, 0));
			break;
		case 1:
			pos.addAll(new CoordGene<Integer>(1, 1).getNeighbors());
			break;
		default:
			Board board = currentState.getBoard();
			Tile current;
			List<Tile> neighbors;
			for (Column column : board.getBoard())
				for (Box box : column)
					if (!box.isEmpty() && (current = box.get(0)) != null && current.getPiece() == null
							&& !(neighbors = board.getPieceNeighbors(current.getCoord())).isEmpty() && !neighbors
									.stream().anyMatch(it -> it.getPiece().team != currentState.getCurrentPlayer()))
						pos.add(current.getCoord());
		}
		return pos;
	}

	public boolean previousState() {
		if (history.hasPreviousState()) {
			currentState = history.getPreviousState(this.getCurrentState());
			return true;
		}
		return false;
	}

	public boolean nextState() {
		if (history.hasNextState()) {
			currentState = history.getNextState(this.getCurrentState());
			return true;
		}
                System.err.println("gargl");
		return false;
	}

	public void save(String name) {
		System.out.println("CICICI");
		if (name == null)
			name = generateSaveName();
		Save.makeSave(name, this);
	}

	private String generateSaveName() {
		// TODO Auto-generated method stub
		return null;
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
