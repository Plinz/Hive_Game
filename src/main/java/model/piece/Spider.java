package main.java.model.piece;

import java.util.ArrayList;
import java.util.List;

import main.java.model.Board;
import main.java.model.Piece;
import main.java.model.Rules;
import main.java.model.Tile;
import main.java.utils.Consts;
import main.java.utils.CoordGene;

public class Spider extends Piece {

	private static final long serialVersionUID = 1194394371161156625L;

	public Spider(int team) {
		super(Consts.SPIDER_NAME, team, "Spider description");
	}

	@Override
	public List<CoordGene<Integer>> getPossibleMovement(Tile tile, Board board) {
		if (this.possibleMovement != null)
			return this.possibleMovement;
		List<CoordGene<Integer>> list = new ArrayList<CoordGene<Integer>>();
		if (!tile.isBlocked() && Rules.oneHive(board, tile)) {
			CoordGene<Integer> coord = tile.getCoord();
			int deep = 0;
			List<CoordGene<Integer>> neighbors = coord.getNeighbors();
			for (int i = 0; i < neighbors.size(); i++) {
				CoordGene<Integer> curr = neighbors.get(i);
				CoordGene<Integer> prev = i == 0 ? neighbors.get(neighbors.size() - 1) : neighbors.get(i - 1);
				CoordGene<Integer> next = i == neighbors.size() - 1 ? neighbors.get(0) : neighbors.get(i + 1);

				if (board.getTile(curr).getPiece() == null && board.getPieceNeighbors(curr).size() != 1
						&& ((board.getTile(prev).getPiece() != null & board.getTile(next).getPiece() == null)
								|| (board.getTile(prev).getPiece() == null & board.getTile(next).getPiece() != null))) {
					while (deep++ < 2 && curr != null && !curr.equals(tile.getCoord())) {
						curr = nextBox(curr, board, list, tile);
						System.err.println(curr);
					}
					deep = 0;
					if (curr != null)
						list.add(curr);
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
