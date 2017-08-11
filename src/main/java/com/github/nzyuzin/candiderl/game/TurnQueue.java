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

package com.github.nzyuzin.candiderl.game;

import com.github.nzyuzin.candiderl.game.characters.GameCharacter;
import com.github.nzyuzin.candiderl.game.characters.actions.Action;

import java.util.PriorityQueue;

public class TurnQueue {

    private final PriorityQueue<Action> turnQueue;

    public TurnQueue() {
        this.turnQueue = new PriorityQueue<>();
    }

    public int getTime() {
        return this.turnQueue.peek().getExecutionTime();
    }

    public GameCharacter poll() {
        return this.turnQueue.poll().getPerformer();
    }

    public void add(GameCharacter gameCharacter) {
        turnQueue.add(gameCharacter.getAction().get());
    }

    public boolean contains(GameCharacter gameCharacter) {
        for (Action action : turnQueue) {
            if (action.getPerformer().equals(gameCharacter)) {
                return true;
            }
        }
        return false;
    }

    public void remove(GameCharacter gameCharacter) {
        this.turnQueue.removeIf(action -> action.getPerformer().equals(gameCharacter));
    }

    public boolean isEmpty() {
        return this.turnQueue.isEmpty();
    }
}
