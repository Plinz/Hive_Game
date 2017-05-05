package main.java.model;

public class State {

	private Board board;
	private Player[] players;
	private int currentPlayer;

	public State(Board board, Player[] players, int currentPlayer) {
		this.board = board;
		this.players = players;
		this.currentPlayer = currentPlayer;
	}

	public State(State s) {
		this.board = new Board(s.getBoard());
		this.players = new Player[2];
		this.players[0] = new Player(s.getPlayers()[0]);
		this.players[1] = new Player(s.getPlayers()[1]);
		this.currentPlayer = s.getCurrentPlayer();
	}

	public State() {
		this.board = new Board();
		this.players[0] = new Player(0);
		this.players[1] = new Player(1);
		this.currentPlayer = 0;
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

	public int getCurrentPlayer() {
		return currentPlayer;
	}

	public void setCurrentPlayer(int currentPlayer) {
		this.currentPlayer = currentPlayer;
	}

}
