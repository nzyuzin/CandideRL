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
		String message = performer.getName() + " dealt " + damage + " points of damage to " + target.getName();
		if ( target.isDead()) {
			message += " and " + target.getName() + " died.";
			Map.putItem(target.getCorpse(), target.getPosition());
			Map.removeGameCharacter(target);
		}
		else message += ". Current " + target.getName() + "'s hp is: " + target.getCurrentHP() + "/" + target.getMaxHP();
		game.GameEngine.showMessage(message);

	}
}