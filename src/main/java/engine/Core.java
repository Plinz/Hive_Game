package main.java.engine;

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

import javafx.application.Platform;
import main.java.controller.GameScreenController;
import main.java.ia.AI;
import main.java.ia.AIFactory;
import main.java.io.Client;
import main.java.io.IO;
import main.java.io.Server;
import main.java.model.Board;
import main.java.model.Box;
import main.java.model.Column;
import main.java.model.HelpMove;
import main.java.model.Piece;
import main.java.model.Player;
import main.java.model.Tile;
import main.java.utils.Consts;
import main.java.utils.CoordGene;
import main.java.utils.Tuple;
import main.java.view.BoardDrawer;

public class Core implements Cloneable {

	private History history;
	private Board board;
	private Player[] players;
	private Emulator emulator;
	private AI ai;
	private IO io;
	private int mode;
	private int status;
	private int turn;
	private int currentPlayer;
	private int difficulty;
	private int state;
	private GameScreenController gameScreen;
	private List<Tuple<String, Integer>> lastMsg;
	public Core(int mode, int difficulty) {
		this.history = new History();
		this.board = new Board();
		this.players = new Player[2];
		this.players[0] = new Player(0);
		this.players[1] = new Player(1);
		this.emulator = new Emulator(this, board, players);
		if (mode == Consts.PVAI || mode == Consts.AIVP)
			this.ai = AIFactory.buildAI(difficulty, this);
		this.mode = mode;
		this.status = Consts.INGAME;
		this.turn = 0;
		this.currentPlayer = Consts.PLAYER1;
		this.difficulty = difficulty;
		this.state = Consts.WAIT_FOR_INPUT;
		this.lastMsg = new ArrayList<>();
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

	public void playNextTurn() {
		state = Consts.WAIT_FOR_INPUT;
		if ((mode == Consts.PVAI && currentPlayer == Consts.PLAYER2)
				|| (mode == Consts.AIVP && currentPlayer == Consts.PLAYER1)) {
			state = Consts.PROCESSING;
			playAI();
		} else if ((mode == Consts.PVEX && currentPlayer == Consts.PLAYER2)
				|| (mode == Consts.EXVP && currentPlayer == Consts.PLAYER1))
			state = Consts.PROCESSING;
	}

	public boolean addPiece(int pieceId, CoordGene<Integer> coord) {
		state = Consts.PROCESSING;
		//if (!canAddPiece(pieceId))
		//	return isGameFinish();
		Piece piece = getCurrentPlayerObj().removePiece(pieceId);
		String notation = Notation.getMoveNotation(board, piece, coord);
		String unplay = Notation.getInverseMoveNotation(board, piece);
		board.addPiece(piece, coord);
		history.save(notation, unplay);
		nextTurn();
		if (mode == Consts.PVEX || mode == Consts.EXVP)
			io.sendMove(notation+";"+unplay);
		state = Consts.READY_TO_CHANGE;
		return status != Consts.INGAME;
	}

	public boolean movePiece(CoordGene<Integer> source, CoordGene<Integer> target) {
		state = Consts.PROCESSING;
		Piece piece = board.getTile(source).getPiece();
		String notation = Notation.getMoveNotation(board, piece, target);
		String unplay = Notation.getInverseMoveNotation(board, piece);
		history.save(notation, unplay);
		board.movePiece(source, target);
		nextTurn();
		if (mode == Consts.PVEX || mode == Consts.EXVP)
			io.sendMove(notation + ";" + unplay);
		state = Consts.READY_TO_CHANGE;
		return status != Consts.INGAME;
	}

	public void removePiece(CoordGene<Integer> coord) {
		Piece piece = board.removePiece(coord);
		players[1 - currentPlayer].addPiece(piece);
	}

	public void playAI() {
		String[] moveAndUnplay = ai.getNextMove().split(";");
		HelpMove iaMove = emulator.getMove(moveAndUnplay[0]);
		if (iaMove.isAdd()) {
			gameScreen.startPlacingAIAnimation(iaMove.getPieceId(), iaMove.getTarget(), moveAndUnplay[0],
					moveAndUnplay[1]);
		} else {
			gameScreen.startMovingAIAnimation(iaMove.getFrom(), iaMove.getTarget(), moveAndUnplay[0], moveAndUnplay[1]);
		}
	}

	public void playExtern(String play, String unplay) {
		HelpMove externMove = emulator.getMove(play);
		if (externMove.isAdd()){
			Platform.runLater(new Runnable() {
				@Override
				public void run() {
					gameScreen.startPlacingAIAnimation(externMove.getPieceId(), externMove.getTarget(), play, unplay);
				}
			});
		} else{
			Platform.runLater(new Runnable() {
				@Override
				public void run() {
					gameScreen.startMovingAIAnimation(externMove.getFrom(), externMove.getTarget(), play, unplay);
				}
			});
		}
	}

	public void playEmulate(String play, String unplay) {
		emulator.play(play);
		history.save(play, unplay);
		nextTurn();
		isGameFinish();
	}

	public boolean canAddPiece(int pieceId) {
		return (turn != 6 && turn != 7) || isQueenOnBoard(currentPlayer) || pieceId == Consts.QUEEN;
	}

	public boolean isQueenOnBoard(int player) {
		return getCurrentPlayerObj().getInventory().stream().noneMatch(piece -> piece.getId() == Consts.QUEEN);
	}

	public boolean isGameFinish() {
		List<Tile> queenStuck = new ArrayList<Tile>();
		board.getBoard().stream()
				.forEach(column -> column.stream()
						.forEach(box -> queenStuck.addAll(box.stream()
								.filter(tile -> tile.getPiece() != null && tile.getPiece().getId() == Consts.QUEEN
										&& board.getPieceNeighbors(tile.getCoord()).size() == 6)
								.collect(Collectors.toList()))));
		if (queenStuck.size() == 1)
			status = queenStuck.get(0).getPiece().getTeam() + 1;
		else if (queenStuck.size() == 2)
			status = Consts.NUL;
		return !queenStuck.isEmpty();
	}

	private boolean isPlayerStuck() {
		int team = getCurrentPlayerObj().getTeam();
		List<CoordGene<Integer>> possibleMovement = getCurrentPlayerObj().getInventory().isEmpty()
				? new ArrayList<CoordGene<Integer>>() : getPossibleAdd(currentPlayer);
		board.getBoard().stream()
				.forEach(column -> column.stream().forEach(box -> box.stream()
						.filter(tile -> tile.getPiece() != null).filter(tile -> tile.getPiece().getTeam() == team)
						.forEach(tile -> possibleMovement.addAll(tile.getPiece().getPossibleMovement(tile, board)))));
		return possibleMovement.isEmpty();
	}

	public List<CoordGene<Integer>> getPossibleMovement(CoordGene<Integer> coord) {
		if (isQueenOnBoard(currentPlayer)) {
			Tile tile = board.getTile(coord);
			return tile.getPiece().getPossibleMovement(tile, board);
		}
		return new ArrayList<CoordGene<Integer>>();
	}

	public List<CoordGene<Integer>> getPossibleAdd(int player) {
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
							&& !neighbors.stream().anyMatch(it -> it.getPiece().getTeam() != player))
						pos.add(current.getCoord());
		}
		return pos;
	}

	public boolean hasPreviousState() {
		return history.hasPrevious();
	}

	public boolean hasNextState() {
		return history.hasNext();
	}

	public boolean previousState() {
		if (history.hasPrevious()) {
			String notation = history.getPrevious();
			emulator.play(notation);
			status = Consts.INGAME;
			previousTurn();
			if (Consts.getPlayer(notation.charAt(0)) != players[currentPlayer].getTeam())
				currentPlayer = 1 - currentPlayer;
			return true;
		}
		return false;
	}

	public boolean nextState() {
		if (history.hasNext()) {
			emulator.play(history.getNext());
			nextTurn();
			isGameFinish();
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
			if (!Files.isDirectory(Paths.get("Hive_init/Hive_save")))
				Files.createDirectories(Paths.get("Hive_init/Hive_save"));
			Path path = Paths.get("Hive_init/Hive_save/" + name);
			BufferedWriter writer = Files.newBufferedWriter(path);
			
			if (mode == Consts.PVAI){
                                writer.write(mode + "\n" + currentPlayer + "\n" + players[Consts.PLAYER1].getName() + "\n");
				writer.write(difficulty + "\n");
                        }
                        else if(mode == Consts.AIVP){
                                writer.write(mode + "\n" + currentPlayer + "\n" + players[Consts.PLAYER2].getName() + "\n");
				writer.write(difficulty + "\n");
                        }
                        else{
                                writer.write(mode + "\n" + currentPlayer + "\n" + players[Consts.PLAYER1].getName() + "\n");
				writer.write(players[Consts.PLAYER2].getName() + "\n");
                        }
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
			if (!Files.isDirectory(Paths.get("Hive_init/Hive_save")))
				Files.createDirectories(Paths.get("Hive_init/Hive_save"));
			return Files.list(Paths.get("Hive_init/Hive_save")).map(p -> p.getFileName().toFile().getName())
					.collect(Collectors.toList());
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	public void load(String saveFile) {
		try {
			Path path = Paths.get("Hive_init/Hive_save/" + saveFile);
			BufferedReader reader = Files.newBufferedReader(path);
			mode = Integer.parseInt(reader.readLine());
			currentPlayer = Integer.parseInt(reader.readLine());
			if (mode == Consts.PVAI) {
                                players[Consts.PLAYER1].setName(reader.readLine());
				difficulty = Integer.parseInt(reader.readLine());
				ai = AIFactory.buildAI(difficulty, this);
                                players[Consts.PLAYER2].setName(Consts.IA_NAME[difficulty]);
                        }
                        else if(mode == Consts.AIVP){
                                players[Consts.PLAYER2].setName(reader.readLine());
                                difficulty = Integer.parseInt(reader.readLine());
				ai = AIFactory.buildAI(difficulty, this);
                                players[Consts.PLAYER1].setName(Consts.IA_NAME[difficulty]);
                        
			} else{
                                players[Consts.PLAYER1].setName(reader.readLine());
				players[Consts.PLAYER2].setName(reader.readLine());
                        }
			String tmp = reader.readLine();
			while (tmp != null && !tmp.isEmpty()) {
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
		if (isPlayerStuck())
			currentPlayer = 1 - currentPlayer;
		turn++;
	}

	private void previousTurn() {
		currentPlayer = 1 - currentPlayer;
		board.clearPossibleMovement();
		if (isPlayerStuck())
			currentPlayer = 1 - currentPlayer;
		turn--;
	}

	public HelpMove help() {
		AI helpAI = AIFactory.buildAI(Consts.EASY, this);
		return emulator.getMove(helpAI.getNextMove().split(";")[0]);
	}

	public void connect(String host, int mode, String playerName) {
		if (mode == -1) {
			io = new Client(this);
			io.connect(host);
			while (!io.sendInfo(playerName))
				;
			while (!io.updateInfo())
				;
			(this.mode == Consts.PVEX ? players[Consts.PLAYER1] : players[Consts.PLAYER2]).setName(playerName);
		} else {
			this.mode = mode;
			(mode == Consts.PVEX ? players[Consts.PLAYER1] : players[Consts.PLAYER2]).setName(playerName);
			io = new Server(this);
			io.connect(null);
			while (!io.sendInfo(playerName))
				;
			while (!io.updateInfo())
				;
		}
	}
	
	public void sendMessage(String message){
		lastMsg.add(0, new Tuple<String, Integer>(message, (mode==Consts.PVEX?Consts.PLAYER1:Consts.PLAYER2)));
		io.sendMessage(message);
	}
	
	public void newMessage(String message){
		lastMsg.add(0, new Tuple<String, Integer>(message, (mode==Consts.PVEX?Consts.PLAYER2:Consts.PLAYER1)));
	}

	@Override
	protected Core clone() {
		Core core = null;
		try {
			core = (Core) super.clone();
		} catch (CloneNotSupportedException e) {
			e.printStackTrace();
		}
		core.history = history.clone();
		core.board = board.clone();
		core.players = players.clone();
		core.emulator = new Emulator(core, core.board, core.players);
		core.ai = AIFactory.buildAI(difficulty, this);
		core.mode = mode;
		core.status = status;
		core.turn = turn;
		core.currentPlayer = currentPlayer;
		core.difficulty = difficulty;
		return core;
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

	public int getDifficulty() {
		return difficulty;
	}

	public void setDifficulty(int difficulty) {
		this.difficulty = difficulty;
	}

	public int getState() {
		return state;
	}

	public void setState(int state) {
		this.state = state;
	}

	public void setGameScreen(GameScreenController gameScreen) {
		this.gameScreen = gameScreen;
	}
	
	public List<Tuple<String, Integer>> getLastMsg() {
		return lastMsg;
	}

	public void setLastMsg(List<Tuple<String, Integer>> lastMsg) {
		this.lastMsg = lastMsg;
	}

}
