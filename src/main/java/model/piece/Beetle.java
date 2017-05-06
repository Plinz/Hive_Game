package main.java.model.piece;

import java.util.ArrayList;
import java.util.List;

import main.java.model.Board;
import main.java.model.Piece;
import main.java.model.Tile;
import main.java.utils.Consts;
import main.java.utils.Coord;

public class Beetle extends Piece {

	public Beetle(int team) {
		super(Consts.BEETLE_NAME, team, "Beetle description");
	}

	@Override
	public List<Coord> getPossibleMovement(Tile tile, Board board) {
		//if (this.possibleMovement != null)
		//	return this.possibleMovement;
                
		List<Coord> list = new ArrayList<Coord>();
		if (!tile.isBlocked()) {
			List<Coord> neighbors = tile.getCoord().getNeighbors();
			for (int i = 0; i < neighbors.size(); i++) {
				Coord curr = neighbors.get(i);
				Coord prev = i == 0 ? neighbors.get(neighbors.size() - 1) : neighbors.get(i - 1);
				Coord next = i == neighbors.size() - 1 ? neighbors.get(0) : neighbors.get(i + 1);
				if (board.getPieceNeighbors(curr).size() != 1 && respectMovementRule(tile, prev, next, curr, board))
					list.add(curr);
			}
		}
		this.possibleMovement = list;
		return list;
	}

	private boolean respectMovementRule(Tile from, Coord leftCoord, Coord rightCoord, Coord targetCoord, Board board) {
		Tile target = board.getTile(targetCoord);
		Tile left = board.getTile(leftCoord);
		Tile right = board.getTile(rightCoord);
		int floor = target.getZ() < from.getZ() ? from.getZ() : target.getZ();
		return ((left.getPiece() != null & right.getPiece() == null)
				|| (left.getPiece() == null & right.getPiece() != null) || left.getZ() < floor || right.getZ() < floor);
	}
}
