package assignment1;

import java.io.File;
import javafx.scene.image.Image;

public class Guard implements Piece {
	private String skinPath;
	
	public Guard(String skinPath) {
		this.skinPath = skinPath;
	}
	
	@Override
	public String getSymbol() {
		// TODO Auto-generated method stub
		return "O";
	}

	@Override
	public Type getType() {
		// TODO Auto-generated method stub
		return Type.GUARD;
	}

	@Override
	public Image getImage() {
		File file = new File(this.skinPath);
		for (File f: file.listFiles()) {
			return new Image("file:" + f, 80, 80, true, true);
		} 
		return new Image("file:" + this.skinPath, 80, 80, true, true);
	}

	@Override
	public boolean canMoveOnto(Cell cell) {
		// TODO Auto-generated method stub
		return !cell.hasPiece();
	}
}