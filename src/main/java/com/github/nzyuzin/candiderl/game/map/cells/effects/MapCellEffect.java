package com.github.nzyuzin.candiderl.game.map.cells.effects;

import com.github.nzyuzin.candiderl.game.map.cells.MapCell;

public interface MapCellEffect<T extends MapCell> {
    void apply(T cell);
    int getDuration();
}
