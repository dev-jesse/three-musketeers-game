package assignment1;

public class Receiver {
	
	ThreeMusketeers model;
	View view;
	
	public Receiver(ThreeMusketeers m, View v) {
		this.model = m;
		this.view = v;
	}
	
	public void undo() {
		this.model.undoMove();
		if (!model.isHumanTurn()) {
			model.runMove();
		}
		view.boardPanel.updateCells();
		view.setUndoButton();
	}

	public void save() {
		this.model.saveBoard();
	}

	public void restart() {
		this.model.restart();
		this.view.initUI();
	}
}