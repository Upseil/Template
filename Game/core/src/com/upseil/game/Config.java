package com.upseil.game;

import com.upseil.gdx.artemis.ArtemisConfigs.SaveConfig;
import com.upseil.gdx.properties.EnumerizedJsonBasedProperties;
import com.upseil.gdx.scene2d.util.BackgroundBuilder;
import com.upseil.gdx.scene2d.util.BorderBuilder;
import com.upseil.gdx.scene2d.util.DividerBuilder;
import com.upseil.gdx.util.GDXUtil;

public final class Config {
    
    public static enum GameConfigValues {
        InitialValue
    }

    public static class GameConfig extends EnumerizedJsonBasedProperties<GameConfigValues> {
    
        private final SaveConfig saveConfig;
        private final BackgroundBuilder.Config backgroundBuilderConfig;
        private final BorderBuilder.Config borderBuilderConfig;
        private final DividerBuilder.Config dividerBuilderConfig;
    
        public GameConfig(String path) {
            super(GDXUtil.readJson(path), GameConfigValues.class);
            saveConfig = new SaveConfig(json.get("savegame"));
            
            backgroundBuilderConfig = new BackgroundBuilder.Config(json.get("backgroundBuilder"));
            borderBuilderConfig = new BorderBuilder.Config(json.get("borderBuilder"));
            dividerBuilderConfig = new DividerBuilder.Config(json.get("dividerBuilder"));
        }
        
        public SaveConfig getSavegameConfig() {
            return saveConfig;
        }
        
        public BackgroundBuilder.Config getBackgroundBuilderConfig() {
            return backgroundBuilderConfig;
        }
        
        public BorderBuilder.Config getBorderBuilderConfig() {
            return borderBuilderConfig;
        }
        
        public DividerBuilder.Config getDividerBuilderConfig() {
            return dividerBuilderConfig;
        }
        
    }
    
    private Config() { }
    
}
