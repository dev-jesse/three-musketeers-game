package assignment1;

public class PieceCreator {
	private String skinPath;

	public PieceCreator() {
		this.skinPath = "Images/Default";
	}
	
	public PieceCreator(String skinPath) {
		this.skinPath = skinPath;
	}
	
	protected void setSkin(String pathFile) {
		this.skinPath = pathFile;
	}
	
	public Piece createPiece(Object symbol) {
		if (symbol.equals("X")) {
			return new Musketeer(this.skinPath);
		}
		else if (symbol.equals("O")){
			return new Guard(this.skinPath);
		}
		else {
			return null;
		}
	}
}
