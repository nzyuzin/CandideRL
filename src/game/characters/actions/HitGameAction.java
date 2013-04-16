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
	
	@Override
	public void execute() {
		target.takeDamage(performer.roleDamageDice());
		if ( target.isDead()) {
			Map.putItem(target.getCorpse(), target.getPosition());
			Map.removeGameCharacter(target);
		}

	}
}