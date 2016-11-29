package com.github.nzyuzin.candiderl.game.map.cells.effects;

import com.github.nzyuzin.candiderl.game.map.cells.AbstractMapCell;
import com.google.common.base.Preconditions;

public class Explosion extends AbstractMapCellEffect<AbstractMapCell> {
    private final int explosionPower;

    public Explosion(int explosionPower) {
        super(1);
        Preconditions.checkArgument(explosionPower >= 0, "Explosion power should be non-negative!");
        this.explosionPower = explosionPower;
    }

    @Override
    public void apply(AbstractMapCell cell) {
        super.apply(cell);
        cell.getGameCharacter().takeDamage(explosionPower);
    }
}
