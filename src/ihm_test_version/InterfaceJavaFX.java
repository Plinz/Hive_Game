/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ihm_test_version;

import java.util.ArrayList;
import java.util.List;

import javafx.application.Application;
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
import main.java.controller.ControllerButton;
import main.java.model.Core;
import main.java.model.Piece;
import main.java.utils.Consts;
import main.java.utils.CoordGene;
import main.java.view.Highlighter;
import main.java.view.RefreshJavaFX;
import main.java.view.TraducteurBoard;

/**
 *
 * @author gontardb
 */
public class InterfaceJavaFX extends Application {

    private Core core;
    private Label choice;
    private VBox piecesToAdd;
    private int pieceToChoose;
    private CoordGene<Integer> pieceToMove;
    private Highlighter highlighted;

    @Override
    public void start(Stage primaryStage) throws Exception {

        /*Initialisation du core et tests basiques*/
        core = new Core(Consts.PVP, Consts.EASY);
        pieceToChoose = new Integer(-1);

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
        initPiecesToAdd();
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
                    if (core.getCurrentState().getBoard().getTile(new CoordGene<Integer>(i, j)) != null) {
                        CoordGene<Integer> coord = new CoordGene<>(i, j);
                        if (pieceToMove != null &&  core.getCurrentState().getBoard().getTile(pieceToMove).getPiece().getPossibleMovement(core.getCurrentState().getBoard().getTile(pieceToMove), core.getCurrentState().getBoard()).contains(coord)/*&& core.getDestination().contains(coord)*/) {
                            //System.out.println("Déplacement de la piece choisie");
                            core.movePiece(pieceToMove, coord);
                            pieceToMove = null;
                            pieceToChoose = -1;
                            initButtonByInventory();
                            choice.setText("Le joueur " + core.getCurrentState().getCurrentPlayer() + " doit choisir sa pièce !");
                            initPiecesToAdd();
                            highlighted.setListTohighlight(null);
                            
                        } else if (core.getCurrentState().getBoard().getTile(coord).getPiece() != null && core.checkQueenRule()/* A commenter si nécessaire */) {
                            if (core.getCurrentState().getBoard().getTile(coord).getPiece().getTeam() == core.getCurrentState().getCurrentPlayer()) {
                                pieceToMove = coord;
                                highlighted.setListTohighlight(core.getPossibleMovement(coord));
                                pieceToChoose = -1;
                            }
                        } else {
                            System.out.println("Cliquable mais pas de pieces  " + i + " " + j + " !  ");
                            if (pieceToChoose != -1 && core.getPossibleAdd().contains(coord)) {
                                core.addPiece(pieceToChoose, coord);
                                pieceToMove = null;
                                pieceToChoose = -1;
                                initButtonByInventory();
                                choice.setText("Le joueur " + core.getCurrentState().getCurrentPlayer() + " doit choisir sa pièce !");
                                initPiecesToAdd();
                                highlighted.setListTohighlight(null);
                            } else {
                                pieceToMove = null;
                                pieceToChoose = -1;
                                highlighted.setListTohighlight(null);
                            }
                        }
                    } else {
                        System.out.println("Pas cliquable  " + i + " " + j + " !  ");
                        pieceToMove = null;
                        pieceToChoose = -1;
                        highlighted.setListTohighlight(null);
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

        List<Piece> inventory = core.getCurrentState().getPlayers()[core.getCurrentState().getCurrentPlayer()].getInventory();
        List<Button> list = new ArrayList<>();

        for (int i = 0; i < inventory.size(); i++) {
            String name = inventory.get(i).getName();
            int team = inventory.get(i).getTeam();
            Button b = new Button();
            b.setMinSize(30, 30);
            b.setBackground(new Background(new BackgroundFill(new ImagePattern(new Image(name + team + ".png")), CornerRadii.EMPTY, Insets.EMPTY)));

            b.setOnMousePressed(new ControllerButton(this,highlighted,core, i));
            list.add(b);
        }
        return list;
    }

    public Core getCore() {
        return core;
    }

    public void setCore(Core core) {
        this.core = core;
    }

    public Label getChoice() {
        return choice;
    }

    public void setChoice(Label choice) {
        this.choice = choice;
    }

    public VBox getPiecesToAdd() {
        return piecesToAdd;
    }

    public void setPiecesToAdd(VBox piecesToAdd) {
        this.piecesToAdd = piecesToAdd;
    }

    public CoordGene<Integer> getPieceToMove() {
        return pieceToMove;
    }

    public void setPieceToMove(CoordGene<Integer> pieceToMove) {
        this.pieceToMove = pieceToMove;
    }

    public Highlighter getHighlighted() {
        return highlighted;
    }

    public void setHighlighted(Highlighter highlighted) {
        this.highlighted = highlighted;
    }
    
    public void initPiecesToAdd(){
        piecesToAdd.getChildren().remove(0, piecesToAdd.getChildren().size());
        piecesToAdd.getChildren().add(choice);
        piecesToAdd.getChildren().addAll(initButtonByInventory());
    }

    public static void creer(String[] args) {
        launch(args);
    }
    
    
    public void setPieceToChoose(int pieceToChoose) {
        this.pieceToChoose = pieceToChoose;
    }
}
