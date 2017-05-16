package main.java.model;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;
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
	private int difficulty;

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
		this.difficulty = difficulty;
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
		return isTile(coord) && isPiece(coord)
				&& getCurrentPlayerObj().getTeam() == board.getTile(coord).getPiece().getTeam();
	}

	public boolean addPiece(int pieceId, CoordGene<Integer> coord) {
		if (!canAddPiece(pieceId))
			return isGameFinish();
		Piece piece = getCurrentPlayerObj().removePiece(pieceId);
		String notation = Notation.getMoveNotation(board, piece, coord);
		String unplay = Notation.getInverseMoveNotation(board, piece);
		board.addPiece(piece, coord);

		// if (mode == Consts.PVP || currentPlayer == Consts.PLAYER1)
		history.save(notation, unplay);
		nextTurn();
		return playNextTurn();
	}

	public boolean movePiece(CoordGene<Integer> source, CoordGene<Integer> target) {
		Piece piece = board.getTile(source).getPiece();
		String notation = Notation.getMoveNotation(board, piece, target);
		String unplay = Notation.getInverseMoveNotation(board, piece);
		// if (this.mode == Consts.PVP || currentPlayer == Consts.PLAYER1)
		history.save(notation, unplay);
		board.movePiece(source, target);
		nextTurn();
		return playNextTurn();
	}

	public void removePiece(CoordGene<Integer> coord) {
		Piece piece = board.removePiece(coord);
		players[1 - currentPlayer].addPiece(piece);
		;
	}

	private boolean playNextTurn() {
		if (isGameFinish())
			return true;
		if (isPlayerStuck())
			currentPlayer = (1 - currentPlayer);
		if (mode == Consts.PVAI && currentPlayer == Consts.AI_PLAYER) {
			AIMove aiMove = ai.getNextMove(this);
			return aiMove.play();
		}

		return false;
	}

	private boolean canAddPiece(int pieceId) {
		return (turn != 6 && turn != 7) || isQueenOnBoard() || pieceId == Consts.QUEEN;
	}

	private boolean isQueenOnBoard() {
		return getCurrentPlayerObj().getInventory().stream().noneMatch(piece -> piece.getId() == Consts.QUEEN);
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
							&& !(neighbors = board.getPieceNeighbors(current.getCoord())).isEmpty()
							&& !neighbors.stream().anyMatch(it -> it.getPiece().team != currentPlayer))
						pos.add(current.getCoord());
		}
		return pos;
	}

	public boolean previousState() {
		if (history.hasPrevious()) {
			emulator.play(history.getPrevious());
			previousTurn();
			if (mode == Consts.PVAI) {
				emulator.play(history.getPrevious());
				previousTurn();
			}
			return true;
		}
		return false;
	}

	public boolean nextState() {
		if (history.hasNext()) {
			emulator.play(history.getNext());
			nextTurn();
			if (mode == Consts.PVAI) {
				emulator.play(history.getNext());
				nextTurn();
			}
			return true;
		}
		return false;
	}

	public void save(String name) {
		if (name == null) {
			if (mode == Consts.PVP)
				name = players[Consts.PLAYER1].getName() + "-" + players[Consts.PLAYER2].getName() + "-turn" + turn;
			else
				name = players[Consts.PLAYER1].getName() + "-AI_"
						+ (difficulty == Consts.EASY ? "EASY" : difficulty == Consts.MEDIUM ? "MEDIUM" : "HARD")
						+ "-turn" + turn;
		}

		try {
			if (!Files.isDirectory(Paths.get("Hive_save")))
				Files.createDirectories(Paths.get("Hive_save"));
			Path path = Paths.get("Hive_save/" + name);
			BufferedWriter writer = Files.newBufferedWriter(path);
			writer.write(mode + "\n" + currentPlayer + "\n" + players[Consts.PLAYER1].getName() + "\n");
			if (mode == Consts.PVAI)
				writer.write(difficulty+"\n");
			else
				writer.write(players[Consts.PLAYER2].getName()+"\n");
			Stack<String> prevPlay = history.getPrevPlay();
			Stack<String> prevUnplay = history.getPrevUnplay();
			if (prevPlay.size() == prevUnplay.size())
				for (int i = 0; i < prevPlay.size(); i++)
					writer.write(prevPlay.get(i) + ";" + prevUnplay.get(i) + "\n");
			writer.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public List<String> load() {
		try {
			if (!Files.isDirectory(Paths.get("Hive_save")))
				Files.createDirectories(Paths.get("Hive_save"));
			return Files.list(Paths.get("Hive_save")).map(p -> p.getFileName().toFile().getName()).collect(Collectors.toList());
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public void load(String saveFile){
		try {
			Path path = Paths.get("Hive_save/"+saveFile);
			BufferedReader reader = Files.newBufferedReader(path);
			mode = Integer.parseInt(reader.readLine());
			currentPlayer = Integer.parseInt(reader.readLine());
			players[Consts.PLAYER1].setName(reader.readLine());
			if (mode == Consts.PVAI){
				difficulty = Integer.parseInt(reader.readLine());
				ai = AIFactory.buildAI(difficulty, this);
			}
			else
				players[Consts.PLAYER2].setName(reader.readLine());
			String tmp = reader.readLine();
			while(tmp != null && !tmp.isEmpty()){
				String[] token = tmp.split(";");
				emulator.play(token[0]);
				turn++;
				history.save(token[0], token[1]);
				tmp = reader.readLine();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public Player getCurrentPlayerObj() {
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
	public int getMode() {
		return mode;
	}
	public int getStatus() {
		return status;
	}
	public Board getBoard() {
		return board;
	}
	public Player[] getPlayers() {
		return players;
	}
	public int getTurn() {
		return turn;
	}
	public int getCurrentPlayer() {
		return currentPlayer;
	}
}
