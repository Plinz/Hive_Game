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
		
		if (tile.getZ() != 0){
			list.add(new Coord(tile.getX()+1, tile.getY()));
			list.add(new Coord(tile.getX(), tile.getY()+1));
			list.add(new Coord(tile.getX()-1, tile.getY()+1));
			list.add(new Coord(tile.getX()-1, tile.getY()));
			list.add(new Coord(tile.getX(), tile.getY()-1));
			list.add(new Coord(tile.getX()+1, tile.getY()-1));
		}
		else {
			Coord coord = tile.getCoord();
//			if (board.getNeighbors(new Coord(tile.getX()+1, tile.getY())).size()!=1 && (board.getSouthEast(coord.getSouthEast()) == null || board.getNorthEast(coord) == null))
//				list.add(new Coord(tile.getX()+1, tile.getY()));
//			if (board.getNeighbors(new Coord(tile.getX(), tile.getY()+1)).size()!=1 && (board.getSouthWest(coord) == null || board.getEast(coord) == null))
//				list.add(new Coord(tile.getX(), tile.getY()+1));
//			if (board.getNeighbors(new Coord(tile.getX()-1, tile.getY()+1)).size()!=1 && (board.getWest(coord) == null || board.getSouthEast(coord) == null))
//				list.add(new Coord(tile.getX()-1, tile.getY()+1));
//			if (board.getNeighbors(new Coord(tile.getX()-1, tile.getY())).size()!=1 && (board.getNorthWest(coord) == null || board.getSouthWest(coord) == null))
//				list.add(new Coord(tile.getX()-1, tile.getY()));
//			if (board.getNeighbors(new Coord(tile.getX(), tile.getY()-1)).size()!=1 && (board.getNorthEast(coord) == null || board.getWest(coord) == null))
//				list.add(new Coord(tile.getX(), tile.getY()-1));
//			if (board.getNeighbors(new Coord(tile.getX()+1, tile.getY()-1)).size()!=1 && (board.getEast(coord) == null || board.getNorthWest(coord) == null))
//				list.add(new Coord(tile.getX()+1, tile.getY()-1));		
		}
		return list;
	}

}
