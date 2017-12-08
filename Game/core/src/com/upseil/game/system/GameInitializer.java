package com.upseil.game.system;

import com.artemis.BaseSystem;
import com.artemis.Entity;
import com.artemis.annotations.Wire;
import com.upseil.game.GameConfig;
import com.upseil.game.Tag;
import com.upseil.game.component.GameState;
import com.upseil.game.domain.Data;
import com.upseil.gdx.artemis.system.TagManager;

public class GameInitializer extends BaseSystem {
    
    private TagManager<Tag> tagManager;
    
    @Wire(name="Config") private GameConfig config;
    
    @Override
    protected void initialize() {
        GameState gameState = new GameState();
        gameState.setData(new Data(config.getInitialValue()));
        
        Entity gameStateEntity = world.createEntity();
        gameStateEntity.edit().add(gameState);
        tagManager.register(Tag.GameState, gameStateEntity);
    }

    @Override
    protected boolean checkProcessing() {
        return false;
    }

    @Override
    protected void processSystem() { }
    
}
