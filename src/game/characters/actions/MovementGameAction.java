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
		return Map.isCellPassable(position) && performer.getPosition().distanceTo(position) == 1;
	}
	
	public void execute() {
		if (!Map.someoneHere(position)) {
			if (Map.isCellPassable(position))
				Map.moveGameCharacter(performer, position);
		}
		else {
			performer.removeCurrentAction();
			performer.addAction(new HitGameAction(performer, position));
			performer.performAction();
		}
	}
}
