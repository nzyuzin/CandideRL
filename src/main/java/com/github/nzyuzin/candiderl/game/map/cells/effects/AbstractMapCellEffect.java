package com.github.nzyuzin.candiderl.game.map.cells.effects;

import com.github.nzyuzin.candiderl.game.map.cells.AbstractMapCell;
import com.google.common.base.Preconditions;

public abstract class AbstractMapCellEffect<T extends AbstractMapCell> implements MapCellEffect<T> {
    private int duration;

    public AbstractMapCellEffect(int initialDuration) {
        this.duration = initialDuration;
    }

    @Override
    public void apply(T cell) {
        Preconditions.checkArgument(duration > 0, "Can only apply effects with positive duration");
        duration--;
    }

    @Override
    public int getDuration() {
        return duration;
    }
}
