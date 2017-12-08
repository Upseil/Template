package com.upseil.game;

import com.upseil.gdx.serialization.Mapper;

public class SerializationContext {
    
    private final Mapper<Savegame> savegameMapper;
    
    public SerializationContext(Mapper<Savegame> savegameMapper) {
        this.savegameMapper = savegameMapper;
    }

    public Mapper<Savegame> getSavegameMapper() {
        return savegameMapper;
    }
    
}
