package game.utility.exceptions.map;

import game.characters.GameCharacter;

public final class CellIsTaken extends Exception {
	private static final long serialVersionUID = 8382L;

	public CellIsTaken(GameCharacter mob) { super("Cannot move here, the cell " + mob.getPosition() + " is taken by " + mob); }
}
