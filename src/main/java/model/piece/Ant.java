package main.java.model.piece;

import java.util.ArrayList;
import java.util.List;

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
		if (!tile.isBlocked() && Rules.oneHive(board, tile)) {
			CoordGene<Integer> coord = tile.getCoord();

			List<CoordGene<Integer>> neighbors = coord.getNeighbors();
			for (int i = 0; i < neighbors.size(); i++) {
				CoordGene<Integer> curr = neighbors.get(i);
				CoordGene<Integer> prev = i == 0 ? neighbors.get(neighbors.size() - 1) : neighbors.get(i - 1);
				CoordGene<Integer> next = i == neighbors.size() - 1 ? neighbors.get(0) : neighbors.get(i + 1);
				if (board.getTile(curr).getPiece() == null && board.getPieceNeighbors(curr).size() != 1
						&& ((board.getTile(prev).getPiece() != null & board.getTile(next).getPiece() == null)
								|| (board.getTile(prev).getPiece() == null & board.getTile(next).getPiece() != null))) {
					while (curr != null && !curr.equals(tile.getCoord())) {
						list.add(curr);
						curr = nextBox(curr, board, list, tile);
					}
				}
			}
		}
		this.possibleMovement = list;
		return list;

	}

	private CoordGene<Integer> nextBox(CoordGene<Integer> coord, Board board, List<CoordGene<Integer>> list,
			Tile tile) {
		List<CoordGene<Integer>> neighbors = coord.getNeighbors();
		for (int i = 0; i < neighbors.size(); i++) {
			CoordGene<Integer> curr = neighbors.get(i);
			CoordGene<Integer> prev = i == 0 ? neighbors.get(neighbors.size() - 1) : neighbors.get(i - 1);
			CoordGene<Integer> next = i == neighbors.size() - 1 ? neighbors.get(0) : neighbors.get(i + 1);
			if (!list.contains(curr) && board.getTile(curr) != null && board.getTile(curr).getPiece() == null
					&& board.getPieceNeighbors(curr).size() != 0
					&& (board.getTile(prev) == null || board.getTile(next) == null
							|| ((board.getTile(prev).getPiece() != null & board.getTile(next).getPiece() == null)
									|| (board.getTile(prev).getPiece() == null
											& board.getTile(next).getPiece() != null)))
					&& !curr.getNeighbors().contains(tile.getCoord()))
				return curr;
		}
		return null;
	}
}
