package com.upseil.game;

import com.upseil.gdx.config.AbstractConfig;
import com.upseil.gdx.config.RawConfig;
import com.upseil.gdx.scene2d.util.BackgroundBuilder;
import com.upseil.gdx.scene2d.util.BorderBuilder;
import com.upseil.gdx.scene2d.util.DividerBuilder;

public class GameConfig extends AbstractConfig {

    private final SavegameConfig savegameConfig;
    
    private final BackgroundBuilder.Config backgroundBuilderConfig;
    private final BorderBuilder.Config borderBuilderConfig;
    private final DividerBuilder.Config dividerBuilderConfig;

    public GameConfig(String path) {
        super(path);
        savegameConfig = new SavegameConfig(getRawConfig().getChild("savegame"));
        
        backgroundBuilderConfig = new BackgroundBuilder.Config(getRawConfig().getChild("backgroundBuilder"));
        borderBuilderConfig = new BorderBuilder.Config(getRawConfig().getChild("borderBuilder"));
        dividerBuilderConfig = new DividerBuilder.Config(getRawConfig().getChild("dividerBuilder"));
    }
    
    public int getInitialValue() {
        return getRawConfig().getInt("initialValue");
    }
    
    public SavegameConfig getSavegameConfig() {
        return savegameConfig;
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
    
    public static class SavegameConfig extends AbstractConfig {

        public SavegameConfig(RawConfig config) {
            super(config);
        }
        
        public String getSaveStoreName() {
            return getRawConfig().getString("saveStoreName");
        }
        
        public String getAutoSaveSlot() {
            return getRawConfig().getString("autoSaveSlot");
        }
        
    }
    
}
