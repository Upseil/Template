package com.upseil.game.system;

import com.artemis.BaseSystem;
import com.artemis.Entity;
import com.artemis.EntityEdit;
import com.artemis.annotations.Wire;
import com.badlogic.gdx.Application.ApplicationType;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.I18NBundle;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.upseil.game.GameConfig;
import com.upseil.game.Tag;
import com.upseil.game.component.GameState;
import com.upseil.game.domain.Data;
import com.upseil.gdx.artemis.component.InputHandler;
import com.upseil.gdx.artemis.component.Layer;
import com.upseil.gdx.artemis.component.Scene;
import com.upseil.gdx.artemis.system.LayeredSceneRenderSystem;
import com.upseil.gdx.artemis.system.TagManager;
import com.upseil.gdx.pool.PooledPools;
import com.upseil.gdx.scene2d.SimpleClickedListener;
import com.upseil.gdx.scene2d.dialog.LargeTextInputDialog;
import com.upseil.gdx.scene2d.util.SameWidth;

public class GameInitializer extends BaseSystem {
    
    private TagManager<Tag> tagManager;
    private LayeredSceneRenderSystem<?> renderSystem;
    private SaveSystem saveSystem;
    private LoadSystem loadSystem;
    
    @Wire(name="Config") private GameConfig config;
    @Wire(name="Skin") private Skin skin;
    @Wire(name="UI") private I18NBundle hudMessages;
    
    @Override
    protected void initialize() {
        Data data = PooledPools.obtain(Data.class);
        data.setValue(config.getInitialValue());
        
        GameState gameState = new GameState();
        gameState.setData(data);
        
        Entity gameStateEntity = world.createEntity();
        gameStateEntity.edit().add(gameState);
        tagManager.register(Tag.GameState, gameStateEntity);
        
        initializeUI();
    }

    private void initializeUI() {
        Stage uiStage = new Stage(new ScreenViewport(), renderSystem.getGlobalBatch());
        EntityEdit hud = world.createEntity().edit();
        hud.create(Layer.class).setZIndex(1);
        hud.create(Scene.class).initialize(uiStage);
        hud.create(InputHandler.class).setProcessor(uiStage);

        TextButton saveButton = new TextButton(hudMessages.get("save"), skin);
        saveButton.addListener(new SimpleClickedListener(saveSystem::saveToAutoSlot));
        TextButton loadButton = new TextButton(hudMessages.get("load"), skin);
        loadButton.addListener(new SimpleClickedListener(loadSystem::loadFromAutoSlot));
        
        TextButton exportButton = new TextButton(hudMessages.get("export"), skin);
        exportButton.addListener(new SimpleClickedListener(() -> saveSystem.exportSave(data -> saveSystem.getSystemAccessClipboard().setContents(data))));
        LargeTextInputDialog importDialog = new LargeTextInputDialog(hudMessages.get("import"), null, hudMessages.get("cancel"),
                                                                     hudMessages.get("import"), skin, loadSystem::importGame);
        importDialog.getTextFieldCell().size(300, 200);
        TextButton importButton = new TextButton(hudMessages.get("import"), skin);
        importButton.addListener(new SimpleClickedListener(() -> {
            if (Gdx.app.getType() == ApplicationType.WebGL) {
                loadSystem.importGame(saveSystem.getSystemAccessClipboard().getContents());
            } else {
                importDialog.show(uiStage);
            }
        }));
        importButton.padLeft(10).padRight(10);

        SameWidth uniform = new SameWidth(loadButton, exportButton, saveButton, importButton);
        
        Table container = new Table(skin);
        container.setFillParent(true);
        container.top().left().pad(10);
        container.defaults().align(Align.center).expandX().fillX().uniformX().width(uniform);
        
        container.add(saveButton);
        container.add(loadButton);
        container.add(exportButton);
        container.add(importButton);
        
        uiStage.addActor(container);
    }

    @Override
    protected boolean checkProcessing() {
        return false;
    }

    @Override
    protected void processSystem() { }
    
}
