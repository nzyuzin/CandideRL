/*
 *  This file is part of CandideRL.
 *
 *  CandideRL is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  CandideRL is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with CandideRL.  If not, see <http://www.gnu.org/licenses/>.
 */

package game.ai;

import game.characters.*;
import game.characters.actions.HitGameAction;
import game.characters.actions.MovementGameAction;

import game.utility.Direction;

public class ArtificialIntelligence {

	private static GameCharacter target = null;
	private static int distanceLimit;
	private static PathFinder path;

	public ArtificialIntelligence() { }

	public static void init(GameCharacter t, int distance) {
		distanceLimit = distance;
		target = t;
	}

	public static void chooseAction(GameCharacter mob) {
		if (target.isDead()) return;
		if (target.getPosition().distanceTo(mob.getPosition()) < 2)
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

		HitGameAction hit = new HitGameAction(mob, Direction
				.applyDirection(mob.getPosition(), there));

		if (hit.canBeExecuted()) mob.addAction(hit);
	}

	private static void moveToTarget(GameCharacter mob) {
		if (mob.getPosition().distanceTo(target.getPosition()) > distanceLimit)
			return;

		if (target.getPositionOnMap().getLastPos() != target.getPosition())
			path = new PathFinder(target.getPosition(), distanceLimit, target.getPositionOnMap().getMap());
		mob.move(path.chooseQuickestWay(mob.getPosition()));
	}

}
