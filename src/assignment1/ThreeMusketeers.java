package assignment1;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import javafx.animation.PauseTransition;
import javafx.util.Duration;

public class ThreeMusketeers {
	private File boardFile;
	private Board board;
	private Agent musketeerAgent;
	private Agent guardAgent;

	// changes
	private View view;
	/*
	 * Scanner becomes obsolete, no longer uses system console for user input
	 * private final Scanner scanner = new Scanner(System.in);
	 */

	private List<MomentoInterface> history = new ArrayList<>();

	/**
	 * Default constructor to load Starter board
	 */
	public ThreeMusketeers(View view) {
		this(new File("Boards/Starter.txt"));
		this.view = view;
	}

	/**
	 * Constructor to load custom board
	 *
	 * @param boardFile filepath of custom board
	 */
	public ThreeMusketeers(File boardFile) {
		this.board = new Board(boardFile);
		this.boardFile = boardFile;
		this.history = new ArrayList<>();
	}


	/**
	 * Restart the game
	 */
	public void restart() {
		board = new Board(this.boardFile);
		history = new ArrayList<>();
	}

    /**
     * Mode selector sets the correct agents based on the given GameMode
     *
     * @param mode the selected GameMode
     */
    protected void selectMode(GameMode mode, Type sideType) {
    	IStrategy human = new HumanAlgorithm();
    	IStrategy random = new RandomAlgorithm();
    	IStrategy greedy = new GreedyAlgorithm();
        switch (mode) {
            case Human -> {
                musketeerAgent = new Agent(board, human);
                guardAgent = new Agent(board, human);
            }
            case HumanRandom -> {
                musketeerAgent = sideType.equals(Type.MUSKETEER) ? new Agent(board, human) : new Agent(board, random);
                guardAgent = sideType.equals(Type.GUARD) ? new Agent(board, human) : new Agent(board, random);
            }
            case HumanGreedy -> {
                musketeerAgent = sideType.equals(Type.MUSKETEER) ? new Agent(board, human) : new Agent(board, greedy);
                guardAgent = sideType.equals(Type.GUARD) ? new Agent(board, human) : new Agent(board, greedy);
            }
        }
    }


	public Board getBoard() {
		return board;
	}

	public int getMovesSize() {
		return history.size();
	}

	public File getBoardFile() {
		return boardFile;
	}


	public Agent getMusketeerAgent() {
		return musketeerAgent;
	}


	public Agent getGuardAgent() {
		return guardAgent;
	}

   protected Agent getCurrentAgent() {
    	return board.getTurn() == Type.MUSKETEER ? musketeerAgent : guardAgent;
   }

	protected Move move(final Agent agent) {
		final Move move = agent.getMove();
		this.move(move);
		return move;
	}

	/**
	 * Gets a move from the given agent, adds a copy of the move using the copy
	 * constructor to the moves stack, and does the move on the board.
	 *
	 * @param agent Agent to get the move from
	 * @return Move that the agent is doing
	 */
	protected void move(Move move) {
		history.add(board.save());
		board.move(move);
	}

	protected void runMove() { // IPR
		if (!this.isHumanTurn()) {
			String computerTurn = String.format("[%s turn] calculating move ...", this.getBoard().getTurn());
			view.messageLabel.setText(computerTurn);
			PauseTransition pause = new PauseTransition(Duration.seconds(0.2));
			pause.setOnFinished(e -> {

				Move move = this.getCurrentAgent().getMove();
				this.move(move);
				view.boardPanel.updateCells();
				if (!this.getBoard().isGameOver()) {
					String humanTurn = String.format("[%s turn] Select a move", this.getBoard().getTurn());
					view.messageLabel.setText(humanTurn);
				}
				else {
					if (this.getBoard().getWinner() == Type.MUSKETEER) {
						this.getBoard().updateState(60);
					} else {
						this.getBoard().updateState(0);
					}
				}
			});
			pause.play();
			// human turn now
		}
		if (this.getBoard().isGameOver()) {
			view.boardPanel.updateCells();
			if (this.getBoard().getWinner() == Type.MUSKETEER) {
				this.getBoard().updateState(60);
			} else {
				this.getBoard().updateState(0);
			}
		}
	}

	/**
	 * Removes a move from the top of the moves stack and undoes the move on the
	 * board.
	 */
	protected void undoMove() {
		if (history.size() == 0) {
			System.out.println("No moves to undo.");
			return;
		}
		if (history.size() == 1 || isHumansPlaying()) {
			board.restore(history.remove(history.size() - 1));
		} else {
			board.restore(history.remove(history.size() - 1));
			board.restore(history.remove(history.size() - 1));
		}
		System.out.println("Undid the previous move.");
	}

	/**
	 * Returns whether both sides are human players
	 *
	 * @return True if both sides are Human, False if one of the sides is a computer
	 */
	public boolean isHumansPlaying() {
		return musketeerAgent.isHuman() && guardAgent.isHuman();
	}


    public boolean isHumanTurn() {
        Type currentTurnType = board.getTurn();
        return (currentTurnType.equals(Type.MUSKETEER) && musketeerAgent.isHuman())
                || (currentTurnType.equals(Type.GUARD) && guardAgent.isHuman());
    }

	/**
	 * Possible game modes
	 */
	public enum GameMode {
		Human("Human vs Human"), HumanRandom("Human vs Computer (Random)"), HumanGreedy("Human vs Computer (Greedy)");

		public String getGameModeLabel() {
			return gameModeLabel;
		}

		private final String gameModeLabel;

		GameMode(final String gameModeLabel) {
			this.gameModeLabel = gameModeLabel;
		}
	}

	// moved setGameMode to Controller
	/*
	 * protected void setGameMode(ThreeMusketeers.GameMode mode) { view.gameMode =
	 * mode; view.gameModeLabel.setText(view.gameMode.getGameModeLabel()); switch
	 * (mode) { case Human -> { this.selectMode(view.gameMode,
	 * Piece.Type.MUSKETEER); view.showBoard(); } case HumanRandom -> {
	 * view.showSideSelector(); } case HumanGreedy -> { view.showSideSelector(); } }
	 * }
	 */

	/**
	 * Handler for the Save Board button Saves the current board state to a text
	 * file Uses saveFileNameTextField to get user input for the name of the file
	 * (must end with ".txt") Contains error handling to make sure the file does not
	 * already exist and the input ends with ".txt" Updates saveFileErrorLabel with
	 * the appropriate message
	 *
	 * Must use saveFileSuccess, saveFileExistsError, or saveFileNotTxtError to set
	 * as the text of saveFileErrorLabel
	 */
	protected void saveBoard() { // delete
		File directory = new File("boards");
		// If the txt name contains .txt
		if (this.view.saveFileNameTextField.getLength() <= 4) {
			this.view.saveFileErrorLabel.setText(View.saveFileNotTxtError);
		} else if (!this.view.saveFileNameTextField.getText().substring(view.saveFileNameTextField.getLength() - 4)
				.equals(".txt")) {
			this.view.saveFileErrorLabel.setText(View.saveFileNotTxtError);
		}
		// If the name already exists
		else if (this.saveBoardExistsHelper(directory)) {
			this.view.saveFileErrorLabel.setText(View.saveFileExistsError);
		}
		// Saves the file
		else {
			File file = new File(directory, this.view.saveFileNameTextField.getText());
			this.getBoard().saveBoard(file);
			this.view.saveFileErrorLabel.setText(View.saveFileSuccess);
		}
	}

	/**
	 * Helper for saveBoard
	 * 
	 * @param The directory that contains the board text files
	 * @return True if there's already a text file with the same name false
	 *         otherwise
	 */
	protected boolean saveBoardExistsHelper(File directory) {// shouldn't be in view
		for (File file : directory.listFiles()) {
			if (file.getName().equals(this.view.saveFileNameTextField.getText())) {
				return true;
			}
		}
		return false;
	}

	protected void setBoard(File boardFile) {
		this.board.loadBoard(boardFile);
		this.boardFile = boardFile;
	}

}