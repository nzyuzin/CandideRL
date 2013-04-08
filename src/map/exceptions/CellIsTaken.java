package map.exceptions;

import creatures.Creature;

public final class CellIsTaken extends Exception {
	private static final long serialVersionUID = 8382L;

	public CellIsTaken(Creature mob) { super("Cannot move here, the cell (" + mob.posX + ", " + mob.posY + ") is taken by " + mob); }
}
