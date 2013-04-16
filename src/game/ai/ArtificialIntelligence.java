package game.ai;
import game.characters.*;
import game.utility.DirectionProcessor;

public class ArtificialIntelligence {
	
	private Player player = null;
	private int[][] distanceToPlayer = null;
	
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
	
	private void moveToPlayer(NPC mob) {
		mob.move(DirectionProcessor.getDirectionFromPositions(mob.getPosition(), player.getPosition()));
	}
	
}
