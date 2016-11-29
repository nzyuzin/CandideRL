package com.github.nzyuzin.candiderl.game.events;

abstract  class AbstractEvent<T extends EventContext> implements Event<T> {
    private final T context;

    AbstractEvent(T context) {
        this.context = context;
    }

    protected T getContext() {
        return context;
    }
}
