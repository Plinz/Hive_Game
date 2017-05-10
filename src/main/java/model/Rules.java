package main.java.model;

import java.util.HashSet;
import java.util.List;
import java.util.Stack;

import main.java.utils.CoordGene;

public class Rules {

	public static boolean oneHive(Board board, Tile toMove) {
		List<Tile> list = board.getPieceNeighbors(toMove);

		if (toMove.getZ() != 0 || list.isEmpty())
			return true;

		HashSet<Tile> visited = new HashSet<Tile>();
		Stack<Tile> nextTiles = new Stack<Tile>();

		visited.add(toMove);
		nextTiles.add(list.get(0));
		Tile current;
		while (!nextTiles.isEmpty()) {
			if (!visited.contains(current = nextTiles.pop())) {
				nextTiles.addAll(board.getPieceNeighbors(current));
				visited.add(current);
			}
		}
		return board.getNbPieceOnTheBoard() == visited.size();
	}

	public static boolean freedomToMove(Board board, int floor, CoordGene<Integer> left, CoordGene<Integer> right,
			CoordGene<Integer> exception) {
		Tile leftTile = board.getTile(left);
		Tile rightTile = board.getTile(right);
		return (leftTile == null || leftTile.getPiece() == null || leftTile.getZ() < floor || rightTile == null
				|| rightTile.getPiece() == null || rightTile.getZ() < floor || left.equals(exception)
				|| right.equals(exception));
	}

	public static boolean permanentContact(Board board, int floor, CoordGene<Integer> left, CoordGene<Integer> right,
			CoordGene<Integer> exception) {
		if (floor > 0)
			return true;
		Tile leftTile = board.getTile(left);
		Tile rightTile = board.getTile(right);
		return (leftTile != null && leftTile.getPiece() != null && !left.equals(exception))
				|| (rightTile != null && rightTile.getPiece() != null && !right.equals(exception));
	}
}
