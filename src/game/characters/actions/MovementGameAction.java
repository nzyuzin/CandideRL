package game.characters.actions;

import game.characters.GameCharacter;
import game.map.Map;
import game.utility.*;

public final class MovementGameAction extends AbstractGameAction {
	
	private final Position position;

	public MovementGameAction(GameCharacter subject, Direction there) {
		super(subject);
		position = DirectionProcessor.applyDirectionToPosition(subject.getPosition(), there);
		actionPointsLeft = Map.getPassageCost(position);
	}
	
	public boolean canBeExecuted() {
		return !performer.isDead() && performer.getPosition().distanceTo(position) < 2 && Map.isCellPassable(position) && !Map.someoneHere(position);
	}
	
	public void execute() {
		Map.moveGameCharacter(performer, position);
	}
}