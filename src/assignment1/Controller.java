package assignment1;

import java.io.File;

public class Controller{
	private View view;
	private ThreeMusketeers model;
	private Receiver receiver;
	
	private File currentBoardFile = new File("Boards/Starter.txt");
	ThreeMusketeers.GameMode gameMode;
	
	// we do not have the CMD Pattern files yet... placeholder code
	// private Receiver receiver;
	//
	
	public Controller(View view, ThreeMusketeers model) {
		this.view = view;
		this.model = model;
		this.receiver = new Receiver(model, view);
	}
	
    protected void runMove() { // IPR
    	if (!model.isHumanTurn()) {
    		model.runMove();
    	}
    	if (model.isHumanTurn()) {
    		Move humanMove = new Move(view.boardPanel.selectedCell,view.boardPanel.destinationCell);
    		model.move(humanMove);
    		if (!model.getBoard().isGameOver()) {
    			model.runMove();
    		}
    	}
    	if (model.getBoard().isGameOver()) {
    		if (model.getBoard().getWinner() == Type.MUSKETEER) {
    			model.getBoard().updateState(60);
    		}
    		else {
    			model.getBoard().updateState(0);
    		}
    	}
    }
	
	public void saveBoard() {
		receiver.save();
	}
	
	public void restart() {
		this.currentBoardFile = new File("Boards/Starter.txt");
		receiver.restart();
	}
	
	public void loadBoard(File file) {
		model.getBoard().loadBoard(file);
	}
	
	protected void setCurrentBoardFile(File file) {
		this.currentBoardFile = file;
	}
	
	protected File getCurrentBoardFile() {
		return this.currentBoardFile;
	}
	
	public void loadSkin(String pathFile) {
		model.getBoard().setSkin(pathFile);
		model.getBoard().loadBoard(this.currentBoardFile);
	}
	
	// helper method for undo
	public boolean canUndoMoves() {
		return this.model.getMovesSize() > 0;
	}
	
	public void undo() {
		receiver.undo();
	}
	
    protected void setGameMode(ThreeMusketeers.GameMode mode) {
    	this.gameMode = mode;
    	view.gameModeLabel.setText(this.gameMode.getGameModeLabel());
    	switch (mode) {
	    	case Human -> {
	    		this.model.selectMode(this.gameMode, Type.MUSKETEER);
	    		view.showBoard();
	    	}
	    	case HumanRandom -> {
	    		view.showSideSelector();
	    	}
	    	case HumanGreedy -> {
	    		view.showSideSelector();
	    	}
    	}
    }
    
    protected void setSide(Type sideType) { 
    	model.selectMode(gameMode, sideType);
    	view.showBoard();
    }
}