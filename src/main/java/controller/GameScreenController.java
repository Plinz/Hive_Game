package main.java.controller;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import javax.imageio.ImageIO;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.VPos;
import javafx.scene.Cursor;
import javafx.scene.ImageCursor;
import javafx.scene.Node;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.WritableImage;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderStroke;
import javafx.scene.layout.BorderStrokeStyle;
import javafx.scene.layout.BorderWidths;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.stage.Popup;
import main.java.engine.Core;
import main.java.implement.Main;
import main.java.model.Box;
import main.java.model.Column;
import main.java.model.HelpMove;
import main.java.model.Piece;
import main.java.model.Tile;
import main.java.utils.Consts;
import main.java.utils.CoordGene;
import main.java.view.AnimationTile;
import main.java.view.Hexagon;
import main.java.view.Highlighter;
import main.java.view.RefreshJavaFX;
import main.java.view.TraducteurBoard;

public class GameScreenController implements Initializable {
    @FXML private Canvas gameCanvas;
    @FXML private Pane panCanvas;
    @FXML private GridPane inventoryPlayer1;
    @FXML private GridPane inventoryPlayer2;
    @FXML private Text namePlayer1;
    @FXML private Text namePlayer2;
    @FXML private Button undo;
    @FXML private Button redo;
    @FXML private Button helpButton;
    @FXML private MenuItem saveMenuItem;
    @FXML private MenuItem lanchNewGame;
    @FXML private MenuItem loadGame;
    @FXML private TextField inputChat;
    @FXML private GridPane textChat;
    @FXML private ScrollPane scrollChat; 
    
    private Main main;
    private Core core;
    
    //The piece the player clicked on his inventory
    private int pieceChosenInInventory;
    private CoordGene<Double> lastCoord;
    private CoordGene<Integer> pieceToMove;
    private List<CoordGene<Integer>> possibleMovement;
    private Highlighter highlighted;
    private TraducteurBoard traductor;
    private BooleanProperty animationPlaying;
    private ToggleGroup inventoryGroup;
    private RefreshJavaFX refreshor;
    private AnimationTile animation;
    private Popup popup = new Popup();
    private int nbMessage, nbChatRow;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
    }

    public void initGame(Main mainApp, Core c) {
        setMainApp(mainApp);
        setCore(c);
        pieceChosenInInventory = -1;
        highlighted = new Highlighter();
        traductor = new TraducteurBoard();
        lastCoord = new CoordGene<Double>(0.0,0.0);
        animationPlaying = new SimpleBooleanProperty();
        animationPlaying.setValue(false);
        nbMessage = 0;
        nbChatRow = 2;
        inventoryGroup = new ToggleGroup();
        if (!core.hasPreviousState()) {
            undo.setDisable(true);
        }
        if (!core.hasNextState()) {
            redo.setDisable(true);
        }
        initButtonByInventory();
        animation = new AnimationTile(panCanvas,traductor);
        refreshor = new RefreshJavaFX(core, gameCanvas, highlighted, traductor, this);
        initGameCanvas();
        core.setGameScreen(this);
        
        if (core.getMode() == Consts.AIVP && core.getTurn() == 0){
            core.playAI();
        }

        if(core.getMode() != Consts.PVEX && core.getMode() != Consts.EXVP){
            inputChat.setVisible(false);
            textChat.setVisible(false);
            scrollChat.setVisible(false);
        }else{
            hideButtonsForNetwork();
        }
        
        refreshor.start();
    }

    public void initGameCanvas() {

        gameCanvas.setOnMousePressed(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent m) {
                CoordGene<Double> coordAx = traductor.pixelToAxial(new CoordGene<Double>(m.getX(), m.getY()));
                CoordGene<Integer> coord = new CoordGene<Integer>(coordAx.getX().intValue(), coordAx.getY().intValue());
                CoordGene<Double> origin = traductor.getMoveOrigin();
                lastCoord = new CoordGene<Double>(m.getX() - origin.getX(), m.getY() - origin.getY());
                
                if (core.getState() == Consts.WAIT_FOR_INPUT) {
                    if (m.getButton() == MouseButton.PRIMARY) {
                        if (core.isTile(coord)) {
                            if (pieceToMove != null && possibleMovement.contains(coord)) {
                                startMovingAnimation(pieceToMove, coord);
                                undo.setDisable(false);
                                redo.setDisable(true);
                            } else if (core.isPieceOfCurrentPlayer(coord)) {
                                pieceToMove = coord;
                                possibleMovement = core.getPossibleMovement(coord);
                                highlighted.setListTohighlight(possibleMovement);
                                pieceChosenInInventory = -1;
                                initButtonByInventory();
                            } else {
                                if (pieceChosenInInventory != -1 && core.getPossibleAdd(core.getCurrentPlayer()).contains(coord)) {
                                    if(core.canAddPiece(pieceChosenInInventory)){
                                        startPlacingAnimation(pieceChosenInInventory,coord);
                                        undo.setDisable(false);
                                        redo.setDisable(true);
                                    }
                                    else{
                                        queenWarning();
                                    }
                                } else {
                                    resetPiece();
                                    initButtonByInventory();
                                    highlighted.setListTohighlight(possibleMovement = null);
                                    highlighted.setHelp(null);
                                }
                            }
                        } else {
                            resetPiece();
                            highlighted.setListTohighlight(null);
                            highlighted.setHelp(null);
                        }
                    }
                }
                clearHelp();
            }
        });

        gameCanvas.setOnMouseDragged(new EventHandler<MouseEvent>() {

            public void handle(MouseEvent m) {
                if (core.getState() != Consts.ANIMATING) {
                    traductor.setMoveOrigin(new CoordGene<Double>(m.getX() - lastCoord.getX(), m.getY() - lastCoord.getY()));
                }
            }
        });

        gameCanvas.setOnMouseMoved(new EventHandler<MouseEvent>() {

            public void handle(MouseEvent m) {

                CoordGene<Double> coordAx = traductor.pixelToAxial(new CoordGene<Double>(m.getX(), m.getY()));

                CoordGene<Integer> coord = new CoordGene<Integer>(coordAx.getX().intValue(), coordAx.getY().intValue());
                if (core.isTile(coord)) {

                    Tile tileTemp = core.getBoard().getTile(coord);
                    List<Tile> listTiles = core.getBoard().getAboveAndBelow(tileTemp);
                    
                    if (!listTiles.isEmpty()) {

                        Rectangle fondPopup = new Rectangle(0,-Consts.SIDE_SIZE,Consts.SIDE_SIZE*2,Consts.SIDE_SIZE*2*(listTiles.size()+1));
                        fondPopup.setArcWidth(20);
                        fondPopup.setArcHeight(20);
                        fondPopup.setFill(Color.IVORY);
                      
                        popup.getContent().add(fondPopup);
                        popupUnderBeetle(listTiles, tileTemp);
                        popup.show(gameCanvas, m.getScreenX()+fondPopup.getWidth()/2, m.getScreenY() +fondPopup.getHeight()/2);
                    } else if(listTiles.isEmpty() && popup.isShowing()){
                        popup.hide();
                        popup = new Popup();
                    }
                }
            }
        });

        gameCanvas.setOnDragDetected(e -> {
            String name = getClass().getClassLoader().getResource("main/resources/img/misc/crossCursor.png").toString();
            Image n = new Image(name);
            gameCanvas.setCursor(new ImageCursor(n));
            e.consume();

        });

        gameCanvas.setOnMouseReleased(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent t) {
                gameCanvas.setCursor(Cursor.DEFAULT);
            }

        });

        gameCanvas.setOnDragDone(event -> {
            gameCanvas.setCursor(Cursor.DEFAULT);
            event.consume();
        });

        gameCanvas.setOnScroll(new EventHandler<ScrollEvent>() {

            @Override
            public void handle(ScrollEvent event) {
                if(core.getState() != Consts.ANIMATING){
                    if (event.getDeltaY() > 0){ 
                        if(Consts.SIDE_SIZE <=120)
                            Consts.SIDE_SIZE *= 1.1;
                    }else if(Consts.SIDE_SIZE >= 20){
                        Consts.SIDE_SIZE *= 0.9;
                    }
                }
            }
        });
        gameCanvas.widthProperty().bind(panCanvas.widthProperty());
        gameCanvas.heightProperty().bind(panCanvas.heightProperty());
    }
    
        private void popupUnderBeetle(List<Tile> listTiles, Tile tileTemp) {

        drawPolygonPopUp(0, tileTemp);
        int deplacement = 2;
        Collections.reverse(listTiles); // A CHANGER UN PEU LOURD PEUT ETRE 
        for (Tile tile : listTiles) {
            drawPolygonPopUp(deplacement, tile);
            deplacement += 2;
        }

    }

    private void drawPolygonPopUp(int deplacement, Tile tile) {
        Hexagon hex = new Hexagon();
        hex.setxPixel(0.0);
        hex.setyPixel(0.0);
        hex.calculHex();
        double x[] = hex.getListXCoord();
        double y[] = hex.getListYCoord();
        int placement = (int) (Consts.SIDE_SIZE);
        Piece piece = tile.getPiece();
        
        Polygon p = new Polygon();
        p.setFill(new ImagePattern(piece.getImage()));
        p.getPoints().addAll(new Double[]{
            x[0]+placement, y[0] + (Consts.SIDE_SIZE * deplacement),
            x[1]+placement, y[1] + Consts.SIDE_SIZE * deplacement,
            x[2]+placement, y[2] + Consts.SIDE_SIZE * deplacement,
            x[3]+placement, y[3] + Consts.SIDE_SIZE * deplacement,
            x[4]+placement, y[4] + Consts.SIDE_SIZE * deplacement,
            x[5]+placement, y[5] + Consts.SIDE_SIZE * deplacement});
        popup.getContent().add(p);

    }
    
    public void handleHelp(){
        if(core.getState() == Consts.WAIT_FOR_INPUT){
            clearHelp();
            HelpMove helpMove = core.help();
            highlighted.setHelp(helpMove);
            if (helpMove.isAdd()){
                    int index;
                    List<Piece> list = core.getCurrentPlayerObj().getInventory();
                    for (index = 0; index<list.size() && list.get(index).getId() != helpMove.getPieceId(); index++);
                    ToggleButton n = (ToggleButton)((core.getCurrentPlayer() == 0)?inventoryPlayer1:inventoryPlayer2).getChildren().get(index);
                    n.setBorder(new Border(new BorderStroke(Color.RED, BorderStrokeStyle.SOLID, new CornerRadii(10), new BorderWidths(3))));
            }
        }
    }
    
    public void handleInputChat(KeyEvent e){
        if(e.getCode().toString().equals("ENTER")){
            core.sendMessage(inputChat.getText());
            inputChat.clear();
        }
    }
    

    public void handleUpButton() {
        if(core.getState() != Consts.ANIMATING)
            traductor.setMoveOrigin(new CoordGene<>(traductor.getMoveOrigin().getX(), traductor.getMoveOrigin().getY() + 10));
    }

    public void handleRightButton() {
        if(core.getState() != Consts.ANIMATING)
            traductor.setMoveOrigin(new CoordGene<>(traductor.getMoveOrigin().getX() + 10, traductor.getMoveOrigin().getY()));
    }

    public void handleDownButton() {
        if(core.getState() != Consts.ANIMATING)
            traductor.setMoveOrigin(new CoordGene<>(traductor.getMoveOrigin().getX(), traductor.getMoveOrigin().getY() - 10));
    }

    public void handleLeftButton() {
        if(core.getState() != Consts.ANIMATING)
            traductor.setMoveOrigin(new CoordGene<>(traductor.getMoveOrigin().getX() - 10, traductor.getMoveOrigin().getY()));
    }
    
    public void handlePlusButton() {    
        if(core.getState() != Consts.ANIMATING && Consts.SIDE_SIZE <=120)
            Consts.SIDE_SIZE *= 1.1;              
    }
        
    public void handleMinusButton() {
        if(core.getState() != Consts.ANIMATING && Consts.SIDE_SIZE >= 20)
            Consts.SIDE_SIZE *= 0.9;
    }

    public void handleUndoButton() {
        if(core.getState() == Consts.WAIT_FOR_INPUT || core.getState() == Consts.END_OF_THE_GAME){
            core.previousState();
            while (core.getMode() != Consts.PVP && core.hasPreviousState() && core.getCurrentPlayer() == (core.getMode() == Consts.PVAI ? Consts.PLAYER2 : Consts.PLAYER1)) {
                core.previousState();
            }
            if (core.getMode() != Consts.PVP && core.getCurrentPlayer() == (core.getMode() == Consts.PVAI ? Consts.PLAYER2 : Consts.PLAYER1))
                core.playAI();
            checkHistory();
            resetPiece();
            clearHelp();
            highlighted.setListTohighlight(null);
            highlighted.setHelp(null);
            initButtonByInventory();
        }
    }

    public void handleRedoButton() {
        if(core.getState() == Consts.WAIT_FOR_INPUT || core.getState() == Consts.END_OF_THE_GAME){
            core.nextState();
            while (core.getMode() != Consts.PVP && core.hasNextState() && core.getCurrentPlayer() == (core.getMode() == Consts.PVAI ? Consts.PLAYER2 : Consts.PLAYER1)) {
                core.nextState();
            }
            checkHistory();
            resetPiece();
            clearHelp();
            highlighted.setListTohighlight(null);
            highlighted.setHelp(null);
            initButtonByInventory();
        }
    }

    /*Méthodes d'initialisation */
    public void giveHand(){
        initButtonByInventory();
        /*
        inventoryGroup.getToggles().remove(0, inventoryGroup.getToggles().size());
         
        namePlayer1.setText(core.getPlayers()[0].getName() + " : changement de tour !");
        namePlayer2.setText(core.getPlayers()[1].getName() + " : changement de tour !"); 
        initPlayer1Button();
        initPlayer2Button();
        */
    }
    
    
    public void initButtonByInventory() {
        inventoryGroup.getToggles().remove(0, inventoryGroup.getToggles().size());
        if (core.getCurrentPlayer() == 0) {
            inventoryPlayer1.setBorder(new Border(new BorderStroke(Color.GREEN, BorderStrokeStyle.SOLID, new CornerRadii(10), new BorderWidths(3))));
            inventoryPlayer2.setBorder(Border.EMPTY);
            inventoryPlayer1.setStyle("-fx-effect: none");
            inventoryPlayer2.setStyle("-fx-effect: innershadow(one-pass-box, lightblue, 100, 0.1, 1, 1);");
            namePlayer1.setText(core.getPlayers()[0].getName() + " à vous de jouer !");
            namePlayer2.setText(core.getPlayers()[1].getName() + " attend !");
        } else {
            
            inventoryPlayer1.setBorder(Border.EMPTY);
            inventoryPlayer2.setStyle("-fx-effect: none");
            inventoryPlayer1.setStyle("-fx-effect: innershadow(one-pass-box, lightgrey, 100, 0.1, 1, 1);");
            inventoryPlayer2.setBorder(new Border(new BorderStroke(Color.GREEN, BorderStrokeStyle.SOLID, new CornerRadii(10), new BorderWidths(3))));

            namePlayer1.setText(core.getPlayers()[0].getName() + " attend !");
            namePlayer2.setText(core.getPlayers()[1].getName() + " à vous de jouer !");
        }
        initPlayer1Button();
        initPlayer2Button();
    }

    public void initPlayer1Button() {
        inventoryPlayer1.getChildren().remove(0, inventoryPlayer1.getChildren().size());
        List<Piece> inventory = core.getPlayers()[0].getInventory();
        int line = 0;
        int col = 0;
        for (int i = 0; i < inventory.size(); i++) {
            Piece piece = inventory.get(i);
            ToggleButton b = new ToggleButton();
            b.setMinSize(65, 65);
            b.setBackground(new Background(new BackgroundFill(new ImagePattern(piece.getImage()), CornerRadii.EMPTY, Insets.EMPTY)));


            if (core.getCurrentPlayer() == Consts.PLAYER1 && core.getState() == Consts.WAIT_FOR_INPUT) {
                b.setOnMouseClicked(new ControllerButtonPiece(this,inventory.get(i).getId(), i));
                //eventDragAndDropPiece(b,inventory.get(i).getId());
                b.getStyleClass().add("buttonInventory");
                b.setCursor(Cursor.HAND);
                b.setTooltip(new Tooltip(inventory.get(i).getDescription()));
                inventoryGroup.getToggles().add(b);
            }
            b.disableProperty().bind(animationPlaying);
            if (i != 0 && i % 4 == 0) {
                col = 0;
                line++;
            }
            GridPane.setConstraints(b, col, line);
            GridPane.setHalignment(b, HPos.CENTER);
            GridPane.setValignment(b, VPos.TOP);
            inventoryPlayer1.getChildren().add(b);
            col++;

        }
    }

    public void initPlayer2Button() {
        inventoryPlayer2.getChildren().remove(0, inventoryPlayer2.getChildren().size());
        List<Piece> inventory = core.getPlayers()[1].getInventory();
        int line = 0;
        int col = 0;
        for (int i = 0; i < inventory.size(); i++) {
            Piece piece = inventory.get(i);
            ToggleButton b = new ToggleButton();
            b.setMinSize(65, 65);
            b.setBackground(new Background(new BackgroundFill(new ImagePattern(piece.getImage()), CornerRadii.EMPTY, Insets.EMPTY)));

            if (core.getCurrentPlayer() == Consts.PLAYER2 && core.getState() == Consts.WAIT_FOR_INPUT) {
                b.setOnMouseClicked(new ControllerButtonPiece(this,inventory.get(i).getId(), i));
                b.getStyleClass().add("buttonInventory");
                b.setCursor(Cursor.HAND);
                b.setTooltip(new Tooltip(inventory.get(i).getDescription()));
                inventoryGroup.getToggles().add(b);
            }
            b.disableProperty().bind(animationPlaying);

            if (i != 0 && i % 4 == 0) {
                col = 0;
                line++;
            }

            GridPane.setConstraints(b, col, line);
            GridPane.setHalignment(b, HPos.CENTER);
            GridPane.setValignment(b, VPos.TOP);
            inventoryPlayer2.getChildren().add(b);
            col++;
        }
    }

    public void resetPiece() {
        if (inventoryGroup.getSelectedToggle() != null) {
            inventoryGroup.getSelectedToggle().setSelected(false);
        }
        pieceToMove = null;
        pieceChosenInInventory = -1;
    }

    
    public void clearHelp(){
		for(Node n : inventoryPlayer1.getChildren())
			((ToggleButton) n).setBorder(null);
		for(Node n : inventoryPlayer2.getChildren())
			((ToggleButton) n).setBorder(null);
    }
    
    public void startMovingAnimation(CoordGene<Integer> coordStart, CoordGene<Integer> coordEnd){
        core.setState(Consts.ANIMATING);
        
        animationPlaying.setValue(true);
        highlighted.setListTohighlight(null);
        highlighted.setHelp(null);

        Piece piece = core.getBoard().getTile(coordStart).getPiece();
        animation.initMovingAnimation(piece.getImage(), coordStart, coordEnd);
        
        animation.getPathAnimation().setOnFinished(new EventHandler<ActionEvent>() {
 
            @Override
            public void handle(ActionEvent event) {
                panCanvas.getChildren().remove(animation.getPolygon());
                handleResize(coordStart,coordEnd);
                core.movePiece(pieceToMove, coordEnd);
                
                resetPiece();
                clearHelp();
                animationPlaying.setValue(false);
            }
        });
 
        animation.play();
    }

    public void startPlacingAnimation(int idPiece, CoordGene<Integer> coordEnd) {
        core.setState(Consts.ANIMATING);

        animationPlaying.setValue(true);
        highlighted.setListTohighlight(null);
        highlighted.setHelp(null);

        Piece piece = null;

        for (int i = 0; i < core.getPlayers()[core.getCurrentPlayer()].getInventory().size(); i++) {
            if (core.getPlayers()[core.getCurrentPlayer()].getInventory().get(i).getId() == idPiece) {
                piece = core.getPlayers()[core.getCurrentPlayer()].getInventory().get(i);
            }
        }
        animation.initPlacingAnimation(piece.getImage(), coordEnd);
        animation.getPathAnimation().setOnFinished(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent event) {

                panCanvas.getChildren().remove(animation.getPolygon());
                handleResize(null,coordEnd);
                core.addPiece(idPiece, coordEnd);
                
                resetPiece();
                clearHelp();
                animationPlaying.setValue(false);
                
            }
        });
        animation.play();
    }
    
    public void startMovingEmulatorAnimation(CoordGene<Integer> coordStart, CoordGene<Integer> coordEnd, String move, String unmove){
    	core.setState(Consts.ANIMATING);
        
        animationPlaying.setValue(true);
        highlighted.setListTohighlight(null);
        highlighted.setHelp(null);

        Piece piece = core.getBoard().getTile(coordStart).getPiece();
        animation.initMovingAnimation(piece.getImage(), coordStart, coordEnd);
        animation.getPathAnimation().setOnFinished(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent event) {
                panCanvas.getChildren().remove(animation.getPolygon());
                handleResize(coordStart,coordEnd);
                core.playEmulate(move,unmove);
                core.setState(Consts.READY_TO_CHANGE);
               
                resetPiece();
                clearHelp();
                animationPlaying.setValue(false);
            }
        });
        animation.play();
    }
    
    public void startPlacingEmulatorAnimation(int idPiece, CoordGene<Integer> coordEnd,String move, String unmove) {
    	core.setState(Consts.ANIMATING);

        animationPlaying.setValue(true);
        highlighted.setListTohighlight(null);
        highlighted.setHelp(null);

        Piece piece = null;

        for (int i = 0; i < core.getPlayers()[core.getCurrentPlayer()].getInventory().size(); i++) {
            if (core.getPlayers()[core.getCurrentPlayer()].getInventory().get(i).getId() == idPiece) {
                piece = core.getPlayers()[core.getCurrentPlayer()].getInventory().get(i);
            }
        }
        animation.initPlacingAnimation(piece.getImage(), coordEnd);
        
        animation.getPathAnimation().setOnFinished(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent event) {
                panCanvas.getChildren().remove(animation.getPolygon());
                core.playEmulate(move,unmove);
                core.setState(Consts.READY_TO_CHANGE);
                handleResize(null,coordEnd);
                resetPiece();
                clearHelp();
                animationPlaying.setValue(false);
            }
            
        });
        animation.play();
    }


    public void handleNewGame() throws IOException {
        if(core.getState() == Consts.WAIT_FOR_INPUT || core.getState() == Consts.END_OF_THE_GAME){
            Alert popup = new Alert(Alert.AlertType.NONE, "Voulez-vous relancer la partie ?", null);
            popup.setTitle("Relancer la partie");
            ButtonType ok = new ButtonType("Relancer",ButtonBar.ButtonData.LEFT);
            ButtonType saveAndQuit = new ButtonType("Sauvegarder et quitter",ButtonBar.ButtonData.OTHER);
            ButtonType cancel = new ButtonType("Annuler",ButtonBar.ButtonData.RIGHT);
            if(core.getState() != Consts.END_OF_THE_GAME)
                popup.getButtonTypes().addAll(ok,saveAndQuit,cancel);
            else
                popup.getButtonTypes().addAll(ok,cancel);

            Optional<ButtonType> result = popup.showAndWait();
            if (result.get().getButtonData() == ButtonBar.ButtonData.LEFT) {
                relaunchGameScreen();
            } else if (result.get().getButtonData() == ButtonBar.ButtonData.OTHER) {
                handleSaveAndQuitGame(Consts.GO_TO_GAME);
            }
        }
    }

    public void relaunchGameScreen() {
        Core c = new Core(core.getMode(), core.getDifficulty());
        c.getPlayers()[0].setName(core.getPlayers()[0].getName());
        c.getPlayers()[1].setName(core.getPlayers()[1].getName());
        refreshor.stop();
        main.showGameScreen(c,false);
    }

    public void handleLeaveGame() throws IOException {
        
        Alert popup = new Alert(Alert.AlertType.NONE, "Voulez-vous quitter la partie ?", null);
        popup.setTitle("Quitter la partie");
        ButtonType ok = new ButtonType("Quitter",ButtonBar.ButtonData.LEFT);
        ButtonType saveAndQuit = new ButtonType("Sauvegarder et quitter",ButtonBar.ButtonData.OTHER);
        ButtonType cancel = new ButtonType("Annuler",ButtonBar.ButtonData.RIGHT);
        if(core.getState() != Consts.END_OF_THE_GAME && core.getMode() != Consts.PVEX && core.getMode() != Consts.EXVP)
            popup.getButtonTypes().addAll(ok,saveAndQuit,cancel);
        else
            popup.getButtonTypes().addAll(ok,cancel);

        Optional<ButtonType> result = popup.showAndWait();
        if (result.get().getButtonData() == ButtonBar.ButtonData.LEFT) {
        	core.disconnect();
            refreshor.stop();
            main.showMainMenu();
        } else if (result.get().getButtonData() == ButtonBar.ButtonData.OTHER) {
            handleSaveAndQuitGame(Consts.GO_TO_MAIN);
        }
        
    }

    public void handleLoadGame() throws IOException {
        if(core.getState() == Consts.WAIT_FOR_INPUT || core.getState() == Consts.END_OF_THE_GAME){
            Alert popup = new Alert(Alert.AlertType.NONE, "Voulez-vous quitter la partie ?", null);
            popup.setTitle("Charger une partie");
            ButtonType ok = new ButtonType("Quitter et charger",ButtonBar.ButtonData.LEFT);
            ButtonType saveAndQuit = new ButtonType("Sauvegarder et quitter",ButtonBar.ButtonData.OTHER);
            ButtonType cancel = new ButtonType("Annuler",ButtonBar.ButtonData.RIGHT);
            if(core.getState() != Consts.END_OF_THE_GAME)
                popup.getButtonTypes().addAll(ok,saveAndQuit,cancel);
            else
                popup.getButtonTypes().addAll(ok,cancel);

            Optional<ButtonType> result = popup.showAndWait();
            if (result.get().getButtonData() == ButtonBar.ButtonData.LEFT) {
                refreshor.stop();
                main.showLoadGameScreen();
            } else if (result.get().getButtonData() == ButtonBar.ButtonData.OTHER) {
                handleSaveAndQuitGame(Consts.GO_TO_LOAD);
            }
        }
    }

    public void handleEndGame(){
        helpButton.setDisable(true);
        saveMenuItem.setDisable(true);

        animationPlaying.setValue(false);
        highlighted.setListTohighlight(null);    
        initButtonByInventory();
        
        Dialog<ButtonType> popup = new Dialog<>();
        popup.setTitle("Fin de partie");
        ButtonType restart = new ButtonType("Relancer la partie", ButtonBar.ButtonData.LEFT);
        ButtonType mainMenu = new ButtonType("Menu principal", ButtonBar.ButtonData.OTHER);
        ButtonType cancel = new ButtonType("Fermer", ButtonBar.ButtonData.RIGHT);
        if(core.getMode() != Consts.PVEX && core.getMode() != Consts.EXVP)
            popup.getDialogPane().getButtonTypes().addAll(restart, mainMenu,cancel);
        else
            popup.getDialogPane().getButtonTypes().addAll(mainMenu);

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 150, 10, 10));

        popup.getDialogPane().setContent(grid);
        Label gameStatus = new Label();
        grid.add(gameStatus, 0, 0);
        
        switch (core.getStatus()) {
            case Consts.WIN_TEAM1:
                namePlayer1.setText(core.getPlayers()[0].getName() + " a perdu !");
                namePlayer2.setText(core.getPlayers()[1].getName() + " a gagné !");
                gameStatus.setText("Le joueur " + core.getPlayers()[1].getName() + " remporte la victoire !");
                break;
            case Consts.WIN_TEAM2:
                namePlayer1.setText(core.getPlayers()[0].getName() + " a gagné !");
                namePlayer2.setText(core.getPlayers()[1].getName() + " a perdu !");
                gameStatus.setText("Le joueur " + core.getPlayers()[0].getName() + " remporte la victoire");
                break;
            case Consts.NUL:
                namePlayer1.setText(core.getPlayers()[0].getName() + " : match nul !");
                namePlayer2.setText(core.getPlayers()[1].getName() + " : match nul !");
                gameStatus.setText("Match nul");
                break;
            default:
                gameStatus.setText("Ne devrait pas arriver !");
                break;
        }
        Optional<ButtonType> result = popup.showAndWait();
        if (result.get().getButtonData() == ButtonBar.ButtonData.LEFT) {
            relaunchGameScreen();
        }
        else if(result.get().getButtonData() == ButtonBar.ButtonData.OTHER){
            refreshor.stop();
            main.showMainMenu();
        }
    }

    public void handleSaveGame() throws IOException{
       if(core.getState() == Consts.WAIT_FOR_INPUT){
            Dialog<ButtonType> popup = new Dialog<>();
            popup.setTitle("Sauvegarder la partie");
            ButtonType save = new ButtonType("Sauvegarder", ButtonBar.ButtonData.LEFT);
            ButtonType cancel = new ButtonType("Annuler", ButtonBar.ButtonData.RIGHT);
            popup.getDialogPane().getButtonTypes().addAll(save, cancel);

            GridPane grid = new GridPane();
            grid.setHgap(10);
            grid.setVgap(10);
            grid.setPadding(new Insets(20, 150, 10, 10));
            TextField saveName = new TextField();
            
            String saveString;
            if (core.getMode() == Consts.PVP) {
                saveString = core.getPlayers()[Consts.PLAYER1].getName() + "-" + core.getPlayers()[Consts.PLAYER2].getName() + "-turn" + core.getTurn();
            } else if (core.getMode() == Consts.PVAI) {
                saveString = core.getPlayers()[Consts.PLAYER1].getName() + "-AI_"
                        + (core.getDifficulty() == Consts.EASY ? "EASY" : core.getDifficulty() == Consts.MEDIUM ? "MEDIUM" : "HARD")
                        + "-turn" + core.getTurn();
            }
            else{
                saveString =  "AI_"+ (core.getDifficulty() == Consts.EASY ? "EASY-" : core.getDifficulty() == Consts.MEDIUM ? "MEDIUM-" : "HARD-")+ core.getPlayers()[Consts.PLAYER2].getName()+"-turn" + core.getTurn();
            }
            
            saveName.setPromptText(saveString);

            popup.getDialogPane().setContent(grid);
            grid.add(new Label("Nom de la sauvegarde :"), 0, 0);
            grid.add(saveName, 1, 0);

            Optional<ButtonType> result = popup.showAndWait();
            if (result.get().getButtonData() == ButtonBar.ButtonData.LEFT) {
                if (!saveName.getText().equals("")) {
                    saveString = saveName.getText();
                }
                if (core.save(saveString))
                    takeSnapshot(saveString);
            }
       }
    }
    
   
    public void handleSaveAndQuitGame(int status) throws IOException{
       
        Dialog<ButtonType> popup = new Dialog<>();
        popup.setTitle("Sauvegarder et quitter la partie");
        ButtonType saveAndQuit = new ButtonType("Sauvegarder et quitter", ButtonBar.ButtonData.LEFT);
        ButtonType cancel = new ButtonType("Annuler", ButtonBar.ButtonData.RIGHT);
        popup.getDialogPane().getButtonTypes().addAll(saveAndQuit, cancel);

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 150, 10, 10));
        TextField saveName = new TextField();
        
        String saveString;
        if (core.getMode() == Consts.PVP) {
            saveString = core.getPlayers()[Consts.PLAYER1].getName() + "-" + core.getPlayers()[Consts.PLAYER2].getName() + "-turn" + core.getTurn();
        } else if (core.getMode() == Consts.PVAI) {
            saveString = core.getPlayers()[Consts.PLAYER1].getName() + "-AI_"
                    + (core.getDifficulty() == Consts.EASY ? "EASY" : core.getDifficulty() == Consts.MEDIUM ? "MEDIUM" : "HARD")
                    + "-turn" + core.getTurn();
        }
        else{
            saveString =  "AI_"+ (core.getDifficulty() == Consts.EASY ? "EASY-" : core.getDifficulty() == Consts.MEDIUM ? "MEDIUM-" : "HARD-")+ core.getPlayers()[Consts.PLAYER2].getName()+"-turn" + core.getTurn();
        }
        saveName.setPromptText(saveString);

        popup.getDialogPane().setContent(grid);
        grid.add(new Label("Nom de la sauvegarde :"), 0, 0);
        grid.add(saveName, 1, 0);

        Optional<ButtonType> result = popup.showAndWait();
        if (result.get().getButtonData() == ButtonBar.ButtonData.LEFT) {
            if (!saveName.getText().equals("")) {
                saveString = saveName.getText();
            }
            if (core.save(saveString))
                takeSnapshot(saveString);
            switch (status) {
                case (Consts.GO_TO_MAIN):
                    refreshor.stop();
                    main.showMainMenu();
                    break;
                case (Consts.GO_TO_LOAD):
                    refreshor.stop();
                    main.showLoadGameScreen();
                    break;
                case (Consts.GO_TO_GAME):
                    relaunchGameScreen();
                    break;
            }
        }
    }

    public void handleResize(CoordGene<Integer> coordStart,CoordGene<Integer> coordEnd) {
        CoordGene<Double> newOrigin = traductor.getMoveOrigin();
        if (coordEnd.getX() == 0 && coordEnd.getY() == 0) {
            newOrigin.setX(newOrigin.getX() - Consts.SIDE_SIZE * 2.6);
            newOrigin.setY(newOrigin.getY() - Consts.SIDE_SIZE * 1.5);
        }
        else if (coordEnd.getX() == 0) {
            newOrigin.setX(newOrigin.getX() - Consts.SIDE_SIZE*1.725);
        }
        else if(coordEnd.getY() == 0){
            newOrigin.setX(newOrigin.getX() - Consts.SIDE_SIZE * 0.875);
            newOrigin.setY(newOrigin.getY() - Consts.SIDE_SIZE * 1.5);
        }
        if(coordStart != null && coordStart.getY() == 1 && coordEnd.getY() != 1){
            int nbPieces=0;
            List<Column> col = core.getBoard().getBoard();
            for(int i = 0; i< col.size();i++){
                if(col.size() > 0){
                    if(col.get(i).size() > 1){
                        Box box = col.get(i).get(1);
                        if(box.size() != 0 && box.get(0).getPiece() != null){
                            nbPieces+= box.size();
                        }
                    }
                }
            }
            if(nbPieces == 1){
                newOrigin.setX(newOrigin.getX() + Consts.SIDE_SIZE*0.875);
                newOrigin.setY(newOrigin.getY() + Consts.SIDE_SIZE * 1.5);
            }
        }
        
        if(coordStart != null && coordStart.getX() == 1 && coordEnd.getX() != 1){
            int nbPieces=0;
            List<Box> list = core.getBoard().getBoard().get(1);
            for(int i = 0; i< list.size();i++){
                    if(list.get(i).size() != 0 && list.get(i).get(0).getPiece() != null){
                        nbPieces+= list.get(i).size();
                    }
                }
            if(nbPieces == 1){
                newOrigin.setX(newOrigin.getX() + Consts.SIDE_SIZE*1.725);
            }
        }
        traductor.setMoveOrigin(newOrigin);
    }

    public void takeSnapshot(String name) throws IOException {
        WritableImage screenshot = new WritableImage((int) main.getPrimaryStage().getScene().getWidth(), (int) main.getPrimaryStage().getScene().getHeight());
        main.getPrimaryStage().getScene().snapshot(screenshot);
        if (!Files.isDirectory(Paths.get("Hive_init/Hive_save_images"))) {
            Files.createDirectories(Paths.get("Hive_init/Hive_save_images"));
        }

        File file = new File("Hive_init/Hive_save_images/" + name + ".png");
        ImageIO.write(SwingFXUtils.fromFXImage(screenshot, null), "png", file);
    }
    
    private void checkHistory() {
        if (core.hasNextState()) {
            redo.setDisable(false);
        } else {
            redo.setDisable(true);
        }
        if (core.hasPreviousState()) {
            undo.setDisable(false);
        } else {
            undo.setDisable(true);
        }
    }
    
    public void hideButtonsForNetwork(){
        undo.setVisible(false);
        redo.setVisible(false);
        helpButton.setVisible(false);
        saveMenuItem.setVisible(false);
        lanchNewGame.setVisible(false);
        loadGame.setVisible(false);
    }
    
    public void updateChat(){
        if(core.getLastMsg().size() != nbMessage){
            nbMessage = core.getLastMsg().size() - nbMessage;
            int me = core.getMode()==Consts.PVEX?Consts.PLAYER1:Consts.PLAYER2;
            for(int i = 0; i < nbMessage;i++ ){ 
            	textChat.addRow(nbChatRow + i, new Label(""));
                if(core.getLastMsg().get(i).y == me){
                    Label msg = new Label(core.getLastMsg().get(i).x);
                    msg.setBackground(new Background(new BackgroundFill(Color.LIGHTBLUE, new CornerRadii(0), new Insets(0))));
                    textChat.add(msg,1, nbChatRow +i);
                    textChat.setHalignment(msg,HPos.RIGHT);                
                }else{
                    Label msg = new Label(core.getLastMsg().get(i).x);
                    msg.setBackground(new Background(new BackgroundFill(Color.LIGHTCORAL, new CornerRadii(0), new Insets(0))));
                    textChat.add(msg,0, nbChatRow +i);
                    textChat.setHalignment(msg,HPos.LEFT); 
                }
            }
            nbChatRow = nbChatRow + nbMessage;
            nbMessage = core.getLastMsg().size();
            scrollChat.setVvalue(scrollChat.getVmax());
        }
    }
    
    public void disconnected() {
        Dialog<ButtonType> popup = new Dialog<>();
        popup.setTitle("Deconnexion");
        ButtonType menu = new ButtonType("Menu Principal", ButtonBar.ButtonData.RIGHT);
        popup.getDialogPane().getButtonTypes().add(menu);

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 150, 10, 10));

        popup.getDialogPane().setContent(grid);
        grid.add(new Label("Votre adversaire s'est déconnecté"), 0, 0);

        Optional<ButtonType> result = popup.showAndWait();
        if (result.get().getButtonData() == ButtonBar.ButtonData.RIGHT) {
        	refreshor.stop();
        	main.showMainMenu();
        }
    }
    
    public void fixOriginForLoad(){
        if(core.getTurn() != 0){
            traductor.setMoveOrigin(new CoordGene<Double>(Consts.SIDE_SIZE,Consts.SIDE_SIZE));
        }
    }
    
    public void queenWarning(){
        
        Dialog<ButtonType> popup = new Dialog<>();
        popup.setTitle("Ajout impossible");
        ButtonType close = new ButtonType("Fermer", ButtonBar.ButtonData.RIGHT);
        popup.getDialogPane().getButtonTypes().add(close);

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 150, 10, 10));

        popup.getDialogPane().setContent(grid);
        grid.add(new Label("Vous devez jouer votre reine."), 0, 0);
        popup.show();
    }
    
    public void recenter(){
        Consts.SIDE_SIZE = 50;
        traductor.setMoveOrigin(new CoordGene<Double>(Consts.SIDE_SIZE,Consts.SIDE_SIZE));
    }
    
    
    public Main getMain() {
        return main;
    }
    
    public void setMainApp(Main mainApp) {
        this.main = mainApp;
    }

    public void setCore(Core c) {
        this.core = c;
    }

    public void setPieceToChoose(int pieceChosenInInventory) {
        this.pieceChosenInInventory = pieceChosenInInventory;
    }

    public void setPieceToMove(CoordGene<Integer> pieceToMove) {
        this.pieceToMove = pieceToMove;
    }

    public void setHighlighted(Highlighter highlighted) {
        this.highlighted = highlighted;
    }

    public boolean isFreeze() {
        return animationPlaying.getValue();
    }

    public Core getCore() {
        return core;
    }

    public Highlighter getHighlighted() {
        return highlighted;
    } 
    
    public GridPane getTextChat() {
        return textChat;
    }
    
    public ToggleGroup getInventoryGroup() {
        return inventoryGroup;
    }
}
