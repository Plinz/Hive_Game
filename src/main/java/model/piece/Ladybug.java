package main.java.model.piece;

import java.util.List;

import main.java.model.Board;
import main.java.model.Piece;
import main.java.model.Tile;
import main.java.utils.CoordGene;

public class Ladybug extends Piece{

	private static final long serialVersionUID = -6861601222826362762L;

	public Ladybug(int id, int team) {
		super("Ladybug", id, team, "Ladybug description");
		// TODO Auto-generated constructor stub

	}

	@Override
	public List<CoordGene<Integer>> getPossibleMovement(Tile tile, Board board) {
		// TODO Auto-generated method stub
		return null;
	}



}
