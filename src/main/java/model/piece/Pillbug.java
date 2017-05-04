package main.java.model.piece;

import java.util.List;

import main.java.model.Board;
import main.java.model.Piece;
import main.java.model.Tile;
import main.java.utils.Coord;

public class Pillbug extends Piece{

	public Pillbug(int team) {
		super("Pillbug", team, "Pillbug description");
	}

	@Override
	public List<Coord> getPossibleMovement(Tile tile, Board board) {
		// TODO Auto-generated method stub
		return null;
	}

}
