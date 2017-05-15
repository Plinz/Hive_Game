package main.java.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="state")
@XmlAccessorType(XmlAccessType.FIELD)
public class State {

	
    @XmlElement(name="board")
    private Board board;
    @XmlElement(name="players")
    private Player[] players;
    @XmlElement(name="currentPlayer")
    private int currentPlayer;
    @XmlAttribute(name="turn")
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

    public void nextTurn() {
		currentPlayer = 1 - currentPlayer;
		board.clearPossibleMovement();
        this.turn++;
    }
    
	public void previousTurn() {
		currentPlayer = 1 - currentPlayer;
		board.clearPossibleMovement();
        this.turn--;		
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
    
    public Player getCurrentPlayerObject() {
        return players[currentPlayer];
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
