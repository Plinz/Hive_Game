/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package main.java.view;

import java.awt.Polygon;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.List;
import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.VBox;
import javafx.scene.paint.ImagePattern;
import javafx.stage.Stage;
import main.java.model.*;
import main.java.utils.Consts;
import main.java.utils.Coord;
import main.java.utils.CoordGene;

/**
 *
 * @author gontardb
 */
public class InterfaceJavaFX extends Application {

    private Core core;
    private Label choice;
    private VBox piecesToAdd;
    private Piece pieceToChoose;
    private Coord pieceToMove;
    private Highlighter highlighted;

    @Override
    public void start(Stage primaryStage) throws Exception {

        /*Initialisation du core et tests basiques*/
        core = new Core(2);

        BorderPane gameBorderPane = new BorderPane();

        Canvas gameCanvas = new Canvas(800, 800);
        ScrollPane scrollPane = new ScrollPane(gameCanvas);
        highlighted = new Highlighter();

        gameBorderPane.setLeft(scrollPane);
        Scene gameScene = new Scene(gameBorderPane, 800, 800);

        primaryStage.setScene(gameScene);
        primaryStage.setTitle("Hive_Test");
        primaryStage.show();

        choice = new Label("Le joueur " + core.getCurrentState().getCurrentPlayer() + " doit choisir sa pièce !");
        choice.setWrapText(true);

        piecesToAdd = new VBox();
        piecesToAdd.setAlignment(Pos.TOP_CENTER);
        piecesToAdd.getChildren().add(choice);
        piecesToAdd.getChildren().addAll(initButtonByInventory());

        gameBorderPane.setRight(piecesToAdd);
        /**
         * ****************************
         */
        /* Bind du canvas et du scrollPane*/
        scrollPane.vvalueProperty().bind(primaryStage.widthProperty().multiply(0.80));
        scrollPane.hvalueProperty().bind(primaryStage.heightProperty());

        gameCanvas.widthProperty().bind(scrollPane.vvalueProperty());
        gameCanvas.heightProperty().bind(scrollPane.hvalueProperty());
        /**
         * ***************
         */

        /* Clic sur le canvas principal */
        gameCanvas.setOnMousePressed(new EventHandler<MouseEvent>() {
            TraducteurBoard t = new TraducteurBoard();

            @Override
            public void handle(MouseEvent m) {
                //Point2D mousePoint = new Point2D.Double(m.getX(), m.getY());

                CoordGene<Double> coordPix = new CoordGene<>(m.getX(), m.getY());
                CoordGene<Double> coordAx = t.pixelToAxial(coordPix);
                int i = coordAx.getX().intValue();
                int j = coordAx.getY().intValue();

                if (m.getButton() == MouseButton.PRIMARY) {
                    if (core.getCurrentState().getBoard().getTile(new Coord(i, j)) != null) {
                        Coord coord = new Coord(i, j);
                        if (pieceToMove != null && core.getCurrentState().getBoard().getTile(pieceToMove).getPiece().getPossibleMovement(core.getCurrentState().getBoard().getTile(pieceToMove), core.getCurrentState().getBoard()).contains(coord)/*&& core.getDestination().contains(coord)*/) {
                            //System.out.println("Déplacement de la piece choisie");
                            core.movePiece(pieceToMove, coord);
                            pieceToMove = null;
                            pieceToChoose = null;
                            initButtonByInventory();
                            choice.setText("Le joueur " + core.getCurrentState().getCurrentPlayer() + " doit choisir sa pièce !");
                            piecesToAdd.getChildren().remove(0, piecesToAdd.getChildren().size());
                            piecesToAdd.getChildren().add(choice);
                            piecesToAdd.getChildren().addAll(initButtonByInventory());
                            highlighted.setListTohighlight(null);
                        } else if (core.getCurrentState().getBoard().getTile(coord).getPiece() != null) {
                            if (core.getCurrentState().getBoard().getTile(coord).getPiece().getTeam() == core.getCurrentState().getCurrentPlayer()) {
                                pieceToMove = coord;
                                highlighted.setListTohighlight(core.getPossibleMovement(coord));
                            }
                        } else {
                            System.out.println("Cliquable mais pas de pieces  " + i + " " + j + " !  ");
                            if (pieceToChoose != null) {
                                core.addPiece(pieceToChoose, coord);
                                pieceToMove = null;
                                pieceToChoose = null;
                                initButtonByInventory();
                                choice.setText("Le joueur " + core.getCurrentState().getCurrentPlayer() + " doit choisir sa pièce !");
                                piecesToAdd.getChildren().remove(0, piecesToAdd.getChildren().size());
                                piecesToAdd.getChildren().add(choice);
                                piecesToAdd.getChildren().addAll(initButtonByInventory());
                                highlighted.setListTohighlight(null);
                            } else {
                                pieceToMove = null;
                                pieceToChoose = null;
                                highlighted.setListTohighlight(null);
                            }
                        }
                    } else {
                        System.out.println("Pas cliquable  " + i + " " + j + " !  ");
                    }
                }
            }

        });
        /**
         * *****************************
         */

        RefreshJavaFX r = new RefreshJavaFX(core, gameCanvas, highlighted);
        r.start();

    }
    public List<Button> initButtonByInventory() {

        System.out.println("Lol + " + core.getCurrentState().getCurrentPlayer());
        List<Piece> inventory = core.getCurrentState().getPlayers()[core.getCurrentState().getCurrentPlayer()].getInventory();
        List<Button> list = new ArrayList<>();

        for (int i = 0; i < inventory.size(); i++) {
            String name = inventory.get(i).getName();
            int team = inventory.get(i).getTeam();
            Button b = new Button();
            b.setMinSize(30, 30);
            b.setBackground(new Background(new BackgroundFill(new ImagePattern(new Image(name + team + ".png")), CornerRadii.EMPTY, Insets.EMPTY)));

            b.setOnMousePressed(new EventHandler<MouseEvent>() {

                @Override
                public void handle(MouseEvent event) {
                    Piece piece = PieceFactory.create(name + team);
                    pieceToChoose = piece;
                    choice.setText(piece.getName() + piece.getTeam());
                    highlighted.setListTohighlight(core.getPossibleAdd());
                }
            });
            list.add(b);
        }
        return list;
    }

    public Polygon addPoly(double x, double y, double i, double j, double size) {
        double[] xd = new double[6];
        double[] yd = new double[6];
        double originX, originY;

        if (j % 2 == 0) {
            originX = x + (2 * size * i) + ((2 * size) * Math.floor(j / 2));
        } else {
            originX = x + (2 * size * i) + size + ((2 * size) * Math.floor(j / 2));

        }
        originY = y + (size * j) + (j * (size / 2));

        int[] rx = new int[6];
        int[] ry = new int[6];
        xd[0] = originX;
        yd[0] = originY;
        xd[1] = originX + size;
        yd[1] = originY + size / 2;
        xd[2] = originX + size;
        yd[2] = (originY + size) + size / 2;
        xd[3] = originX;
        yd[3] = originY + 2 * size;
        xd[4] = originX - size;
        yd[4] = (originY + size) + size / 2;
        xd[5] = originX - size;
        yd[5] = originY + size / 2;
        for (int a = 0; a < 6; a++) {
            rx[a] = (int) xd[a];
            ry[a] = (int) yd[a];
        }

        return new Polygon(rx, ry, 6);
    }

    public static void creer(String[] args) {
        launch(args);
    }
}
