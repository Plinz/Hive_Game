package main.java.model.piece;

import java.util.List;

import main.java.model.Board;
import main.java.model.Piece;
import main.java.model.Tile;
import main.java.utils.CoordGene;

public class Mosquito extends Piece{

	public Mosquito(int id, int team) {
		super("Mosquito", id, team, "Mosquito description");
	}

	@Override
	public List<CoordGene<Integer>> getPossibleMovement(Tile tile, Board board) {
		// TODO Auto-generated method stub
		return null;
	}



}
