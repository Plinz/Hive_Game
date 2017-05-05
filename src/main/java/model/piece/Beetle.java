package main.java.model.piece;

import java.util.ArrayList;
import java.util.List;

import main.java.model.Board;
import main.java.model.Piece;
import main.java.model.Tile;
import main.java.utils.Coord;

public class Beetle extends Piece{

	public Beetle(int team) {
		super("Beetle", team, "Beetle description");
	}

	@Override
	public List<Coord> getPossibleMovement(Tile tile, Board board) {
		List<Coord> list = new ArrayList<Coord>();
		if (tile.isBlocked())
			return list;
		
		Coord coord = tile.getCoord();
		if (board.getNeighbors(coord.getEast()).size()!=1 && respectMovementRule(tile, coord.getSouthEast(), coord.getNorthEast(), coord.getEast(), board))
			list.add(coord.getEast());
		if (board.getNeighbors(coord.getSouthEast()).size()!=1 && respectMovementRule(tile, coord.getSouthWest(), coord.getEast(), coord.getSouthEast(), board))
			list.add(coord.getSouthEast());
		if (board.getNeighbors(coord.getSouthWest()).size()!=1 && respectMovementRule(tile, coord.getWest(), coord.getSouthEast(), coord.getSouthWest(), board))
			list.add(coord.getSouthWest());
		if (board.getNeighbors(coord.getWest()).size()!=1 && respectMovementRule(tile, coord.getNorthWest(), coord.getSouthWest(), coord.getWest(), board))
			list.add(coord.getWest());
		if (board.getNeighbors(coord.getNorthWest()).size()!=1 && respectMovementRule(tile, coord.getNorthEast(), coord.getWest(), coord.getNorthWest(), board))
			list.add(coord.getNorthWest());
		if (board.getNeighbors(coord.getNorthEast()).size()!=1 && respectMovementRule(tile, coord.getEast(), coord.getNorthWest(), coord.getNorthEast(), board))
			list.add(coord.getNorthEast());
	
		return list;
	}

	
	private boolean respectMovementRule(Tile from, Coord leftCoord, Coord rightCoord, Coord targetCoord, Board board){
		Tile target = board.getTile(targetCoord);
		Tile left = board.getTile(leftCoord);
		Tile right = board.getTile(rightCoord);
		int floor = target.getZ() < from.getZ() ? from.getZ() : target.getZ();
		return (left == null || left.getZ() < floor || right == null || right.getZ() < floor);
	}
}
