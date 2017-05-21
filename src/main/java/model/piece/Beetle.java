package main.java.model.piece;

import java.util.ArrayList;
import java.util.List;

import main.java.model.Board;
import main.java.model.Piece;
import main.java.model.Tile;
import main.java.utils.Consts;
import main.java.utils.CoordGene;

public class Beetle extends Piece {

	public Beetle(int id, int team) {
		super(Consts.BEETLE_NAME, id, team, "Le scarabée se déplace d'un espace à la fois, et a la capacité\nde monter sur les autres pièces pour les bloquer");
                if (team == 0)
                    drawingId = 4;
                else
                    drawingId = 5;
        }

	@Override
	public List<CoordGene<Integer>> getPossibleMovement(Tile tile, Board board) {
		if (this.possibleMovement != null)
			return this.possibleMovement;
		List<CoordGene<Integer>> list = new ArrayList<>();
		if (!tile.isBlocked() && board.oneHive(tile)) {
			List<CoordGene<Integer>> neighbors = tile.getCoord().getNeighbors();
			for (int i = 0; i < neighbors.size(); i++) {
				Tile target = board.getTile(neighbors.get(i));
				CoordGene<Integer> left = neighbors.get(Math.floorMod(i - 1, 6));
				CoordGene<Integer> right = neighbors.get(Math.floorMod(i + 1, 6));
				int floor;
				if (target.getZ() == 0 && tile.getZ() == 0)
					floor = (target.getPiece() == null) ? 0 : 1;
				else
					floor = Math.max(target.getZ(), tile.getZ());
				if (board.freedomToMove(floor, left, right, tile.getCoord())
						&& board.permanentContact(floor, left, right, tile.getCoord()))
					list.add(target.getCoord());
			}
		}
		this.possibleMovement = list;
		return list;
	}
}
