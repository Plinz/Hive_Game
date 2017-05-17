package main.java.model.piece;

import java.util.ArrayList;
import java.util.List;

import main.java.model.Board;
import main.java.model.Piece;
import main.java.model.Tile;
import main.java.utils.Consts;
import main.java.utils.CoordGene;

public class Queen extends Piece {

	public Queen(int id, int team) {
		super(Consts.QUEEN_NAME, id, team, "Queen description");
	}

	@Override
	public List<CoordGene<Integer>> getPossibleMovement(Tile tile, Board board) {
		if (this.possibleMovement != null)
			return this.possibleMovement;

		List<CoordGene<Integer>> list = new ArrayList<>();
		if (!tile.isBlocked() && board.oneHive(tile)) {
			List<CoordGene<Integer>> neighbors = tile.getCoord().getNeighbors();
			for (int i = 0; i < neighbors.size(); i++) {
				CoordGene<Integer> target = neighbors.get(i);
				CoordGene<Integer> left = neighbors.get(Math.floorMod(i - 1, 6));
				CoordGene<Integer> right = neighbors.get(Math.floorMod(i + 1, 6));

				if (board.getTile(target).getPiece() == null
						&& board.freedomToMove(0, left, right, tile.getCoord())
						&& board.permanentContact(0, left, right, tile.getCoord()))
					list.add(target);
			}
		}
		this.possibleMovement = list;
		return list;
	}

}
