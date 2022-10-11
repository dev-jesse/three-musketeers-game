package assignment1;

import javafx.scene.image.Image;

public interface Piece {
	public String getSymbol();
	public Type getType();
	public Image getImage();
	public boolean canMoveOnto(Cell cell);
}