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
			if (board.getEast(coord) == null && board.getNeighbors(new Coord(tile.getX()+1, tile.getY())).size()!=1)
				list.add(new Coord(tile.getX()+1, tile.getY()));
			if (board.getSouthEast(coord) == null && board.getNeighbors(new Coord(tile.getX(), tile.getY()+1)).size()!=1)
				list.add(new Coord(tile.getX(), tile.getY()+1));
			if (board.getSouthWest(coord) == null && board.getNeighbors(new Coord(tile.getX()-1, tile.getY()+1)).size()!=1)
				list.add(new Coord(tile.getX()-1, tile.getY()+1));
			if (board.getWest(coord) == null && board.getNeighbors(new Coord(tile.getX()-1, tile.getY())).size()!=1)
				list.add(new Coord(tile.getX()-1, tile.getY()));
			if (board.getNorthWest(coord) == null && board.getNeighbors(new Coord(tile.getX(), tile.getY()-1)).size()!=1)
				list.add(new Coord(tile.getX(), tile.getY()-1));
			if (board.getNorthEast(coord) == null && board.getNeighbors(new Coord(tile.getX()+1, tile.getY()-1)).size()!=1)
				list.add(new Coord(tile.getX()+1, tile.getY()-1));			
		}
		return list;
	}

}
