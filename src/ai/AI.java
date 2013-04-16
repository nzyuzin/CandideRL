package ai;
import characters.*;
import utility.DirectionProcessor;

public class AI {
	
	private Player player = null;
	private int[][] distanceToPlayer = null;
	
	public AI(Player player){
		this.player = player;
	}
	
	public void chooseAction(NPC mob) {
		if ( player.isDead()) return;
		if ( player.position.distanceTo(mob.position) == 1 )
			mob.hit(player.position);
		else {
			moveToPlayer(mob);
		}
	}
	
	private void moveToPlayer(NPC mob) {
		mob.move(DirectionProcessor.getDirectionFromPositions(mob.position, player.position));
	}
	
}
