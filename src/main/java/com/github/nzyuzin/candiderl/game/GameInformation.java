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

import com.github.nzyuzin.candiderl.game.characters.Player;
import com.google.common.collect.Lists;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.List;

public final class GameInformation {

    private int currentTurn;
    private int depth;
    private final Deque<String> gameMessages;
    private final Player player;

    public GameInformation(final Player player) {
        this.currentTurn = 0;
        this.depth = 1;
        this.gameMessages = new ArrayDeque<>(10);
        this.player = player;
    }

    public void addMessage(String msg) {
        gameMessages.addFirst(msg);
    }

    public List<String> getMessages() {
        return Lists.newArrayList(gameMessages);
    }

    public void incrementTurn() {
        currentTurn++;
    }

    public int getCurrentTurn() {
        return currentTurn;
    }

    public void incrementDepth() {
        depth++;
    }

    public void decrementDepth() {
        depth--;
    }

    public int getDepth() {
        return depth;
    }

    public Player getPlayer() {
        return player;
    }
}
