package assignment1;

import javafx.scene.control.Button;

public class RestartCommand extends Button implements CommandInterface {

	private Receiver receiver;

	public RestartCommand(Receiver target) {
		receiver = target; 
	}

	public void execute() {
		receiver.restart();
	}
}