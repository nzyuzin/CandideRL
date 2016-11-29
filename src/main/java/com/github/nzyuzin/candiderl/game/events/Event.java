package com.github.nzyuzin.candiderl.game.events;

public interface Event<T extends EventContext> {
    void occur();
}
