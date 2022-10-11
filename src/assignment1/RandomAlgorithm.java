package assignment1;

import java.util.List;
import java.util.Random;

public class RandomAlgorithm implements IStrategy {
	
	public RandomAlgorithm() {
		
	}
	
	public Move getMove(Board board) {
		List<Cell> possibleCells = board.getPossibleCells();
        Cell fromCell = possibleCells.get(new Random().nextInt(possibleCells.size()));

        List<Cell> possibleDestinations = board.getPossibleDestinations(fromCell);
        Cell toCell = possibleDestinations.get(new Random().nextInt(possibleDestinations.size()));

        System.out.printf("[%s (Random Agent)] Moving piece %s to %s.\n",
                board.getTurn().getType(), fromCell.getCoordinate(), toCell.getCoordinate());
        return new Move(fromCell, toCell);
	}
}
