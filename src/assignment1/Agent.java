package assignment1;

public class Agent {

    protected Board board;
    private IStrategy strategy;
    
    /**
     * An Agent that can play ThreeMusketeers
     * @param board a Board the Agent can play on
     */
    public Agent(Board board, IStrategy algorithm){
        this.board = board;
        this.strategy = algorithm;
    }
    
     /**
     * Gets a valid move that the Agent can perform on the Board
     * @return a valid Move that the Agent can do
     */
    public Move getMove() {
    	return strategy.getMove(board);
    }
    
    public boolean isHuman() {
    	return this.strategy instanceof HumanAlgorithm;
    }
    
    public boolean isRandom() {
    	return this.strategy instanceof RandomAlgorithm;
    }
    
    public boolean isGreedy() {
    	return this.strategy instanceof GreedyAlgorithm;
    }
    // -> make agent, need board as parameter
    // agent.getMove() (does not need to pass in board)
    // 
}
