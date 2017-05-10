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

public class Spider extends Piece {

	private static final long serialVersionUID = 1194394371161156625L;

	public Spider(int id, int team) {
		super(Consts.SPIDER_NAME, id, team, "Spider description");
	}

	@Override
	public List<CoordGene<Integer>> getPossibleMovement(Tile tile, Board board) {
		if (this.possibleMovement != null)
			return this.possibleMovement;
		List<CoordGene<Integer>> list = new ArrayList<CoordGene<Integer>>();
		if (!tile.isBlocked() && Rules.oneHive(board, tile))
			list.addAll(path(board, tile.getCoord(), 0, new HashSet<CoordGene<Integer>>(), tile.getCoord()));
		this.possibleMovement = list;
		return list;

	}

	private List<CoordGene<Integer>> path(Board board, CoordGene<Integer> origin, int deep,
			Set<CoordGene<Integer>> visited, CoordGene<Integer> from) {
		List<CoordGene<Integer>> nextBox = new ArrayList<CoordGene<Integer>>();
		if (deep == 3) {
			if (!visited.contains(from))
				nextBox.add(from);
			return nextBox;
		}
		visited.add(from);
		List<CoordGene<Integer>> neighbors = from.getNeighbors();
		for (int i = 0; i < neighbors.size(); i++) {
			CoordGene<Integer> target = neighbors.get(i);
			CoordGene<Integer> left = neighbors.get(Math.floorMod(i - 1, 6));
			CoordGene<Integer> right = neighbors.get(Math.floorMod(i + 1, 6));

			if (board.getTile(target) != null && board.getTile(target).getPiece() == null
					&& Rules.freedomToMove(board, 0, left, right, origin)
					&& Rules.permanentContact(board, 0, left, right, origin))
				nextBox.addAll(path(board, origin, deep + 1, visited, target));
		}
		return nextBox;
	}
}
