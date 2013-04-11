package characters.actions;

import characters.GameCharacter;
import utility.interfaces.GameAction;

public abstract class AbstractGameAction implements GameAction {

	protected final GameCharacter subject;

	protected int actionPointsLeft = 0;
	
	public abstract void execute();

	public AbstractGameAction(GameCharacter subject) {
		this.subject = subject;
	}

	public int actionPointsLeft() {
		return ( this.actionPointsLeft < 0 ? 0 : this.actionPointsLeft );
	}

	public void setActionPointsLeft(int points) {
		this.actionPointsLeft = points;
	}

}