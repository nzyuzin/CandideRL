package movement;

public interface Movable {
	public boolean canMoveThere(Direction where);
	public void moveThere(Direction where);
}
