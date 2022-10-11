package assignment1;


public class ConcreteMomento implements MomentoInterface {

	private final Cell[][] state;
	private Type turn;
	private Type winner;
	// will definitely have to do more re-factoring after factory + builder method...
	public ConcreteMomento(Cell[][] board, Type turn, Type winner) {
		this.state = board;
		this.turn = turn;
		this.winner = winner;
	}

	public Cell[][] getState(){
		return this.state;
	}
	
	public Type getWinner(){
		return this.winner;
	}
	
	public Type getTurn(){
		return this.turn;
	}
}
