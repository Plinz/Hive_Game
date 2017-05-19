/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package main.java.view;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import main.java.engine.OptionManager;
import main.java.engine.Visitor;
import main.java.model.Board;
import main.java.model.HelpMove;
import main.java.model.Piece;
import main.java.model.Tile;
import main.java.utils.Consts;
import main.java.utils.CoordGene;

public class BoardDrawer extends Visitor {
	Canvas can;
	GraphicsContext gc;
	TraducteurBoard traducteur;
	Hexagon hex;

	public BoardDrawer(Canvas c) {
		this.can = c;
		this.gc = can.getGraphicsContext2D();
		traducteur = new TraducteurBoard();
	}

	public BoardDrawer(Canvas c, TraducteurBoard t) {
		this.can = c;
		this.gc = can.getGraphicsContext2D();
		hex = new Hexagon();
		traducteur = t;
		traducteur.setMoveOrigin(new CoordGene<Double>(can.getWidth() / 2, (can.getHeight() / 2) - Consts.SIDE_SIZE));
	}

	public boolean visit(Board b) {
		gc.clearRect(0, 0, can.getWidth(), can.getHeight());
		String name = getClass().getClassLoader().getResource("main/resources/img/misc/metal.jpg").toString();
		gc.setFill(new ImagePattern(new Image(name)));
		gc.strokeRect(0, 0, can.getWidth(), can.getHeight());
		gc.fillRect(0, 0, can.getWidth(), can.getHeight());
		return false;
	}

	public boolean visit(Tile t) {

		CoordGene<Double> coord = new CoordGene<Double>((double) t.getX(), (double) t.getY());
		CoordGene<Double> coordPix = traducteur.axialToPixel(coord);

		hex.setxPixel(coordPix.getX() + traducteur.getMoveOrigin().getX());
		hex.setyPixel(coordPix.getY() + traducteur.getMoveOrigin().getY());
		hex.calculHex();
		Piece piece = t.getPiece();
		if (piece != null) {
			String name;
			if (t.getZ() == 0) {
				name = getClass().getClassLoader()
						.getResource("main/resources/img/tile/" + piece.getName() + piece.getTeam() + ".png")
						.toString();
			} else {
				name = getClass().getClassLoader()
						.getResource("main/resources/img/tile/" + piece.getName() + piece.getTeam() + t.getZ() + ".png")
						.toString();
			}
			gc.setFill(new ImagePattern(new Image(name)));
			gc.setStroke(Color.BLACK);
			gc.setLineWidth(1);
			gc.strokePolygon(hex.getListXCoord(), hex.getListYCoord(), 6);
			gc.fillPolygon(hex.getListXCoord(), hex.getListYCoord(), 6);

		} else {
			if (OptionManager.isGridEnable()) {
				gc.setStroke(Color.BLACK);
				gc.strokePolygon(hex.getListXCoord(), hex.getListYCoord(), 6);
			}
		}

		return false;
	}

	@Override
	public boolean visit(CoordGene<Integer> c) {
		CoordGene<Double> coord = new CoordGene<Double>((double) c.getX(), (double) c.getY());
		CoordGene<Double> coordPix = traducteur.axialToPixel(coord);

		hex.setxPixel(coordPix.getX() + traducteur.getMoveOrigin().getX());
		hex.setyPixel(coordPix.getY() + traducteur.getMoveOrigin().getY());
		hex.calculHex();

		gc.setStroke(Color.BLACK);
		gc.setGlobalAlpha(0.5);
		gc.setFill(Color.BLUE);
		gc.fillPolygon(hex.getListXCoord(), hex.getListYCoord(), 6);
		gc.setGlobalAlpha(1);
		gc.strokePolygon(hex.getListXCoord(), hex.getListYCoord(), 6);
		return false;
	}

	@Override
	protected boolean visit(Piece p) {
		throw new UnsupportedOperationException("Not supported yet."); // To
																		// change
																		// body
																		// of
																		// generated
																		// methods,
																		// choose
																		// Tools
																		// |
																		// Templates.
	}

	@Override
	public boolean visit(HelpMove h) {
		if (h.isAdd()) {
			drawHelp(h.getTarget());
		} else {
			drawHelp(h.getFrom());
			drawHelp(h.getTarget());
		}
		return false;
	}

	public void drawHelp(CoordGene<Integer> c) {
		CoordGene<Double> coord = new CoordGene<Double>((double) c.getX(), (double) c.getY());
		CoordGene<Double> coordPix = traducteur.axialToPixel(coord);

		hex.setxPixel(coordPix.getX() + traducteur.getMoveOrigin().getX());
		hex.setyPixel(coordPix.getY() + traducteur.getMoveOrigin().getY());
		hex.calculHex();

		gc.setStroke(Color.RED);
		gc.setLineWidth(3);
//		gc.setGlobalAlpha(0.5);
//		gc.setFill(Color.RED);
//		gc.fillPolygon(hex.getListXCoord(), hex.getListYCoord(), 6);
//		gc.setGlobalAlpha(1);
		gc.strokePolygon(hex.getListXCoord(), hex.getListYCoord(), 6);

	}
}