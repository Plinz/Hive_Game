package main.java.engine;

import java.util.Collection;
import java.util.List;

import main.java.model.Board;
import main.java.model.HelpMove;
import main.java.model.Piece;
import main.java.model.Player;
import main.java.model.Tile;
import main.java.utils.Consts;
import main.java.utils.CoordGene;

public class Emulator{

	Core core;
	Board board;
	Player[] players;

	public Emulator(Core core, Board board, Player[] players) {
		this.core = core;
		this.board = board;
		this.players = players;
	}

	public HelpMove getMove(String notation){
		String[] tokens = notation.split(" ");
		boolean move = false;
		Player player = players[Consts.getPlayer(tokens[0].charAt(0))];
		int pieceId = Consts.getId(tokens[0].substring(1, tokens[0].length()));
		Piece piece = player.getInventory().stream().filter(p -> p.getId() == pieceId).findFirst().orElse(null);

		Tile tile = null;
		if (piece == null) {
			move = true;
			tile = getTile(pieceId, player.getTeam());
		}

		CoordGene<Integer> target = new CoordGene<Integer>(0, 0);
		
		if (tokens.length == 2) {
			switch (tokens[1].charAt(0)) {
			case 'R':
				target = null;
				break;
			case '-':
				target = getTile(Consts.getId(tokens[1].substring(2)), Consts.getPlayer(tokens[1].charAt(1))).getCoord()
						.getWest();
				break;
			case '/':
				target = getTile(Consts.getId(tokens[1].substring(2)), Consts.getPlayer(tokens[1].charAt(1))).getCoord()
						.getSouthWest();
				break;
			case '\\':
				target = getTile(Consts.getId(tokens[1].substring(2)), Consts.getPlayer(tokens[1].charAt(1))).getCoord()
						.getNorthWest();
				break;
			default:
				switch (tokens[1].charAt(tokens[1].length() - 1)) {
				case '-':
					target = getTile(Consts.getId(tokens[1].substring(1, tokens[1].length() - 1)), Consts.getPlayer(tokens[1].charAt(0)))
							.getCoord().getEast();
					break;
				case '/':
					target = getTile(Consts.getId(tokens[1].substring(1, tokens[1].length() - 1)), Consts.getPlayer(tokens[1].charAt(0)))
							.getCoord().getNorthEast();
					break;
				case '\\':
					target = getTile(Consts.getId(tokens[1].substring(1, tokens[1].length() - 1)), Consts.getPlayer(tokens[1].charAt(0)))
							.getCoord().getSouthEast();
					break;
				default:
					target = getTile(Consts.getId(tokens[1].substring(1, tokens[1].length())), Consts.getPlayer(tokens[1].charAt(0)))
							.getCoord();
				}
			}
		}
		return new HelpMove(move, piece.getId(), tile.getCoord(), target);
	}
	
	public void play(String notation) {
		String[] tokens = notation.split(" ");
		boolean move = false;
		Player player = players[Consts.getPlayer(tokens[0].charAt(0))];
		int pieceId = Consts.getId(tokens[0].substring(1, tokens[0].length()));
		Piece piece = player.getInventory().stream().filter(p -> p.getId() == pieceId).findFirst().orElse(null);

		Tile tile = null;
		if (piece == null) {
			move = true;
			tile = getTile(pieceId, player.getTeam());
		}

		CoordGene<Integer> target = new CoordGene<Integer>(0, 0);
		
		if (tokens.length == 2) {
			switch (tokens[1].charAt(0)) {
			case 'R':
				target = null;
				break;
			case '-':
				target = getTile(Consts.getId(tokens[1].substring(2)), Consts.getPlayer(tokens[1].charAt(1))).getCoord()
						.getWest();
				break;
			case '/':
				target = getTile(Consts.getId(tokens[1].substring(2)), Consts.getPlayer(tokens[1].charAt(1))).getCoord()
						.getSouthWest();
				break;
			case '\\':
				target = getTile(Consts.getId(tokens[1].substring(2)), Consts.getPlayer(tokens[1].charAt(1))).getCoord()
						.getNorthWest();
				break;
			default:
				switch (tokens[1].charAt(tokens[1].length() - 1)) {
				case '-':
					target = getTile(Consts.getId(tokens[1].substring(1, tokens[1].length() - 1)), Consts.getPlayer(tokens[1].charAt(0)))
							.getCoord().getEast();
					break;
				case '/':
					target = getTile(Consts.getId(tokens[1].substring(1, tokens[1].length() - 1)), Consts.getPlayer(tokens[1].charAt(0)))
							.getCoord().getNorthEast();
					break;
				case '\\':
					target = getTile(Consts.getId(tokens[1].substring(1, tokens[1].length() - 1)), Consts.getPlayer(tokens[1].charAt(0)))
							.getCoord().getSouthEast();
					break;
				default:
					target = getTile(Consts.getId(tokens[1].substring(1, tokens[1].length())), Consts.getPlayer(tokens[1].charAt(0)))
							.getCoord();
				}
			}
		}
		if (target == null)
			player.addPiece(board.removePiece(tile.getCoord()));
		else
			if (move)
				board.movePiece(tile.getCoord(), target);
			else
				board.addPiece(player.removePiece(piece.getId()), target);
	}

	public void playAll(List<String> notations) {
		notations.stream().forEach(n -> play(n));
	}

	private Tile getTile(int piece, int team) {
		return board.getBoard()
				.stream().flatMap(Collection::stream).flatMap(Collection::stream).filter(t -> t != null
						&& t.getPiece() != null && t.getPiece().getTeam() == team && t.getPiece().getId() == piece)
				.findFirst().orElse(null);
	}
}
