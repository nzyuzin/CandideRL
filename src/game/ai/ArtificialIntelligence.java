package game.ai;

import game.characters.*;
import game.characters.actions.HitGameAction;
import game.characters.actions.MovementGameAction;

import game.utility.Direction;
import game.utility.DirectionProcessor;
import game.utility.Position;

public class ArtificialIntelligence {
	
	private static GameCharacter target = null;
	private static Position oldTargetPosition = null;
	private static int distanceLimit;
	
	public ArtificialIntelligence(){ }
	
	public static void init(GameCharacter t, int distance) {
		distanceLimit = distance;
		target = t;
	}
	
	public static void chooseAction(GameCharacter mob) {
		if ( target.isDead()) return;
		if ( target.getPosition().distanceTo(mob.getPosition()) == 1 )
			mob.hit(target.getPosition());
		else {
			moveToTarget(mob);
		}
	}
	
	public static void chooseActionInDirection(GameCharacter mob, Direction there) {
		MovementGameAction move = new MovementGameAction(mob, there);
		
		if ( move.canBeExecuted() ) {
			mob.addAction(move);
			return;
		}
		
		HitGameAction hit = new HitGameAction(mob, DirectionProcessor.applyDirectionToPosition(mob.getPosition(), there));
		
		if ( hit.canBeExecuted() ) mob.addAction(hit);
	}
	
	private static void moveToTarget(GameCharacter mob) {
		if ( mob.getPosition().distanceTo(target.getPosition()) > distanceLimit ) return;
		
		if ( oldTargetPosition != target.getPosition() ) {
			PathFinder.init(target.getPosition(), distanceLimit);
			oldTargetPosition = target.getPosition();
		}
		mob.move(PathFinder.chooseQuickestWay(mob.getPosition()));
	}
	
}
