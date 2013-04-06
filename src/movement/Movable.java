package movement;

public interface Movable {
	public boolean canMove(Direction there);
	public void move(Direction there);
}
