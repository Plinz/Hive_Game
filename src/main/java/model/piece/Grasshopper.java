package main.java.model.piece;

import java.util.ArrayList;
import java.util.List;

import main.java.model.Board;
import main.java.model.Piece;
import main.java.model.Tile;
import main.java.utils.Consts;
import main.java.utils.CoordGene;

public class Grasshopper extends Piece {

	public Grasshopper(int id, int team) {
		super(Consts.GRASSHOPPER_NAME, id, team, "La sauterelle se déplace en sautant en ligne droite par-dessus\nune ou plusieurs autres pièces, jusqu'au premier espace libre");
        }

	@Override
	public List<CoordGene<Integer>> getPossibleMovement(Tile tile, Board board) {
		if (this.possibleMovement != null)
			return this.possibleMovement;
		
		ArrayList<CoordGene<Integer>> list = new ArrayList<>();
		if (!tile.isBlocked() && board.oneHive(tile)) {
			CoordGene<Integer> coord = tile.getCoord();
			
			Tile tmp = board.getTile(coord.getEast());
			if (tmp.getPiece() != null){
				while ((tmp = board.getTile(tmp.getCoord().getEast())).getPiece() != null);
				list.add(tmp.getCoord());
			}
			
			tmp = board.getTile(coord.getSouthEast());
			if (tmp.getPiece() != null){
				while ((tmp = board.getTile(tmp.getCoord().getSouthEast())).getPiece() != null);
				list.add(tmp.getCoord());
			}
			
			tmp = board.getTile(coord.getSouthWest());
			if (tmp.getPiece() != null){
				while ((tmp = board.getTile(tmp.getCoord().getSouthWest())).getPiece() != null);
				list.add(tmp.getCoord());
			}
			
			tmp = board.getTile(coord.getWest());
			if (tmp.getPiece() != null){
				while ((tmp = board.getTile(tmp.getCoord().getWest())).getPiece() != null);
				list.add(tmp.getCoord());
			}
			
			tmp = board.getTile(coord.getNorthWest());
			if (tmp.getPiece() != null){
				while ((tmp = board.getTile(tmp.getCoord().getNorthWest())).getPiece() != null);
				list.add(tmp.getCoord());
			}
			
			tmp = board.getTile(coord.getNorthEast());
			if (tmp.getPiece() != null){
				while ((tmp = board.getTile(tmp.getCoord().getNorthEast())).getPiece() != null);
				list.add(tmp.getCoord());
			}
		}
		this.possibleMovement = list;
		return list;
	}

}
