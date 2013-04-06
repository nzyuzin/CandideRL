package movement;

public interface Movable {
	public boolean canMoveHere(Direction where);
	public boolean moveHere(Direction where);
}
