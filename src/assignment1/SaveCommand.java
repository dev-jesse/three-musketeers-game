package assignment1;

import javafx.scene.control.Button;


public class SaveCommand extends Button implements CommandInterface {

	private Receiver receiver;

	public SaveCommand(Receiver target) {
		receiver = target; 
	}

	public void execute() {
		receiver.save();
	}
}