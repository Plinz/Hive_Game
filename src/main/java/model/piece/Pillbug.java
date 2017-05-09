package main.java.model.piece;

import java.util.List;

import main.java.model.Board;
import main.java.model.Piece;
import main.java.model.Tile;
import main.java.utils.CoordGene;

public class Pillbug extends Piece{

	private static final long serialVersionUID = 927638636316794861L;

	public Pillbug(int team) {
		super("Pillbug", team, "Pillbug description");
	}

	@Override
	public List<CoordGene<Integer>> getPossibleMovement(Tile tile, Board board) {
		// TODO Auto-generated method stub
		return null;
	}

}
