package assignment1;

import javafx.scene.control.Button;


public class UndoCommand extends Button implements CommandInterface {

	private Receiver receiver;

	public UndoCommand(Receiver target) {
		receiver = target; 
	}

	public void execute() {
		receiver.undo();
	}

}