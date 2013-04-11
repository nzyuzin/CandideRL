package characters.actions;

import characters.GameCharacter;
import map.Map;
import utility.*;

public final class HitGameAction extends AbstractGameAction {

	private GameCharacter target;
	
	public HitGameAction(GameCharacter performer, Position pos) {
		super(performer);
		target = Map.getGameCharacter(pos);
	}
	
	@Override
	public void execute() {
		target.takeDamage(performer.hit());
		if (target.currentHP <= 0)
			Map.removeGameCharacter(target);
	}

}
