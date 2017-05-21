package main.java.model.piece;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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
		Set<CoordGene<Integer>> set = new HashSet<>();
		List<CoordGene<Integer>> pos = new ArrayList<>();
		if (!tile.isBlocked()){
			List<Tile> neigbors = board.getPieceNeighbors(tile.getCoord());
			for (Tile t : neigbors)
				set.addAll(t.getPiece().getPossibleMovement(tile, board));
			pos.addAll(set);
		}
		return pos;
	}



}
