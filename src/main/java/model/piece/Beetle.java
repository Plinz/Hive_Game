package main.java.model.piece;

import java.util.ArrayList;
import java.util.List;

import main.java.model.Board;
import main.java.model.Piece;
import main.java.model.Rules;
import main.java.model.Tile;
import main.java.utils.Consts;
import main.java.utils.CoordGene;

public class Beetle extends Piece {

	private static final long serialVersionUID = -8993644035879598044L;

	public Beetle(int team) {
		super(Consts.BEETLE_NAME, team, "Beetle description");
	}

	@Override
	public List<CoordGene<Integer>> getPossibleMovement(Tile tile, Board board) {
		if (this.possibleMovement != null)
			return this.possibleMovement;
		List<CoordGene<Integer>> list = new ArrayList<CoordGene<Integer>>();
		if (!tile.isBlocked() && Rules.oneHive(board, tile)) {
			List<CoordGene<Integer>> neighbors = tile.getCoord().getNeighbors();
			for (int i = 0; i < neighbors.size(); i++) {
				Tile target = board.getTile(neighbors.get(i));
				CoordGene<Integer> left = neighbors.get(Math.floorMod(i - 1, 6));
				CoordGene<Integer> right = neighbors.get(Math.floorMod(i + 1, 6));
				int floor;
				if (target.getZ() == 0 && tile.getZ() == 0)
					floor = (target.getPiece() == null) ? 0 : 1;
				else
					floor = Math.max(target.getZ(), tile.getZ());
				if (Rules.freedomToMove(board, floor, left, right, tile.getCoord())
						&& Rules.permanentContact(board, floor, left, right, tile.getCoord()))
					list.add(target.getCoord());
			}
		}
		this.possibleMovement = list;
		return list;
	}
}
