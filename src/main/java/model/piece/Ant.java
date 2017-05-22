package main.java.model.piece;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import main.java.model.Board;
import main.java.model.Piece;
import main.java.model.Tile;
import main.java.utils.Consts;
import main.java.utils.CoordGene;

public class Ant extends Piece {

	public Ant(int id, int team) {
		super(Consts.ANT_NAME, id, team, "La fourmi peut se déplacer d'autant d'espaces que le joueur le désire");
        }

	@Override
	public List<CoordGene<Integer>> getPossibleMovement(Tile tile, Board board) {
		if (this.possibleMovement != null)
			return this.possibleMovement;
		List<CoordGene<Integer>> list = new ArrayList<>();
		if (!tile.isBlocked() && board.oneHive(tile))
			list.addAll(path(board, tile.getCoord(), new HashSet<>(), tile.getCoord()));
		this.possibleMovement = list;
		return list;

	}

	private List<CoordGene<Integer>> path(Board board, CoordGene<Integer> origin, Set<CoordGene<Integer>> visited,
			CoordGene<Integer> from) {
		List<CoordGene<Integer>> nextBox = new ArrayList<>();
		List<CoordGene<Integer>> neighbors = from.getNeighbors();
		for (int i = 0; i < neighbors.size(); i++) {
			CoordGene<Integer> target = neighbors.get(i);
			CoordGene<Integer> left = neighbors.get(Math.floorMod(i - 1, 6));
			CoordGene<Integer> right = neighbors.get(Math.floorMod(i + 1, 6));

			if (!visited.contains(target) && board.getTile(target) != null && board.getTile(target).getPiece() == null
					&& board.freedomToMove(0, left, right, origin)
					&& board.permanentContact(0, left, right, origin)) {
				nextBox.add(target);
				visited.add(target);
				nextBox.addAll(path(board, origin, visited, target));
			}
		}
		return nextBox;
	}
}
