package main.java.model;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import main.java.ia.AI;
import main.java.ia.AIFactory;
import main.java.ia.AIMove;
import main.java.utils.Consts;
import main.java.utils.CoordGene;
import main.java.view.BoardDrawer;

public class Core {

	private HistoryNotation history;
	private State currentState;
	private Emulator emulator;
	private AI ai;
	private int mode;
	private int status;

	public Core() {
		this.mode = -1;
		this.status = -1;
	}

	public Core(int mode, int difficulty) {
		this.history = new HistoryNotation();
		this.currentState = new State();
		this.emulator = new Emulator(this, currentState.getBoard(), currentState.getPlayers());
		this.ai = AIFactory.buildAI(difficulty);
		this.mode = mode;
		this.status = Consts.INGAME;
	}

	public Core(HistoryNotation history, State currentState, Emulator emulator, int mode, int difficulty, int status) {
		this.history = history;
		this.currentState = currentState;
		this.emulator = emulator;
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

	public boolean addPiece(int pieceId, CoordGene<Integer> coord) {
		if (!canAddPiece(pieceId))
			return isGameFinish();
		Piece piece = currentState.getCurrentPlayerObject().removePiece(pieceId);
		String notation = Notation.getMoveNotation(currentState.getBoard(), piece, coord);
		String unplay = Notation.getInverseMoveNotation(currentState.getBoard(), piece);
		currentState.getBoard().addPiece(piece, coord);

		if (mode == Consts.PVP || currentState.getCurrentPlayer() == Consts.PLAYER1)
			history.save(notation, unplay);
		currentState.nextTurn();
		return playNextTurn();
	}

	public boolean movePiece(CoordGene<Integer> source, CoordGene<Integer> target) {
		Piece piece = currentState.getBoard().getTile(source).getPiece();
		String notation = Notation.getMoveNotation(currentState.getBoard(), piece, target);
		String unplay = Notation.getInverseMoveNotation(currentState.getBoard(), piece);
		if (this.mode == Consts.PVP || currentState.getCurrentPlayer() == Consts.PLAYER1)
			history.save(notation, unplay);
		currentState.getBoard().movePiece(source, target);
		currentState.nextTurn();
		return playNextTurn();
	}
	
	public void removePiece(CoordGene<Integer> coord) {
		Piece piece = currentState.getBoard().removePiece(coord);
		currentState.getPlayers()[1 - currentState.getCurrentPlayer()].getInventory().add(piece);
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

	private boolean canAddPiece(int pieceId) {
		return (currentState.getTurn() != 6 && currentState.getTurn() != 7) || isQueenOnBoard()
				|| pieceId == Consts.QUEEN;
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
		if (history.hasPrevious()) {
			emulator.play(history.getPrevious());
			currentState.previousTurn();
			return true;
		}
		return false;
	}

	public boolean nextState() {
		if (history.hasNext()) {
			emulator.play(history.getNext());
			currentState.nextTurn();
			return true;
		}
		return false;
	}

	public void save(String name) {
		System.out.println("SAVE PROCESS");
		if (name == null)
			name = generateSaveName();
		Save.makeSave(name, this);
	}

	private String generateSaveName() {
		return null;
	}

	public HistoryNotation getHistory() {
		return history;
	}

	public void setHistory(HistoryNotation history) {
		this.history = history;
	}

	public State getCurrentState() {
		return currentState;
	}

	public void setCurrentState(State currentState) {
		this.currentState = currentState;
	}

	public Emulator getEmulator() {
		return emulator;
	}

	public void setEmulator(Emulator emulator) {
		this.emulator = emulator;
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
