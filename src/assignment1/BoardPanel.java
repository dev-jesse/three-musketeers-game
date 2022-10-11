package assignment1;

import java.util.List;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;

public class BoardPanel extends GridPane implements EventHandler<ActionEvent> {

    private final View view;
    private final Board board;
    protected Cell selectedCell;
    protected Cell destinationCell; 
    
    private final Controller control;
    
    /**
     * Constructs a new GridPane that contains a Cell for each position in the board
     *
     * Contains default alignment and styles which can be modified
     * @param view
     * @param board
     */
    public BoardPanel(View view, Board board, Controller control) {
        this.view = view;
        this.board = board;
        this.control = control;
        
        // Can modify styling
        this.setAlignment(Pos.CENTER);
        this.setStyle("-fx-background-color: #181a1b;");
        int size = 550;
        this.setPrefSize(size, size);
        this.setMinSize(size, size);
        this.setMaxSize(size, size);

        setupBoard();
        updateCells();
    }

    /**
     * Setup the BoardPanel with Cells
     */
    protected void setupBoard(){
    	for (int i = 0; i < board.size; i++) {
    		for (int j = 0; j < board.size; j++) {
    			Cell c = board.getCell(new Coordinate(i, j));
    			c.setOnAction(this);
    			if (c.getPiece() != null) {
    				c.setGraphic(new ImageView(c.getPiece().getImage()));
    				this.add(c, j, i);
    			}else {
    				this.add(c, j, i);
    			}
    		}
    	}
    }
    /**
     * Updates the BoardPanel to represent the board with the latest information
     *
     * If it's a computer move: disable all cells and disable all game controls in view
     *
     * If it's a human player turn and they are picking a piece to move:
     *      - disable all cells
     *      - enable cells containing valid pieces that the player can move
     * If it's a human player turn and they have picked a piece to move:
     *      - disable all cells
     *      - enable cells containing other valid pieces the player can move
     *      - enable cells containing the possible destinations for the currently selected piece
     *
     * If the game is over:
     *      - update view.messageLabel with the winner ('MUSKETEER' or 'GUARD')
     *      - disable all cells
     */
    protected void updateCells(){ // IPR
    	if (board.isGameOver()) {
    		enableControls();
    		view.setUndoButton();
    		view.messageLabel.setText("Winner: " + view.model.getBoard().getWinner().toString());
    		disableCells();
    		setWinnerColors();
    	}else {
    		if (!view.model.isHumanTurn()) {
    			String computerTurn = String.format("[%s turn] calculating move ...",view.model.getBoard().getTurn());
        		view.messageLabel.setText(computerTurn);
        		disableControls();
        		disableCells();
        	}
        	else if (view.model.isHumanTurn() && selectedCell == null) {
        		String humanTurn = String.format("[%s turn] Select a piece to move",view.model.getBoard().getTurn());
        		view.messageLabel.setText(humanTurn);
        		enableControls();
        		disableCells();
        		List<Cell> possibleCells = view.model.getBoard().getPossibleCells();
        		
        		for (Cell cell: possibleCells) {
        			cell.setDisable(false);
        			cell.setOptionsColor();
        		}
        		
        	}
        	else if (view.model.isHumanTurn() && selectedCell != null) {
        		String humanTurn = String.format("[%s turn] Select a destination to move to",view.model.getBoard().getTurn());
        		view.messageLabel.setText(humanTurn);
        		enableControls();
        		disableCells();
        		List<Cell> possibleCells = view.model.getBoard().getPossibleCells();
        		List<Cell> pdest = view.model.getBoard().getPossibleDestinations(selectedCell);
        		for (Cell cell: possibleCells) {
        			cell.setDisable(false);
        			cell.setOptionsColor();
        		}
        		
        		for (Cell cell: pdest) {
        			cell.setDisable(false);
        			cell.setOptionsColor();
        		}
        		selectedCell.setAgentFromColor();
        	}
    	}
    }
    
    protected void enableControls() {
    	if (view.saveButton != null) {
    		view.saveButton.setDisable(false);
    	}
    	if (view.undoButton != null) {
    		view.undoButton.setDisable(false);
    	}
    	if (view.restartButton != null) {
    		view.restartButton.setDisable(false);
    	}
    }


	/**
     * Sets the colors to the cells respective to who the winner is
     */
    protected void setWinnerColors() {
    	for (Cell cell: board.getAllCells()) {
			if (cell.hasPiece()) {
    			if (board.getWinner() == Type.MUSKETEER) {
    				if (cell.getPiece().getType() == Type.MUSKETEER) {
    					cell.setWinColor();
    				}
    				else {
    					cell.setLossColor();
    				}
    			}
    			else {
    				if (cell.getPiece().getType() == Type.GUARD) {
    					cell.setWinColor();
    				}
    				else {
    					cell.setLossColor();
    				}
    			}
			}
		}
    }
    
    /**
     * Disables every cell on the board
     */
    protected void disableCells() {
    	for (Cell cell: board.getAllCells()){
    		cell.setDisable(true);
    		cell.setDefaultColor();
    	}
    }
    
    /**
     * Disables game controls (Undo move, New game, Save board)
     */
    protected void disableControls() {
    	if (view.saveButton != null) {
    		view.saveButton.setDisable(true);	
    	}
    	if (view.undoButton != null) {
    		view.undoButton.setDisable(true);
    	}
    	if (view.restartButton != null) {
    		view.restartButton.setDisable(true);
    	}
    }
    
    /**
     * Handles Cell clicks and updates the board accordingly
     * When a Cell gets clicked the following must be handled:
     *  - If it's a valid piece that the player can move, select the piece and update the board
     *  - If it's a destination for a selected piece to move, perform the move and update the board
     * @param actionEvent
     */
    @Override
    public void handle(ActionEvent actionEvent) { 
    	Cell current = (Cell) actionEvent.getSource();
    	if (board.getPossibleCells().contains(current)) {
    		this.selectedCell = current;
    		updateCells();
    	}else if (board.getPossibleDestinations(this.selectedCell).contains(current)) {
    		this.destinationCell = current;
    		control.runMove();
    		this.selectedCell = null;
			this.destinationCell = null;
			updateCells();
    	}
    }
}
