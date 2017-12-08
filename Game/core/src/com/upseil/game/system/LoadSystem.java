package com.upseil.game.system;

import com.upseil.game.MetaData;
import com.upseil.game.Savegame;
import com.upseil.game.Tag;
import com.upseil.game.GameConfig.SavegameConfig;
import com.upseil.game.component.GameState;
import com.upseil.gdx.artemis.system.AbstractLoadSystem;
import com.upseil.gdx.artemis.system.TagManager;
import com.upseil.gdx.math.Random;
import com.upseil.gdx.serialization.Reader;

public class LoadSystem extends AbstractLoadSystem<Savegame> {
    
    private TagManager<Tag> tagManager;
    
    public LoadSystem(Reader<Savegame> mapper, SavegameConfig config) {
        super(mapper, config.getSaveStoreName(), config.getAutoSaveSlot());
    }

    @Override
    protected void loadGame(Savegame savegame) {
        GameState gameState = savegame.getGameState();
        world.edit(tagManager.getEntityId(Tag.GameState)).add(gameState);

        MetaData metaData = savegame.getMetaData();
        Random.setState(metaData.getSeed0(), metaData.getSeed1());
    }
    
}
