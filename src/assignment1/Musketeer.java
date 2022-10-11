package assignment1;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.io.File;

public class Musketeer implements Piece {
	private String skinPath;
	
	public Musketeer(String skinPath) {
		this.skinPath = skinPath;
	}
	
	@Override
	public String getSymbol() {
		return "X";
	}

	@Override
	public Type getType() {
		// TODO Auto-generated method stub
		return Type.MUSKETEER;
	}

	@Override
	public Image getImage() {
		// TODO Auto-generated method stub
		Integer counter = 0;
		File file = new File(this.skinPath);
		for (File f: file.listFiles()) {
			if (counter == 1) {
				return new Image("file:" + f, 80, 80, true, true);
			}
			counter += 1;
		}
		return new Image("file:" + this.skinPath, 80, 80, true, true);
	}

	@Override
	public boolean canMoveOnto(Cell cell) {
		// TODO Auto-generated method stub
		return cell.hasPiece() && cell.getPiece().getType() == Type.GUARD; // don't know if we keep this
	}
	
}