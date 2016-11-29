package com.github.nzyuzin.candiderl.game.characters.actions;

import com.github.nzyuzin.candiderl.game.characters.GameCharacter;
import com.github.nzyuzin.candiderl.game.events.Event;
import com.github.nzyuzin.candiderl.game.events.ExplosionEvent;
import com.github.nzyuzin.candiderl.game.events.PositionedEventContext;
import com.github.nzyuzin.candiderl.game.utility.PositionOnMap;

import java.util.Collections;
import java.util.List;

public class CastExplosionAction extends AbstractGameAction {
    private final PositionOnMap position;

    public CastExplosionAction(GameCharacter subject, PositionOnMap position) {
        super(subject);
        this.position = position;
    }

    @Override
    public boolean canBeExecuted() {
        return true;
    }

    @Override
    protected List<Event> doExecute() {
        PositionedEventContext context = new PositionedEventContext(position);
        return Collections.singletonList(new ExplosionEvent(context, 3, 10));
    }
}
