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

package com.github.nzyuzin.candiderl.game.characters.actions;

import com.github.nzyuzin.candiderl.game.events.Event;

import java.util.Collections;
import java.util.List;

public class ActionResult {

    public static final ActionResult EMPTY = new ActionResult();

    private final List<Event> events;

    private final String message;

    public ActionResult(List<Event> events, String message) {
        this.events = events;
        this.message = message;
    }

    public ActionResult(List<Event> events) {
        this(events, "");
    }

    public ActionResult(Event event) {
        this(Collections.singletonList(event));
    }

    public ActionResult(String message) {
        this(Collections.emptyList(), message);
    }

    private ActionResult() {
        this(Collections.emptyList(), "");
    }

    public List<Event> getEvents() {
        return events;
    }

    public String getMessage() {
        return message;
    }
}
