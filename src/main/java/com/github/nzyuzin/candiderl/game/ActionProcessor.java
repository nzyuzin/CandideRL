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

import com.github.nzyuzin.candiderl.game.ai.NpcController;
import com.github.nzyuzin.candiderl.game.characters.GameCharacter;
import com.github.nzyuzin.candiderl.game.characters.Npc;
import com.github.nzyuzin.candiderl.game.characters.Player;
import com.github.nzyuzin.candiderl.game.characters.actions.ActionResult;
import com.github.nzyuzin.candiderl.game.events.Event;
import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.ListIterator;

public class ActionProcessor {

    private static final Logger log = LoggerFactory.getLogger(ActionProcessor.class);

    private final GameInformation gameInformation;
    private final NpcController npcController;
    private final List<Event> events;
    private final TurnQueue turnQueue;

    public ActionProcessor(GameInformation gameInformation) {
        this.gameInformation = gameInformation;
        int npcOperationalRange = 20; // arbitrary for now
        this.npcController = new NpcController(gameInformation.getPlayer(), npcOperationalRange, gameInformation);
        this.events = Lists.newArrayList();
        this.turnQueue = new TurnQueue();
    }

    public void scheduleActions() {
        final Player player = gameInformation.getPlayer();
        turnQueue.add(player);
        for (final GameCharacter character : player.getMap().getCharacters()) {
            if (character instanceof Npc && !character.hasAction()) {
                scheduleAction(character);
            }
        }
    }

    private void scheduleAction(GameCharacter npc) {
        if (!npc.getMap().equals(gameInformation.getPlayer().getMap())) {
            turnQueue.remove(npc);
        }
        if (!turnQueue.contains(npc)) {
            npcController.act(npc);
            if (npc.hasAction()) {
                turnQueue.add(npc);
            }
        }
    }

    public void processActions() {
        Preconditions.checkArgument(!turnQueue.isEmpty(), "No actions on turn queue!");
        gameInformation.setTime(turnQueue.getTime());
        GameCharacter nextActor = turnQueue.poll();
        while (!(nextActor instanceof Player)) { // first process actions before player
            processAction(nextActor);
            scheduleAction(nextActor);
            gameInformation.setTime(turnQueue.getTime());
            nextActor = turnQueue.poll();
        }
        processAction(nextActor); // player action
        while (!turnQueue.isEmpty() && turnQueue.getTime() == gameInformation.getCurrentTime()) {
            // then actions at the same time as player
            nextActor = turnQueue.poll();
            processAction(nextActor);
        }
    }

    private void processAction(GameCharacter gameCharacter) {
        log.debug(gameCharacter + " performs " + gameCharacter.describeAction() + " at " + gameInformation.getCurrentTime());
        final ActionResult actionResult = gameCharacter.performAction();
        events.addAll(actionResult.getEvents());
        for (final String message : gameCharacter.pollMessages()) {
            gameInformation.addMessage(message);
        }
        gameInformation.addMessage(actionResult.getMessage());
    }

    public void processEvents() {
        final ListIterator<Event> eventsIterator = events.listIterator();
        while (eventsIterator.hasNext()) {
            final Event e = eventsIterator.next();
            e.occur();
            gameInformation.addMessage(e.getTextualDescription());
            eventsIterator.remove();
        }
    }
}
