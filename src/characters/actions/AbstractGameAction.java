package characters.actions;

import characters.GameCharacter;
import utility.interfaces.GameAction;

// GameActions connect GameCharacters and classes from another packages,
// such as Map, so GameCharacters will only operate with highly abstract methods.

public abstract class AbstractGameAction implements GameAction {

	protected final GameCharacter performer; // The one who performs an action

	protected int actionPointsLeft = 0;  // Not implemented yet.
	
	/* Perform an action. Action is not supposed to used after doing this method once. */
	public abstract void execute();

	public AbstractGameAction(GameCharacter subject) {
		this.performer = subject;
	}

	public int actionPointsLeft() {  // Not implemented yet.
		return ( this.actionPointsLeft < 0 ? 0 : this.actionPointsLeft );
	}

	public void setActionPointsLeft(int points) {  // Not implemented yet.
		this.actionPointsLeft = points;
	}

}