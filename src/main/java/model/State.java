package main.java.model;

import java.io.Serializable;

public class State implements Serializable{

	private static final long serialVersionUID = -7410268289229213472L;
	private Board board;
	private Player[] players;
	private int currentPlayer;
	private int turn;

	public State(Board board, Player[] players, int currentPlayer, int turn) {
		this.board = board;
		this.players = players;
		this.currentPlayer = currentPlayer;
		this.turn = turn;
	}

	public State(State s) {
		this.board = new Board(s.getBoard());
		this.players = new Player[2];
		this.players[0] = new Player(s.getPlayers()[0]);
		this.players[1] = new Player(s.getPlayers()[1]);
		this.currentPlayer = s.getCurrentPlayer();
		this.turn = s.getTurn();
	}

	public State() {
		this.board = new Board();
		this.players = new Player[2];
		this.players[0] = new Player(0);
		this.players[1] = new Player(1);
		this.currentPlayer = 0;
		this.turn = 0;
	}

	public void nextTurn(){
		this.turn++;
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

	public int getTurn() {
		return turn;
	}

	public void setTurn(int turn) {
		this.turn = turn;
	}

}
