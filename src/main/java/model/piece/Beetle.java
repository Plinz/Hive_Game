package main.java.model.piece;

import java.util.List;

import main.java.model.Board;
import main.java.model.Piece;
import main.java.utils.Coord;

public class Beetle extends Piece{

	public Beetle(int team) {
		super("Beetle", team, "Beetle description");
	}

	@Override
	public List<Coord> getPossibleMovement(Coord coord, int floor, Board board) {
		// TODO Auto-generated method stub
		return null;
	}

}
