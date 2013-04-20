package game.ai;

import game.characters.*;
import game.characters.actions.HitGameAction;
import game.characters.actions.MovementGameAction;
import game.utility.Direction;
import game.utility.DirectionProcessor;

public class ArtificialIntelligence {
	
	private Player player = null;
	
	public ArtificialIntelligence(Player player){
		this.player = player;
	}
	
	public void chooseAction(NPC mob) {
		if ( player.isDead()) return;
		if ( player.getPosition().distanceTo(mob.getPosition()) == 1 )
			mob.hit(player.getPosition());
		else {
			moveToPlayer(mob);
		}
	}
	
	public void chooseActionInDirection(GameCharacter mob, Direction there) {
		MovementGameAction move = new MovementGameAction(mob, there);
		if (move.canBeExecuted()) {
			mob.addAction(move);
			return;
		}
		HitGameAction hit = new HitGameAction(mob, DirectionProcessor.applyDirectionToPosition(mob.getPosition(), there));
		if (hit.canBeExecuted()) mob.addAction(hit);
	}
	
	private void moveToPlayer(NPC mob) {
		PathFinder.init(player.getPosition());
		mob.move(PathFinder.chooseQuickestWay(mob.getPosition()));
		
		//mob.move(DirectionProcessor.getDirectionFromPositions(mob.getPosition(), player.getPosition()));
	}
	
}
