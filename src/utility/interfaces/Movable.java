package utility.interfaces;

import utility.Direction;

public interface Movable {
	boolean canMove(Direction there);
	void move(Direction there);
}
