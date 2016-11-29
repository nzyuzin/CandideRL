package com.github.nzyuzin.candiderl.game.events;

import com.github.nzyuzin.candiderl.game.map.cells.effects.Explosion;
import com.github.nzyuzin.candiderl.game.utility.PositionOnMap;

public class ExplosionEvent extends AbstractEvent<PositionedEventContext> {
    private final int size;
    private final int damage;

    public ExplosionEvent(PositionedEventContext context, int size, int damage) {
        super(context);
        this.size = size;
        this.damage = damage;
    }

    @Override
    public void occur() {
        final PositionOnMap pos = getContext().getPosition();
        pos.getMap().addEffectsToArea(pos.getPosition(), size, size, new Explosion(damage));
    }
}
