package game.characters.actions;

import game.characters.GameCharacter;
import game.map.Map;
import game.utility.*;

public final class HitGameAction extends AbstractGameAction {

	private GameCharacter target;
	
	public HitGameAction(GameCharacter performer, Position pos) {
		super(performer);
		target = Map.getGameCharacter(pos);
	}
	
	public boolean canBeExecuted() {
		return target != null && !target.isDead() && !performer.isDead() && target.getPosition().distanceTo(performer.getPosition()) == 1;
	}
	
	public void execute() {
		int damage = performer.roleDamageDice();
		target.takeDamage(damage);
		if ( target.isDead()) {
			Map.putItem(target.getCorpse(), target.getPosition());
			Map.removeGameCharacter(target);
		}

	}
}