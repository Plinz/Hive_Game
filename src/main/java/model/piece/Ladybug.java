package main.java.model.piece;

import java.util.List;

import main.java.model.Board;
import main.java.model.Piece;
import main.java.model.Tile;
import main.java.utils.CoordGene;

public class Ladybug extends Piece{

	public Ladybug(int id, int team) {
		super("Ladybug", id, team, "Ladybug description");
	}

	@Override
	public List<CoordGene<Integer>> updatePossibleMovement(Tile tile, Board board) {
		// TODO Auto-generated method stub
		return null;
	}



}
