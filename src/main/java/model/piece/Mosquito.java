package main.java.model.piece;

import java.util.List;

import main.java.model.Board;
import main.java.model.Piece;
import main.java.model.Tile;
import main.java.utils.Coord;

public class Mosquito extends Piece{

	public Mosquito(int team) {
		super("Mosquito", team, "Mosquito description");
		// TODO Auto-generated constructor stub
	}

	@Override
	public List<Coord> getPossibleMovement(Tile tile, Board board) {
		// TODO Auto-generated method stub
		return null;
	}



}
