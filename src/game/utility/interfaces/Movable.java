package game.utility.interfaces;

import game.utility.Direction;
import game.utility.Position;

public interface Movable {
	void move(Direction there);
	Position getPosition();
	void setPosition(Position position);
}
