package main.java.model.piece;

import java.util.ArrayList;
import java.util.List;

import main.java.model.Board;
import main.java.model.Piece;
import main.java.model.Tile;
import main.java.utils.Consts;
import main.java.utils.Coord;

public class Grasshopper extends Piece {

	public Grasshopper(int team) {
		super(Consts.GRASSHOPPER_NAME, team, "Grasshopper description");

	}

	@Override
	public List<Coord> getPossibleMovement(Tile tile, Board board) {
		if (this.possibleMovement != null)
			return this.possibleMovement;
		
		ArrayList<Coord> list = new ArrayList<Coord>();
		if (!tile.isBlocked()) {
			Coord coord = tile.getCoord();
			
			Tile tmp = board.getTile(coord.getEast());
			if (tmp.getPiece() != null){
				while ((tmp = board.getTile(tmp.getCoord().getEast())).getPiece() != null)
					coord = tmp.getCoord();
				list.add(tmp.getCoord());
			}
			
			tmp = board.getTile(coord.getSouthEast());
			if (tmp.getPiece() != null){
				while ((tmp = board.getTile(tmp.getCoord().getSouthEast())).getPiece() != null)
					coord = tmp.getCoord();
				list.add(tmp.getCoord());
			}
			
			tmp = board.getTile(coord.getSouthWest());
			if (tmp.getPiece() != null){
				while ((tmp = board.getTile(tmp.getCoord().getSouthWest())).getPiece() != null)
					coord = tmp.getCoord();
				list.add(tmp.getCoord());
			}
			
			tmp = board.getTile(coord.getWest());
			if (tmp.getPiece() != null){
				while ((tmp = board.getTile(tmp.getCoord().getWest())).getPiece() != null)
					coord = tmp.getCoord();
				list.add(tmp.getCoord());
			}
			
			tmp = board.getTile(coord.getNorthWest());
			if (tmp.getPiece() != null){
				while ((tmp = board.getTile(tmp.getCoord().getNorthWest())).getPiece() != null)
					coord = tmp.getCoord();
				list.add(tmp.getCoord());
			}
			
			tmp = board.getTile(coord.getNorthEast());
			if (tmp.getPiece() != null){
				while ((tmp = board.getTile(tmp.getCoord().getNorthEast())).getPiece() != null)
					coord = tmp.getCoord();
				list.add(tmp.getCoord());
			}
		}
		this.possibleMovement = list;
		return list;
	}

}
