package movement;

public interface Movable {
	boolean canMove(Direction there);
	void move(Direction there);
}
