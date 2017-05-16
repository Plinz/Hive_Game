package main.java.model;

import java.util.Collection;
import java.util.List;

import main.java.utils.Consts;
import main.java.utils.CoordGene;

public class Notation {

	public static String getMoveNotation(Board board, Piece piece, CoordGene<Integer> coord) {
		String notation = (piece.getTeam() == 0 ? "w" : "b") + Consts.getNotation(piece.getId());
		List<Tile> neighbors = board.getPieceNeighbors(coord);
		Tile t = board.getTile(coord);
		if (t != null && t.getPiece() != null) {
			char team = t.getPiece().getTeam() == 0 ? 'w' : 'b';
			notation += " " + team + Consts.getNotation(t.getPiece().getId());
		} else if (!neighbors.isEmpty()) {
			while((t = neighbors.get(0)).isBlocked()) System.out.println(t);;
			char team = t.getPiece().getTeam() == 0 ? 'w' : 'b';
			CoordGene<Integer> offset = new CoordGene<Integer>(coord.getX() - t.getX(), coord.getY() - t.getY());
			if (offset.getX() == 1 && offset.getY() == 0)
				notation += " " + team + Consts.getNotation(t.getPiece().getId()) + "-";
			if (offset.getX() == 0 && offset.getY() == 1)
				notation += " " + team + Consts.getNotation(t.getPiece().getId()) + "\\";
			if (offset.getX() == -1 && offset.getY() == 1)
				notation += " /" + team + Consts.getNotation(t.getPiece().getId());
			if (offset.getX() == -1 && offset.getY() == 0)
				notation += " -" + team + Consts.getNotation(t.getPiece().getId());
			if (offset.getX() == 0 && offset.getY() == -1)
				notation += " \\" + team + Consts.getNotation(t.getPiece().getId());
			if (offset.getX() == 1 && offset.getY() == -1)
				notation += " " + team + Consts.getNotation(t.getPiece().getId()) + "/";
		}
		return notation;
	}

	public static String getInverseMoveNotation(Board board, Piece piece) {
		String unplay = Consts.getColor(piece.getTeam()) + "";

		Tile tile = board.getBoard().stream().flatMap(Collection::stream)
				.flatMap(Collection::stream).filter(t -> t != null && t.getPiece() != null
						&& t.getPiece().getTeam() == piece.getTeam() && t.getPiece().getId() == piece.getId())
				.findFirst().orElse(null);
		if (tile != null)
			unplay += Consts.getNotation(tile.getPiece().getId()) + " " + getNeighborsNotation(board, tile);
		else
			unplay += Consts.getNotation(piece.getId()) + " REMOVE";
		return unplay;
	}

	public static String getHumanDescription(String notation, boolean add) {
		String[] tokens = notation.split(" ");
		String description = (add ? "ajoute" : "déplace") + " la pièce ";
		description += Consts.getName(Consts.getId(tokens[0].substring(1)))
				+ (tokens[0].charAt(0) == 'w' ? " blanche" : " noire");
		if (tokens.length == 2) {
			switch (tokens[1].charAt(0)) {
			case '-':
				description += " à gauche de la pièce " + Consts.getName(Consts.getId(tokens[1].substring(2)))
						+ (tokens[1].charAt(1) == 'w' ? " blanche" : " noire");
				break;
			case '/':
				description += " en bas à gauche de la pièce " + Consts.getName(Consts.getId(tokens[1].substring(2)))
						+ (tokens[1].charAt(1) == 'w' ? " blanche" : " noire");
				break;
			case '\\':
				description += " en haut à gauche de la pièce " + Consts.getName(Consts.getId(tokens[1].substring(2)))
						+ (tokens[1].charAt(1) == 'w' ? " blanche" : " noire");
				break;
			default:
				switch (tokens[1].charAt(tokens[1].length() - 1)) {
				case '-':
					description += " à droite de la pièce "
							+ Consts.getName(Consts.getId(tokens[1].substring(1, tokens[1].length() - 1)))
							+ (tokens[1].charAt(0) == 'w' ? " blanche" : " noire");
					break;
				case '/':
					description += " en haut à droite de la pièce "
							+ Consts.getName(Consts.getId(tokens[1].substring(1, tokens[1].length() - 1)))
							+ (tokens[1].charAt(0) == 'w' ? " blanche" : " noire");
					break;
				case '\\':
					description += " en bas à droite de la pièce "
							+ Consts.getName(Consts.getId(tokens[1].substring(1, tokens[1].length() - 1)))
							+ (tokens[1].charAt(0) == 'w' ? " blanche" : " noire");
				default:
					description += " sur la pièce " + Consts.getName(Consts.getId(tokens[1].substring(1)))
							+ (tokens[1].charAt(0) == 'w' ? " blanche" : " noire");
				}
			}
		}
		return description;
	}

	private static String getNeighborsNotation(Board board, Tile tile) {
		String notation = null;

		if (tile.getZ() > 0) {
			Piece tmp = board.getAboveAndBelow(tile).get(0).getPiece();
			notation = Consts.getColor(tmp.getTeam()) + Consts.getNotation(tmp.getId());
		} else {
			CoordGene<Integer> from = tile.getCoord();
			Tile target;
			if ((target = board.getTile(from.getEast())) != null && target.getPiece() != null)
				notation = "-" + Consts.getColor(target.getPiece().getTeam())
						+ Consts.getNotation(target.getPiece().getId());
			else if ((target = board.getTile(from.getSouthEast())) != null && target.getPiece() != null)
				notation = "\\" + Consts.getColor(target.getPiece().getTeam())
						+ Consts.getNotation(target.getPiece().getId());
			else if ((target = board.getTile(from.getSouthWest())) != null && target.getPiece() != null)
				notation = Consts.getColor(target.getPiece().getTeam()) + Consts.getNotation(target.getPiece().getId())
						+ "/";
			else if ((target = board.getTile(from.getWest())) != null && target.getPiece() != null)
				notation = Consts.getColor(target.getPiece().getTeam()) + Consts.getNotation(target.getPiece().getId())
						+ "-";
			else if ((target = board.getTile(from.getNorthWest())) != null && target.getPiece() != null)
				notation = Consts.getColor(target.getPiece().getTeam()) + Consts.getNotation(target.getPiece().getId())
						+ "\\";
			else if ((target = board.getTile(from.getNorthEast())) != null && target.getPiece() != null)
				notation = "/" + Consts.getColor(target.getPiece().getTeam())
						+ Consts.getNotation(target.getPiece().getId());
		}
		return notation;
	}

}
