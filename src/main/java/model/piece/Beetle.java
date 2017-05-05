package main.java.model.piece;

import java.util.ArrayList;
import java.util.List;

import main.java.model.Board;
import main.java.model.Piece;
import main.java.model.Tile;
import main.java.utils.Coord;

public class Beetle extends Piece {

	public Beetle(int team) {
		super("Beetle", team, "Beetle description");
	}

	@Override
	public List<Coord> getPossibleMovement(Tile tile, Board board) {
		if (this.possibleMovement != null)
			return this.possibleMovement;
		List<Coord> list = new ArrayList<Coord>();
		if (!tile.isBlocked()) {
			Coord coord = tile.getCoord();
			if (board.getPieceNeighbors(coord.getEast()).size() != 1
					&& respectMovementRule(tile, coord.getSouthEast(), coord.getNorthEast(), coord.getEast(), board))
				list.add(coord.getEast());
			if (board.getPieceNeighbors(coord.getSouthEast()).size() != 1
					&& respectMovementRule(tile, coord.getSouthWest(), coord.getEast(), coord.getSouthEast(), board))
				list.add(coord.getSouthEast());
			if (board.getPieceNeighbors(coord.getSouthWest()).size() != 1
					&& respectMovementRule(tile, coord.getWest(), coord.getSouthEast(), coord.getSouthWest(), board))
				list.add(coord.getSouthWest());
			if (board.getPieceNeighbors(coord.getWest()).size() != 1
					&& respectMovementRule(tile, coord.getNorthWest(), coord.getSouthWest(), coord.getWest(), board))
				list.add(coord.getWest());
			if (board.getPieceNeighbors(coord.getNorthWest()).size() != 1
					&& respectMovementRule(tile, coord.getNorthEast(), coord.getWest(), coord.getNorthWest(), board))
				list.add(coord.getNorthWest());
			if (board.getPieceNeighbors(coord.getNorthEast()).size() != 1
					&& respectMovementRule(tile, coord.getEast(), coord.getNorthWest(), coord.getNorthEast(), board))
				list.add(coord.getNorthEast());
		}
		this.possibleMovement = list;
		return list;
	}

	private boolean respectMovementRule(Tile from, Coord leftCoord, Coord rightCoord, Coord targetCoord, Board board) {
		Tile target = board.getTile(targetCoord);
		Tile left = board.getTile(leftCoord);
		Tile right = board.getTile(rightCoord);
		int floor = target.getZ() < from.getZ() ? from.getZ() : target.getZ();
		return (left.getPiece() == null || left.getZ() < floor || right.getPiece() == null || right.getZ() < floor);
	}
}
