package com.upseil.game.system;

import com.artemis.BaseSystem;
import com.artemis.annotations.Wire;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

public class EntityFactory extends BaseSystem {
    
    @Wire(name="Skin") private Skin skin;
    
    @Override
    protected boolean checkProcessing() {
        return false;
    }
    
    @Override
    protected void processSystem() { }
    
}
