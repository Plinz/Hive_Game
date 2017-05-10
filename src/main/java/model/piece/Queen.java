package main.java.model.piece;

import java.util.ArrayList;
import java.util.List;

import main.java.model.Board;
import main.java.model.Piece;
import main.java.model.Rules;
import main.java.model.Tile;
import main.java.utils.Consts;
import main.java.utils.CoordGene;

public class Queen extends Piece {

	private static final long serialVersionUID = -3832557804456133927L;

	public Queen(int team) {
		super(Consts.QUEEN_NAME, team, "Queen description");

		// TODO Auto-generated constructor stub
	}

	@Override
	public List<CoordGene<Integer>> getPossibleMovement(Tile tile, Board board) {
		if (this.possibleMovement != null)
			return this.possibleMovement;

		List<CoordGene<Integer>> list = new ArrayList<CoordGene<Integer>>();
		if (!tile.isBlocked() && Rules.oneHive(board, tile)) {
			List<CoordGene<Integer>> neighbors = tile.getCoord().getNeighbors();
			for (int i = 0; i < neighbors.size(); i++) {
				CoordGene<Integer> curr = neighbors.get(i);
				CoordGene<Integer> prev = i == 0 ? neighbors.get(neighbors.size() - 1) : neighbors.get(i - 1);
				CoordGene<Integer> next = i == neighbors.size() - 1 ? neighbors.get(0) : neighbors.get(i + 1);
				if (board.getTile(curr).getPiece() == null && board.getPieceNeighbors(curr).size() != 1
						&& ((board.getTile(prev).getPiece() != null & board.getTile(next).getPiece() == null)
								|| (board.getTile(prev).getPiece() == null & board.getTile(next).getPiece() != null)))
					list.add(curr);
			}
		}
		this.possibleMovement = list;
		return list;
	}

}
