package main.java.controller;


import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
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
import javafx.scene.canvas.Canvas;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.image.WritableImage;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.AnchorPane;
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
import javafx.scene.shape.LineTo;
import javafx.scene.shape.MoveTo;
import javafx.scene.shape.Path;
import javafx.scene.text.Text;
import main.java.engine.Core;
import main.java.implement.Main;
import main.java.model.Piece;
import main.java.utils.Consts;
import main.java.utils.CoordGene;
import main.java.view.AnimationTile;
import main.java.view.Highlighter;
import main.java.view.RefreshJavaFX;
import main.java.view.TraducteurBoard;

/**
 *
 * @author duvernet
 */
public class GameScreenController implements Initializable {
    @FXML private AnchorPane mainAnchor;
    @FXML private Canvas gameCanvas;
    @FXML private Pane panCanvas;
    @FXML private Path path;
    @FXML private GridPane inventoryPlayer1;
    @FXML private GridPane inventoryPlayer2;
    @FXML private Text namePlayer1;
    @FXML private Text namePlayer2;
    @FXML private Button undo;
    @FXML private Button redo;
    private Main main;
    private Core core;
    private int pieceToChoose;
    private CoordGene<Double> lastCoord;
    private CoordGene<Integer> pieceToMove;
    private List<CoordGene<Integer>> possibleMovement;
    private Highlighter highlighted;
    private TraducteurBoard t;
    private BooleanProperty freeze;
    private boolean endOfGame;
    
    private ToggleGroup inventoryGroup;
    private RefreshJavaFX r;
    private AnimationTile animation;
   
    
   
  
     @Override
    public void initialize(URL url, ResourceBundle rb) {
    }
   
    public void initGame(Main mainApp, Core c){
        setMainApp(mainApp);
        setCore(c);
        pieceToChoose = -1;
        highlighted = new Highlighter();
        t = new TraducteurBoard();
        freeze = new SimpleBooleanProperty();
        freeze.setValue(false);
        endOfGame = false;
        inventoryGroup = new ToggleGroup();
        if (!core.hasPreviousState())
        	undo.setDisable(true);
        if (!core.hasNextState())
        	redo.setDisable(true);
        initButtonByInventory();
//        initButtonActivation();
        animation = new AnimationTile();                     
        initGameCanvas();
       
        r = new RefreshJavaFX(core, gameCanvas, highlighted, t);
        r.start();
       
    }
    /* Inititialisation des handlers */
//    
//    public void initButtonActivation(){
//        undo.disableProperty().bind(freeze);
//        redo.disableProperty().bind(freeze);
//    }
    
    
    public void initGameCanvas(){
       
        gameCanvas.setOnMousePressed(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent m) {
                if(!endOfGame && !freeze.getValue()){
                    CoordGene<Double> coordAx = t.pixelToAxial(new CoordGene<Double>(m.getX(), m.getY()));
                    CoordGene<Integer> coord = new CoordGene<Integer>(coordAx.getX().intValue(), coordAx.getY().intValue());
                    CoordGene<Double> origin = t.getMoveOrigin();
                    lastCoord = new CoordGene<Double>(m.getX() - origin.getX(), m.getY() - origin.getY());
                   
                    if (m.getButton() == MouseButton.PRIMARY) {
                        if (core.isTile(coord)) {
                            if (pieceToMove != null &&  possibleMovement.contains(coord)) {
                                startMovingAnimation(pieceToMove,coord);
                                undo.setDisable(false);
                                redo.setDisable(true);
                            } else if (core.isPieceOfCurrentPlayer(coord)) {
                                    pieceToMove = coord;
                                    possibleMovement = core.getPossibleMovement(coord);
                                    highlighted.setListTohighlight(possibleMovement);
                                    pieceToChoose = -1;
                            } else {
                                if (pieceToChoose != -1 && core.getPossibleAdd(core.getCurrentPlayer()).contains(coord)){
                                    if(core.addPiece(pieceToChoose, coord))
                                        handleEndGame();
                                    handleResize(coord); 
                                    undo.setDisable(false);
                                    redo.setDisable(true);
                                }
                                resetPiece();
                                initButtonByInventory();
                                highlighted.setListTohighlight(possibleMovement = null);
                            }
                        } else {
                            resetPiece();
                            highlighted.setListTohighlight(null);
                        }
                    }
                }
            }
        });
       
        gameCanvas.setOnMouseDragged(new EventHandler<MouseEvent>(){
               
                public void handle(MouseEvent m) {
                    if(!freeze.getValue() /*|| endOfGame*/)
                        t.setMoveOrigin(new CoordGene<Double>(m.getX() - lastCoord.getX(),m.getY() - lastCoord.getY()));
                }
    
        });
          
         gameCanvas.setOnDragDetected(e-> {
                String name = getClass().getClassLoader().getResource("main/resources/img/misc/crossCursor.png").toString();
                Image n = new Image(name); 
                gameCanvas.setCursor(new ImageCursor(n));
                e.consume();
            });
           
        gameCanvas.setOnMouseReleased(new EventHandler<MouseEvent>(){
            @Override
            public void handle(MouseEvent t) {
                gameCanvas.setCursor(Cursor.DEFAULT);
            }
           
        });
       
        gameCanvas.setOnDragDone(event->{
                gameCanvas.setCursor(Cursor.DEFAULT);
                event.consume();
        });
             
        gameCanvas.setOnScroll(new EventHandler<ScrollEvent>(){

            @Override
            public void handle(ScrollEvent event) {
                if(event.getDeltaY() > 0){       
                    Consts.SIDE_SIZE*=1.1;
                }
                else{
                    Consts.SIDE_SIZE*=0.9;
                }
            }
        });
        gameCanvas.widthProperty().bind(panCanvas.widthProperty());
        gameCanvas.heightProperty().bind(panCanvas.heightProperty());

    }
    public void handleNewGame() throws IOException{
        Alert popup = new Alert(Alert.AlertType.NONE, "Voulez-vous relancer la partie ?", null);
        ButtonType ok = new ButtonType("Relancer",ButtonBar.ButtonData.LEFT);
        ButtonType saveAndQuit = new ButtonType("Sauvegarder et quitter",ButtonBar.ButtonData.OTHER);
        ButtonType cancel = new ButtonType("Annuler",ButtonBar.ButtonData.RIGHT);
        popup.getButtonTypes().addAll(ok,saveAndQuit,cancel);
       
        Optional<ButtonType> result = popup.showAndWait();
        if(result.get().getButtonData() == ButtonBar.ButtonData.LEFT){
                gameScreen();
        }
        else if(result.get().getButtonData() == ButtonBar.ButtonData.OTHER){
            handleSaveAndQuitGame(Consts.GO_TO_GAME);
        }
    }
   
    public void gameScreen(){
        Core c = new Core(core.getMode(),core.getDifficulty());
        c.getPlayers()[0].setName(core.getPlayers()[0].getName());
        c.getPlayers()[1].setName(core.getPlayers()[1].getName());
        r.stop();
        main.showGameScreen(c);
    }
    
    public void handleLeaveGame() throws IOException{
        Alert popup = new Alert(Alert.AlertType.NONE, "Voulez-vous quitter la partie ?", null);
        ButtonType ok = new ButtonType("Quitter",ButtonBar.ButtonData.LEFT);
        ButtonType saveAndQuit = new ButtonType("Sauvegarder et quitter",ButtonBar.ButtonData.OTHER);
        ButtonType cancel = new ButtonType("Annuler",ButtonBar.ButtonData.RIGHT);
        popup.getButtonTypes().addAll(ok,saveAndQuit,cancel);
       
        Optional<ButtonType> result = popup.showAndWait();
        if(result.get().getButtonData() == ButtonBar.ButtonData.LEFT){
            r.stop();
            main.showMainMenu();
        }
        else if(result.get().getButtonData() == ButtonBar.ButtonData.OTHER){
            handleSaveAndQuitGame(Consts.GO_TO_MAIN);
        }
    }
    
    public void handleLoadGame() throws IOException{
        Alert popup = new Alert(Alert.AlertType.NONE, "Voulez-vous quitter la partie ?", null);
        ButtonType ok = new ButtonType("Quitter et charger",ButtonBar.ButtonData.LEFT);
        ButtonType saveAndQuit = new ButtonType("Sauvegarder et quitter",ButtonBar.ButtonData.OTHER);
        ButtonType cancel = new ButtonType("Annuler",ButtonBar.ButtonData.RIGHT);
        popup.getButtonTypes().addAll(ok,saveAndQuit,cancel);
       
        Optional<ButtonType> result = popup.showAndWait();
        if(result.get().getButtonData() == ButtonBar.ButtonData.LEFT){
            r.stop();
            main.showLoadGameScreen();
        }
        else if(result.get().getButtonData() == ButtonBar.ButtonData.OTHER){
            handleSaveAndQuitGame(Consts.GO_TO_LOAD);
        }
    }
   
    public void handleUpButton(){
        t.setMoveOrigin(new CoordGene<>(t.getMoveOrigin().getX(),t.getMoveOrigin().getY()-10));
    }
   
    public void handleRightButton(){
        t.setMoveOrigin(new CoordGene<>(t.getMoveOrigin().getX()+10,t.getMoveOrigin().getY()));
    }
   
    public void handleDownButton(){
        t.setMoveOrigin(new CoordGene<>(t.getMoveOrigin().getX(),t.getMoveOrigin().getY()+10));
    }
   
    public void handleLeftButton(){
        t.setMoveOrigin(new CoordGene<>(t.getMoveOrigin().getX()-10,t.getMoveOrigin().getY()));
    }
   
    public void handleUndoButton(){
    	core.previousState();
    	while (core.getMode() != Consts.PVP && core.getCurrentPlayer() == (core.getMode() == Consts.PVAI ? Consts.PLAYER2 : Consts.PLAYER1))
    		core.previousState();
    	checkHistory();
        resetPiece();
        highlighted.setListTohighlight(null);
        initButtonByInventory();
    }
   
	public void handleRedoButton(){
		core.nextState();
    	while (core.getMode() != Consts.PVP && core.getCurrentPlayer() == (core.getMode() == Consts.PVAI ? Consts.PLAYER2 : Consts.PLAYER1))
    		core.nextState();
        checkHistory();
        resetPiece();
        highlighted.setListTohighlight(null);
        initButtonByInventory();
    }
    /*Fin des handlers */
   
    /*Méthodes d'initialisation */
    public void initButtonByInventory() {
        inventoryGroup.getToggles().remove(0, inventoryGroup.getToggles().size());
        if(core.getCurrentPlayer() == 0){          
            inventoryPlayer1.setBorder(new Border(new BorderStroke(Color.LIGHTGREEN, BorderStrokeStyle.SOLID, CornerRadii.EMPTY,new BorderWidths(5))));
            inventoryPlayer2.setBorder(new Border(new BorderStroke(Color.RED, BorderStrokeStyle.SOLID, CornerRadii.EMPTY,new BorderWidths(5))));
            namePlayer1.setText(core.getPlayers()[0].getName() + " à vous de jouer !");
            namePlayer2.setText(core.getPlayers()[1].getName() + " attend !");
        }
        else{
            inventoryPlayer1.setBorder(new Border(new BorderStroke(Color.RED, BorderStrokeStyle.SOLID, CornerRadii.EMPTY,new BorderWidths(5))));
            inventoryPlayer2.setBorder(new Border(new BorderStroke(Color.LIGHTGREEN, BorderStrokeStyle.SOLID, CornerRadii.EMPTY,new BorderWidths(5))));

            namePlayer1.setText(core.getPlayers()[0].getName() + " attend !");
            namePlayer2.setText(core.getPlayers()[1].getName() + " à vous de jouer !");
        }
        initPlayer1Button();
        initPlayer2Button();
    }
       
    public void initPlayer1Button(){
        inventoryPlayer1.getChildren().remove(0, inventoryPlayer1.getChildren().size());
        List<Piece> inventory = core.getPlayers()[0].getInventory();
        int line = 0;
        int col = 0;
        for (int i = 0; i < inventory.size(); i++) {
            String name = inventory.get(i).getName();
            int team = inventory.get(i).getTeam();
            ToggleButton b = new ToggleButton();
            b.setMinSize(45, 45);
            b.setBackground(new Background(new BackgroundFill(new ImagePattern(new Image(getClass().getClassLoader().getResource("main/resources/img/tile/"+name + team + ".png").toString())), CornerRadii.EMPTY, Insets.EMPTY)));

            if(core.getCurrentPlayer() == 0 && !endOfGame){
                b.setOnMouseClicked(new ControllerButtonPiece(this,highlighted,core, inventory.get(i).getId(),i));
                b.getStyleClass().add("buttonInventory");
                b.setCursor(Cursor.HAND);
                b.setTooltip(new Tooltip(inventory.get(i).getDescription()));
                b.disableProperty().bind(freeze);
                inventoryGroup.getToggles().add(b);
            }
            if(i != 0 && i%4 == 0){
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
   
    public void initPlayer2Button(){
        inventoryPlayer2.getChildren().remove(0, inventoryPlayer2.getChildren().size());
        List<Piece> inventory = core.getPlayers()[1].getInventory();
        int line = 0;
        int col = 0;
        for (int i = 0; i < inventory.size(); i++) {
            String name = inventory.get(i).getName();
            int team = inventory.get(i).getTeam();
            ToggleButton b = new ToggleButton();
            b.setMinSize(45, 45);
            b.setBackground(new Background(new BackgroundFill(new ImagePattern(new Image(getClass().getClassLoader().getResource("main/resources/img/tile/"+name + team + ".png").toString())), CornerRadii.EMPTY, Insets.EMPTY)));
           
            if(core.getCurrentPlayer() == 1 && !endOfGame){
                b.setOnMouseClicked(new ControllerButtonPiece(this,highlighted,core, inventory.get(i).getId(),i));
                b.getStyleClass().add("buttonInventory");
                b.setCursor(Cursor.HAND);
                b.setTooltip(new Tooltip(inventory.get(i).getDescription()));
                b.disableProperty().bind(freeze);
                inventoryGroup.getToggles().add(b);
            }
           
            if(i!= 0 && i%4 == 0){
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

    public ToggleGroup getInventoryGroup() {
        return inventoryGroup;
    }
   
    public void resetPiece(){
        if(inventoryGroup.getSelectedToggle() != null)
            inventoryGroup.getSelectedToggle().setSelected(false);
        pieceToMove = null;
        pieceToChoose = -1;
    }
    
    public void startMovingAnimation(CoordGene<Integer> coordStart, CoordGene<Integer> coordEnd){
        
        freeze.setValue(true);
        highlighted.setListTohighlight(null);

        Piece piece = core.getBoard().getTile(coordStart).getPiece();
        
        CoordGene<Double> start = new CoordGene<>((double)coordStart.getX(),(double)coordStart.getY());
        CoordGene<Double> end = new CoordGene<>((double)coordEnd.getX(),(double)coordEnd.getY());       
        start = t.axialToPixel(start);
        end = t.axialToPixel(end);
                
        panCanvas.getChildren().add(animation.getPolygon());
        String name = getClass().getClassLoader().getResource("main/resources/img/tile/"+piece.getName()+piece.getTeam()+".png").toString();
        Image image = new Image(name);       
        animation.setImagePolygon(image);
        animation.setPath(new Path( 
            new MoveTo(start.getX() + t.getMoveOrigin().getX(), start.getY()+t.getMoveOrigin().getY()), 
            new LineTo(end.getX() + t.getMoveOrigin().getX(), end.getY()+t.getMoveOrigin().getY()))); 
        animation.getPathAnimation().setOnFinished(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent event) {
                panCanvas.getChildren().remove(animation.getPolygon());
                if(core.movePiece(pieceToMove, coordEnd))
                    handleEndGame();
                else{
                	if (core.getMode() != Consts.PVP)
	                	while(core.getCurrentPlayer() != (core.getMode() == Consts.PVAI ? Consts.PLAYER1 : Consts.PLAYER2)){
	                		core.playAI();
	                	}
                    handleResize(coordEnd);
                    resetPiece();
                    freeze.setValue(false);
                    initButtonByInventory();
                }
            }
        });
        animation.play();

    }
   
    public void handleEndGame(){
        freeze.setValue(true);
        endOfGame = true;
        highlighted.setListTohighlight(null);
        initButtonByInventory();
        Dialog dialog = new Alert(Alert.AlertType.INFORMATION);
        dialog.setTitle("Fin de partie !");
        System.err.println(core.getStatus() + "statut de fin");
        switch(core.getStatus()){
            case Consts.WIN_TEAM1 :
                namePlayer1.setText(core.getPlayers()[0].getName() + " a perdu !");
                namePlayer2.setText(core.getPlayers()[1].getName() + " a gagné !");
                dialog.setContentText("Le joueur noir remporte la victoire !");
                break;
            case Consts.WIN_TEAM2 :
                namePlayer1.setText(core.getPlayers()[0].getName() + " a gagné !");
                namePlayer2.setText(core.getPlayers()[1].getName() + " a perdu !");
                dialog.setContentText("Le joueur blanc remporte la victoire");
                break;
            case Consts.NUL:
                namePlayer1.setText(core.getPlayers()[0].getName() + " : match nul !");
                namePlayer2.setText(core.getPlayers()[1].getName() + " : match nul !");
                dialog.setContentText("Match nul");
                break;
            default :
                dialog.setContentText("Ne devrait pas arriver !");
                break;
        }
        dialog.show();
    }
   
    /*******************/
    
    public void handleSaveGame() throws IOException{
       
        Dialog popup = new Dialog();
        popup.setTitle("Sauvegarder la partie");
        ButtonType save = new ButtonType("Sauvegarder",ButtonBar.ButtonData.LEFT);
        ButtonType cancel = new ButtonType("Annuler",ButtonBar.ButtonData.RIGHT);
        popup.getDialogPane().getButtonTypes().addAll(save,cancel);
       
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 150, 10, 10));
        TextField saveName = new TextField();
        saveName.setPromptText("Sauvegarde");
       
        popup.getDialogPane().setContent(grid);
        grid.add(new Label("Nom de la sauvegarde :"), 0, 0);
        grid.add(saveName, 1, 0);
       
        Optional<ButtonType> result = popup.showAndWait();
        if(result.get().getButtonData() == ButtonBar.ButtonData.LEFT){
            String saveString = saveName.getText();
            if(saveString.equals("")){
                if (core.getMode() == Consts.PVP)
                        saveString = core.getPlayers()[Consts.PLAYER1].getName() + "-" + core.getPlayers()[Consts.PLAYER2].getName() + "-turn" + core.getTurn();
                    else
                        saveString = core.getPlayers()[Consts.PLAYER1].getName() + "-AI_"
    			+ (core.getDifficulty() == Consts.EASY ? "EASY" : core.getDifficulty() == Consts.MEDIUM ? "MEDIUM" : "HARD")
			+ "-turn" + core.getTurn();
            }
            core.save(saveString);
            takeSnapshot(saveString);
        }
        main.getPrimaryStage().requestFocus();
    }
    
   
   
    public void handleSaveAndQuitGame(int status) throws IOException{
       
        Dialog popup = new Dialog();
        popup.setTitle("Sauvegarder et quitter la partie");
        ButtonType saveAndQuit = new ButtonType("Sauvegarder et quitter",ButtonBar.ButtonData.LEFT);
        ButtonType cancel = new ButtonType("Annuler",ButtonBar.ButtonData.RIGHT);
        popup.getDialogPane().getButtonTypes().addAll(saveAndQuit,cancel);
       
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 150, 10, 10));
        TextField saveName = new TextField();
        saveName.setPromptText("Sauvegarde");
       
        popup.getDialogPane().setContent(grid);
        grid.add(new Label("Nom de la sauvegarde :"), 0, 0);
        grid.add(saveName, 1, 0);
       
        Optional<ButtonType> result = popup.showAndWait();
        if(result.get().getButtonData() == ButtonBar.ButtonData.LEFT){
            r.stop();
            String saveString = saveName.getText();
            if(saveString.equals("")){
                if (core.getMode() == Consts.PVP)
				saveString = core.getPlayers()[Consts.PLAYER1].getName() + "-" + core.getPlayers()[Consts.PLAYER2].getName() + "-turn" + core.getTurn();
			else
				saveString = core.getPlayers()[Consts.PLAYER1].getName() + "-AI_"
						+ (core.getDifficulty() == Consts.EASY ? "EASY" : core.getDifficulty() == Consts.MEDIUM ? "MEDIUM" : "HARD")
						+ "-turn" + core.getTurn();
            }
            core.save(saveString);
            takeSnapshot(saveString);
            switch(status){
                case (Consts.GO_TO_MAIN):
                        main.showMainMenu();
                        break;
                case (Consts.GO_TO_LOAD):
                        main.showLoadGameScreen();
                        break;
                case (Consts.GO_TO_GAME):
                        gameScreen();
                        break;
            }   
        }
        main.getPrimaryStage().requestFocus();
    }
   
    public void handleResize(CoordGene<Integer> coordEnd){
        CoordGene<Double> newOrigin = t.getMoveOrigin();
        if(coordEnd.getX() == 0){
            newOrigin.setX(newOrigin.getX()-Consts.SIDE_SIZE*2.25);
        }
        if(coordEnd.getY() == 0){
            newOrigin.setY(newOrigin.getY()-Consts.SIDE_SIZE*1.5);
        }
        if(coordEnd.getX() == core.getBoard().getBoard().size()-1){
            newOrigin.setX(newOrigin.getX()-Consts.SIDE_SIZE/2.25);
        }
        if(coordEnd.getY() == core.getBoard().getBoard().get(coordEnd.getX()).size()-1){
            newOrigin.setY(newOrigin.getX()-Consts.SIDE_SIZE/1.5);
        }
            t.setMoveOrigin(newOrigin);
    }
   
    public void takeSnapshot(String name) throws IOException{
        WritableImage screenshot = new WritableImage((int)main.getPrimaryStage().getScene().getWidth(),(int)main.getPrimaryStage().getScene().getHeight());
        main.getPrimaryStage().getScene().snapshot(screenshot);
        if (!Files.isDirectory(Paths.get("Hive_save_images")))
                Files.createDirectories(Paths.get("Hive_save_images"));
       
        File file = new File("Hive_save_images/"+name+".png");
        ImageIO.write(SwingFXUtils.fromFXImage(screenshot, null), "png", file);  
    }
    
    public void setMainApp(Main mainApp) {
        this.main = mainApp;   
    }
    public void setCore(Core c) {
        this.core = c;   
    }

    public void setPieceToChoose(int pieceToChoose) {
        this.pieceToChoose = pieceToChoose;
    }

    public void setPieceToMove(CoordGene<Integer> pieceToMove) {
        this.pieceToMove = pieceToMove;
    }

    public void setHighlighted(Highlighter highlighted) {
        this.highlighted = highlighted;
    }

    public boolean isFreeze() {
        return freeze.getValue();
    }
    
    private void checkHistory() {
    	if (core.hasNextState())
    		redo.setDisable(false);
    	else
    		redo.setDisable(true);
    	if (core.hasPreviousState())
    		undo.setDisable(false);
    	else
    		undo.setDisable(true);
	}
}
