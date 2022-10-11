package assignment1;

import javafx.animation.PauseTransition;
import javafx.geometry.HPos;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Date;

public class View {

	// The view should not know the model, instead uses the controller as a intermediary
    ThreeMusketeers model;
    Stage stage;
    BorderPane borderPane;
    
    Controller control;

    Label messageLabel = new Label("");
    Label gameModeLabel = new Label("");
    BoardPanel boardPanel;
    Button undoButton, saveButton, restartButton;
    TextField saveFileNameTextField;
    Label saveFileErrorLabel;

    // must use these strings to update saveFileErrorLabel when savingh a board
    static String saveFileSuccess = "Saved board";
    static String saveFileExistsError = "Error: File already exists";
    static String saveFileNotTxtError = "Error: File must end with .txt";

    public View(Stage stage) {
        this.model = new ThreeMusketeers(this);
        this.stage = stage;
        this.control = new Controller(this, model);
        initUI();
    }

    /**
     * Initializes the UI and shows the main menu
     *
     * Contains default alignment and styles which can be modified
     */
    protected void initUI() {
        borderPane = new BorderPane();

        // DO NOT MODIFY IDs
        borderPane.setId("BorderPane");  // DO NOT MODIFY ID
        gameModeLabel.setId("GameModeLabel");  // DO NOT MODIFY ID
        messageLabel.setId("MessageLabel"); // DO NOT MODIFY ID

        var threeMusketeersLabel = new Label("Three Musketeers");

        // Default styles which can be modified

        borderPane.setStyle("-fx-background-color: #121212;");

        threeMusketeersLabel.setFont(new Font(30));
        threeMusketeersLabel.setStyle("-fx-text-fill: #e8e6e3");

        gameModeLabel.setText("");
        gameModeLabel.setFont(new Font(20));
        gameModeLabel.setStyle("-fx-text-fill: #e8e6e3");

        messageLabel.setFont(new Font(20));
        messageLabel.setStyle("-fx-text-fill: #e8e6e3");

        VBox labels = new VBox(threeMusketeersLabel, gameModeLabel);
        labels.setAlignment(Pos.TOP_CENTER);
        borderPane.setTop(labels);
        	
        //test 
        //showSkinSelect();
        
        showMainMenu();

        var scene = new Scene(borderPane, 800, 1000);
        scene.getStylesheets().add(getClass().getResource("Style.css").toExternalForm());
        stage.setScene(scene);
        stage.show();
    }
    
    
    /**
     * Updates the view to show the Main menu
     */
    protected void showMainMenu(){
        ModeInputPanel modeInputPanel = new ModeInputPanel(this, control);
        VBox vBox = new VBox(20, messageLabel, modeInputPanel);
        vBox.setAlignment(Pos.CENTER);

        borderPane.setCenter(vBox);
        borderPane.setBottom(null);
    }

    /**
     * Updates the view to show the BoardPanel and game controls
     */
    protected void showBoard() {
        boardPanel = new BoardPanel(this, model.getBoard(), control);
        undoButton = new Button("Undo move");
        undoButton.setId("UndoButton");   // DO NOT MODIFY ID
        undoButton.setPrefSize(150, 50);
        undoButton.setFont(new Font(12));
        undoButton.setStyle("-fx-background-color: #17871b; -fx-text-fill: white;");

        saveButton = new Button("Save board");
        saveButton.setId("SaveButton");  // DO NOT MODIFY ID
        saveButton.setPrefSize(150, 50);
        saveButton.setFont(new Font(12));
        saveButton.setStyle("-fx-background-color: #17871b; -fx-text-fill: white;");

        String boardName = new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss").format(new Date()) + ".txt";
        saveFileNameTextField = new TextField(boardName);
        saveFileNameTextField.setId("SaveFileNameTextField");  // DO NOT MODIFY ID
        saveFileNameTextField.setStyle("-fx-background-color: #181a1b; -fx-text-fill: white;");

        saveFileErrorLabel = new Label("");
        saveFileErrorLabel.setId("SaveFileErrorLabel");  // DO NOT MODIFY ID
        saveFileErrorLabel.setStyle("-fx-text-fill: #e8e6e3;");

        restartButton = new Button("New game");
        restartButton.setId("RestartButton");  // DO NOT MODIFY ID
        restartButton.setPrefSize(150, 50);
        restartButton.setFont(new Font(12));
        restartButton.setStyle("-fx-background-color: #17871b; -fx-text-fill: white;");
        
        // mapping to Receiver..
        saveButton.setOnAction(e -> control.saveBoard());
        restartButton.setOnAction(e -> control.restart());
        undoButton.setOnAction(e ->  control.undo());
        setUndoButton();

        GridPane controls = new GridPane();
        controls.addRow(0, undoButton, restartButton);
        controls.addRow(1, saveFileNameTextField, saveButton); 
        controls.add(saveFileErrorLabel, 0, 2, 2, 1);
        controls.setHgap(20);
        controls.setVgap(20);
        controls.setAlignment(Pos.BOTTOM_CENTER);
        GridPane.setHalignment(saveFileErrorLabel, HPos.CENTER);

        EvaluationBarContainer evalBar = new EvaluationBarContainer();

        this.model.getBoard().register(evalBar);
        this.model.getBoard().updateState();
        VBox vBox = new VBox(20, messageLabel, evalBar, boardPanel, controls);
        vBox.setAlignment(Pos.CENTER);
        borderPane.setCenter(vBox);
        
        if (!(model.getCurrentAgent().isHuman())) {
            //runMove();
        	control.runMove();
        } else {
            messageLabel.setText(String.format("[%s turn] Select a piece", model.getBoard().getTurn().getType()));
        }
    }

    /**
     * Updates the messageLabel to the given String
     * @param messageLabel String to use for the text of the messageLabel
     */
    protected void setMessageLabel(String messageLabel) {
        this.messageLabel.setText(messageLabel);
    }

    /**
     *  Enables or disables the undo button depending on if there are moves to undo
     */
    protected void setUndoButton() { 
    	if (this.control.canUndoMoves()) {
    		this.undoButton.setDisable(false);
    	}
    	else {
    		this.undoButton.setDisable(true);
    	}
    }

    /**
     * Handles setting the correct agents based on the selected GameMode and the player's piece type by
     * calling model.selectMode
     * Shows the BoardPanel once the side and mode are selected
     * @param sideType the selected Piece Type for the human player in Human vs Computer games
     */
    /**
     * Updates the view to show the side selector
     */
    //changed this from private to protected
    protected void showSideSelector() {
        VBox vBox = new VBox(20, messageLabel, new SideInputPanel(this, control));
        vBox.setAlignment(Pos.CENTER);
        borderPane.setCenter(vBox);
    }
}
