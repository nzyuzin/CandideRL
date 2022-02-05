package com.github.nzyuzin.candiderl.game;

import com.github.nzyuzin.candiderl.game.characters.NpcFactory;
import com.github.nzyuzin.candiderl.game.characters.actions.ActionFactory;
import com.github.nzyuzin.candiderl.game.fov.FovFactory;
import com.github.nzyuzin.candiderl.game.map.MapFactory;

public class GameFactories {
    private final ActionFactory actionFactory;
    private final FovFactory fovFactory;
    private final MapFactory mapFactory;
    private final NpcFactory npcFactory;

    public GameFactories(ActionFactory actionFactory, FovFactory fovFactory,
                         MapFactory mapFactory, NpcFactory npcFactory) {
        this.actionFactory = actionFactory;
        this.fovFactory = fovFactory;
        this.mapFactory = mapFactory;
        this.npcFactory = npcFactory;
    }

    public ActionFactory getActionFactory() {
        return actionFactory;
    }

    public FovFactory getFovFactory() {
        return fovFactory;
    }

    public MapFactory getMapFactory() {
        return mapFactory;
    }

    public NpcFactory getNpcFactory() {
        return npcFactory;
    }
}
