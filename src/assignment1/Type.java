package assignment1;

public enum Type {
	/**
     * All possible Piece types
     */
    MUSKETEER("MUSKETEER"),
    GUARD("GUARD");

    private final String type;

    Type(final String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }
}
