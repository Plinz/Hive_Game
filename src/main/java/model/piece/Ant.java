package main.java.model.piece;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import main.java.model.Board;
import main.java.model.Piece;
import main.java.model.Rules;
import main.java.model.Tile;
import main.java.utils.Consts;
import main.java.utils.CoordGene;

public class Ant extends Piece {

	private static final long serialVersionUID = -8220106922861765305L;

	public Ant(int team) {
		super(Consts.ANT_NAME, team, "Fourmi description");
	}

	@Override
	public List<CoordGene<Integer>> getPossibleMovement(Tile tile, Board board) {
		if (this.possibleMovement != null)
			return this.possibleMovement;
		List<CoordGene<Integer>> list = new ArrayList<CoordGene<Integer>>();
		if (!tile.isBlocked() && Rules.oneHive(board, tile))
			list.addAll(path(board, tile.getCoord(), new HashSet<CoordGene<Integer>>(), tile.getCoord()));
		list.remove(tile.getCoord());
		this.possibleMovement = list;
		return list;

	}

	private List<CoordGene<Integer>> path(Board board, CoordGene<Integer> origin, Set<CoordGene<Integer>> visited,
			CoordGene<Integer> from) {
		List<CoordGene<Integer>> nextBox = new ArrayList<CoordGene<Integer>>();
		if (!visited.contains(from)){
			nextBox.add(from);
			visited.add(from);
			List<CoordGene<Integer>> neighbors = from.getNeighbors();
			for (int i = 0; i < neighbors.size(); i++) {
				CoordGene<Integer> target = neighbors.get(i);
				CoordGene<Integer> left = neighbors.get(Math.floorMod(i - 1, 6));
				CoordGene<Integer> right = neighbors.get(Math.floorMod(i + 1, 6));
	
				if (board.getTile(target) != null && board.getTile(target).getPiece() == null
						&& Rules.freedomToMoveAndPermanentContact(board, left, right, origin))
					if(board.getPieceNeighbors(target).size() != 1 || !board.getPieceNeighbors(target).get(0).getCoord().equals(origin))
						nextBox.addAll(path(board, origin, visited, target));
			}
		}
		return nextBox;
	}
}
