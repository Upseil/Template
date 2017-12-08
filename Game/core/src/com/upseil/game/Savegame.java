package com.upseil.game;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.upseil.game.component.GameState;

public class Savegame {
    
    private final MetaData metaData;
    private final GameState gameState;
    
    public Savegame(GameState gameState) {
        this(MetaData.get(), gameState);
    }

    /**
     * Only for serialization. 
     */
    @Deprecated
    @JsonCreator
    private Savegame(@JsonProperty("metaData") MetaData metaData, @JsonProperty("gameState") GameState gameState) {
        this.metaData = metaData;
        this.gameState = gameState;
    }

    public MetaData getMetaData() {
        return metaData;
    }

    public GameState getGameState() {
        return gameState;
    }
    
}
