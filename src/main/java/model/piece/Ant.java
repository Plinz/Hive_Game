package main.java.model.piece;

import java.util.List;

import main.java.model.Board;
import main.java.model.Piece;
import main.java.model.Tile;
import main.java.utils.Coord;

public class Ant extends Piece{
	
	public Ant(int team) {
		super("Fourmi", team, "Fourmi description");
	}

	@Override
	public List<Coord> getPossibleMovement(Tile tile, Board board) {
		// TODO Auto-generated method stub
		return null;
	}


	
	

}
