package main.java.model;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;


public class State {

	private Board board;
	private Player[] players;
	private IntegerProperty currentPlayer;

	public State(Board board, Player[] players, int currentPlayer) {
		this.board = board;
		this.players = players;
                this.currentPlayer = new SimpleIntegerProperty();
		this.currentPlayer.setValue(currentPlayer);
                
	}

	public State(State s) {
		this.board = new Board(s.getBoard());
		this.players = new Player[2];
		this.players[0] = new Player(s.getPlayers()[0]);
		this.players[1] = new Player(s.getPlayers()[1]);
                this.currentPlayer = new SimpleIntegerProperty();
		this.currentPlayer.setValue(s.getCurrentPlayer().intValue());
	}

	public State() {
		this.board = new Board();
		this.players = new Player[2];
		this.players[0] = new Player(0);
		this.players[1] = new Player(1);
                this.currentPlayer = new SimpleIntegerProperty();
		this.currentPlayer.setValue(0);
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

	public IntegerProperty getCurrentPlayer() {
		return currentPlayer;
	}

	public void setCurrentPlayer(int currentPlayer) {
		this.currentPlayer.setValue(currentPlayer);
	}

}
