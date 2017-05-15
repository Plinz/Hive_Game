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
	private Board board;
	private Player[] players;
	private Emulator emulator;
	private AI ai;
	private int mode;
	private int status;
	private int turn;
	private int currentPlayer;

	public Core(int mode, int difficulty) {
		this.history = new HistoryNotation();
		this.board = new Board();
        this.players = new Player[2];
        this.players[0] = new Player(0);
        this.players[1] = new Player(1);
		this.emulator = new Emulator(this, board, players);
		this.ai = AIFactory.buildAI(difficulty, this);
		this.mode = mode;
		this.status = Consts.INGAME;
		this.turn = 0;
		this.currentPlayer = Consts.PLAYER1;
	}

	public boolean accept(BoardDrawer b) {
		board.accept(b);
		return false;
	}

	public boolean isTile(CoordGene<Integer> coord) {
		return board.getTile(coord) != null;
	}

	public boolean isPiece(CoordGene<Integer> coord) {
		return isTile(coord) && board.getTile(coord).getPiece() != null;
	}

	public boolean isPieceOfCurrentPlayer(CoordGene<Integer> coord) {
		return isTile(coord) && isPiece(coord) && getCurrentPlayerObj().getTeam() == board.getTile(coord).getPiece().getTeam();
	}

	public boolean addPiece(int pieceId, CoordGene<Integer> coord) {
		if (!canAddPiece(pieceId))
			return isGameFinish();
		Piece piece = getCurrentPlayerObj().removePiece(pieceId);
		String notation = Notation.getMoveNotation(board, piece, coord);
		String unplay = Notation.getInverseMoveNotation(board, piece);
		board.addPiece(piece, coord);

		if (mode == Consts.PVP || currentPlayer == Consts.PLAYER1)
			history.save(notation, unplay);
		nextTurn();
		return playNextTurn();
	}

	public boolean movePiece(CoordGene<Integer> source, CoordGene<Integer> target) {
		Piece piece = board.getTile(source).getPiece();
		String notation = Notation.getMoveNotation(board, piece, target);
		String unplay = Notation.getInverseMoveNotation(board, piece);
		if (this.mode == Consts.PVP || currentPlayer == Consts.PLAYER1)
			history.save(notation, unplay);
		board.movePiece(source, target);
		nextTurn();
		return playNextTurn();
	}
	
	public void removePiece(CoordGene<Integer> coord) {
		Piece piece = board.removePiece(coord);
		players[1 - currentPlayer].addPiece(piece);;
	}

	private boolean playNextTurn() {
		if (isGameFinish())
			return true;
		if (isPlayerStuck())
			currentPlayer = (1 - currentPlayer);
		if (mode == Consts.PVAI && currentPlayer == Consts.AI_PLAYER) {
			AIMove aiMove = ai.getNextMove(this);
			aiMove.setCore(this);
			return aiMove.play();
		}

		return false;
	}

	private boolean canAddPiece(int pieceId) {
		return (turn != 6 && turn != 7) || isQueenOnBoard()
				|| pieceId == Consts.QUEEN;
	}

	private boolean isQueenOnBoard() {
		return getCurrentPlayerObj().getInventory().stream()
				.noneMatch(piece -> piece.getId() == Consts.QUEEN);
	}

	private boolean isGameFinish() {
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
		int team = getCurrentPlayerObj().getTeam();
		List<CoordGene<Integer>> possibleMovement = getCurrentPlayerObj().getInventory().isEmpty()
				? new ArrayList<CoordGene<Integer>>() : getPossibleAdd();
		board.getBoard().stream()
				.forEach(column -> column.stream().forEach(box -> box.stream()
						.filter(tile -> tile.getPiece() != null).filter(tile -> tile.getPiece().getTeam() == team)
						.forEach(tile -> possibleMovement.addAll(tile.getPiece().getPossibleMovement(tile, board)))));
		return possibleMovement.isEmpty();
	}

	public List<CoordGene<Integer>> getPossibleMovement(CoordGene<Integer> coord) {
		if (isQueenOnBoard()) {
			Tile tile = board.getTile(coord);
			return tile.getPiece().getPossibleMovement(tile, board);
		}
		return new ArrayList<CoordGene<Integer>>();
	}

	public List<CoordGene<Integer>> getPossibleAdd() {
		List<CoordGene<Integer>> pos = new ArrayList<CoordGene<Integer>>();
		switch (turn) {
		case 0:
			pos.add(new CoordGene<Integer>(0, 0));
			break;
		case 1:
			pos.addAll(new CoordGene<Integer>(1, 1).getNeighbors());
			break;
		default:
			Tile current;
			List<Tile> neighbors;
			for (Column column : board.getBoard())
				for (Box box : column)
					if (!box.isEmpty() && (current = box.get(0)) != null && current.getPiece() == null
							&& !(neighbors = board.getPieceNeighbors(current.getCoord())).isEmpty() && !neighbors
									.stream().anyMatch(it -> it.getPiece().team != currentPlayer))
						pos.add(current.getCoord());
		}
		return pos;
	}

	public boolean previousState() {
		if (history.hasPrevious()) {
			emulator.play(history.getPrevious());
			previousTurn();
			return true;
		}
		return false;
	}

	public boolean nextState() {
		if (history.hasNext()) {
			emulator.play(history.getNext());
			nextTurn();
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
	
	public Player getCurrentPlayerObj(){
		return players[currentPlayer];
	}
	
    public void nextTurn() {
		currentPlayer = 1 - currentPlayer;
		board.clearPossibleMovement();
        turn++;
    }
    
	public void previousTurn() {
		currentPlayer = 1 - currentPlayer;
		board.clearPossibleMovement();
        turn--;		
	}

	public HistoryNotation getHistory() {
		return history;
	}

	public void setHistory(HistoryNotation history) {
		this.history = history;
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

	public Board getBoard() {
		return board;
	}

	public void setBoard(Board board) {
		this.board = board;
	}

	public Player[] getPlayers() {
		return players;
	}

	public void setPlayers(Player[] players) {
		this.players = players;
	}

	public int getTurn() {
		return turn;
	}

	public void setTurn(int turn) {
		this.turn = turn;
	}
	
	public int getCurrentPlayer(){
		return this.currentPlayer;
	}

	public void setCurrentPlayer(int currentPlayer) {
		this.currentPlayer = currentPlayer;
	}

}
