/*
 * This file is part of CandideRL.
 *
 * CandideRL is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * CandideRL is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with CandideRL.  If not, see <http://www.gnu.org/licenses/>.
 */

package game.characters;

import game.GameObject;
import game.characters.actions.GameAction;
import game.items.GameItem;
import game.utility.Position;
import game.utility.PositionOnMap;
import game.utility.interfaces.Damageable;
import game.utility.interfaces.Movable;
import game.utility.interfaces.Visible;

public interface GameCharacter extends GameObject, Movable, Damageable, Visible {

    public boolean hasAction();

    public void addAction(GameAction action);

    public void removeCurrentAction();

    public boolean isDead();

    public Position getPosition();

    public PositionOnMap getPositionOnMap();

    public void setPositionOnMap(PositionOnMap position);

    public int getCurrentHP();

    public int getMaxHP();

    public boolean canPerformAction();

    public void performAction();

    public void breakActionQueue();

    public void hit(Position pos);

    public int roleDamageDice();

    public GameItem getCorpse();
}
