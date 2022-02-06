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
import com.github.nzyuzin.candiderl.game.map.Map;
import com.google.common.base.Preconditions;
import com.google.common.base.Strings;
import com.google.common.collect.Lists;

import java.io.Serializable;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;

public final class GameState implements Serializable {

    private GameFactories gameFactories;

    private Player player;
    private int time;

    private final Deque<String> gameMessages;
    private final List<Map> dungeon;

    public GameState(final GameFactories gameFactories) {
        this.gameFactories = gameFactories;
        this.time = 0;
        this.gameMessages = new ArrayDeque<>(10);
        this.dungeon = new ArrayList<>();
    }

    public GameFactories getGameFactories() {
        return gameFactories;
    }

    public int getNextMapId() {
        return dungeon.size();
    }

    public void addMap(Map map) {
        Preconditions.checkNotNull(map);
        Preconditions.checkArgument(map.getId() == getNextMapId());
        dungeon.add(map);
    }

    public Map getMap(int id) {
        Preconditions.checkArgument(id >= 0 && id < dungeon.size(),
                "Map id is out of bounds: id = " + id + " not in [" + 0 + ", " + (dungeon.size() - 1) + ")");
        return dungeon.get(id);
    }

    public void addMessage(String msg) {
        if (!Strings.isNullOrEmpty(msg)) {
            gameMessages.addFirst(msg);
        }
    }

    public List<String> getMessages() {
        return Lists.newArrayList(gameMessages);
    }

    public void setTime(final int time) {
        Preconditions.checkArgument(time >= this.time);
        this.time = time;
    }

    public int getCurrentTime() {
        return time;
    }

    public int getCurrentTurn() {
        return time / 100;
    }

    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }
}
