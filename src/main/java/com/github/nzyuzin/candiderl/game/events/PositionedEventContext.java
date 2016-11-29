package com.github.nzyuzin.candiderl.game.events;

import com.github.nzyuzin.candiderl.game.utility.PositionOnMap;

public class PositionedEventContext extends AbstractEventContext {
    PositionOnMap position;

    public PositionedEventContext(PositionOnMap position) {
        this.position = position;
    }

    public PositionOnMap getPosition() {
        return position;
    }
}
