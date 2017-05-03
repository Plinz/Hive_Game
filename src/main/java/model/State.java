package main.java.model;

public class State {
	
	private Board board;
	private Player player1;
	private Player player2;
	
	public State(Board board, Player player1, Player player2) {
		this.board = board;
		this.player1 = player1;
		this.player2 = player2;
	}
	
	public State() {
		this.board = new Board();
		this.player1 = new Player();
		this.player2 = new Player();
	}

	public Board getBoard() {
		return board;
	}
	public void setBoard(Board board) {
		this.board = board;
	}
	public Player getPlayer1() {
		return player1;
	}
	public void setPlayer1(Player player1) {
		this.player1 = player1;
	}
	public Player getPlayer2() {
		return player2;
	}
	public void setPlayer2(Player player2) {
		this.player2 = player2;
	}
	

}
