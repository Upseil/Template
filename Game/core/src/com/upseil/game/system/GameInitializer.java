package com.upseil.game.system;

import com.artemis.BaseSystem;
import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.EntityEdit;
import com.artemis.annotations.Wire;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.I18NBundle;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.upseil.game.Config.GameConfig;
import com.upseil.game.Config.GameConfigValues;
import com.upseil.game.Constants.Tag;
import com.upseil.game.component.GameState;
import com.upseil.game.domain.Data;
import com.upseil.gdx.artemis.component.InputHandler;
import com.upseil.gdx.artemis.component.Layer;
import com.upseil.gdx.artemis.component.Scene;
import com.upseil.gdx.artemis.system.LayeredSceneRenderSystem;
import com.upseil.gdx.artemis.system.TagManager;
import com.upseil.gdx.pool.PooledPools;
import com.upseil.gdx.scene2d.SimpleChangeListener;
import com.upseil.gdx.scene2d.util.SameWidth;
import com.upseil.gdx.scene2d.util.ValueLabelBuilder;

public class GameInitializer extends BaseSystem {
    
    private TagManager<Tag> tagManager;
    private ImportExportSystem importExportSystem;
    private LayeredSceneRenderSystem<?> renderSystem;
    private SaveSystem saveSystem;
    private LoadSystem loadSystem;
    
    private ComponentMapper<GameState> gameStateMapper;
    
    @Wire(name="Config") private GameConfig config;
    @Wire(name="Skin") private Skin skin;
    @Wire(name="UI") private I18NBundle hudMessages;
    
    @Override
    protected void initialize() {
        Data data = PooledPools.obtain(Data.class);
        data.setValue(config.getInt(GameConfigValues.InitialValue));
        GameState gameState = new GameState();
        gameState.setData(data);
        
        Entity gameStateEntity = world.createEntity();
        gameStateEntity.edit().add(gameState);
        tagManager.register(Tag.GameState, gameStateEntity);
        
        initializeUI();
    }

    private void initializeUI() {
        Stage uiStage = new Stage(new ScreenViewport(), renderSystem.getGlobalBatch());
        EntityEdit uiEntity = world.createEntity().edit();
        uiEntity.create(Layer.class).setZIndex(1);
        uiEntity.create(Scene.class).initialize(uiStage);
        uiEntity.create(InputHandler.class).setProcessor(uiStage);
        tagManager.register(Tag.UiStage, uiEntity.getEntityId());

        TextButton saveButton = new TextButton(hudMessages.get("save"), skin);
        saveButton.addListener(new SimpleChangeListener(saveSystem::saveToAutoSlot));
        TextButton loadButton = new TextButton(hudMessages.get("load"), skin);
        loadButton.addListener(new SimpleChangeListener(loadSystem::loadFromAutoSlot));
        
        TextButton exportButton = new TextButton(hudMessages.get("export"), skin);
        exportButton.addListener(new SimpleChangeListener(() -> saveSystem.exportSave(data -> importExportSystem.exportText(data))));
        TextButton importButton = new TextButton(hudMessages.get("import"), skin);
        importButton.addListener(new SimpleChangeListener(() -> importExportSystem.showImportDialog()));
        importButton.padLeft(10).padRight(10);
        
        Button minusButton = new ImageButton(skin, "minus");
        minusButton.addListener(new SimpleChangeListener(() -> getGameState().getData().decrement()));
        Button plusButton = new ImageButton(skin, "plus");
        plusButton.addListener(new SimpleChangeListener(() -> getGameState().getData().increment()));
        Table controlsTable = new Table(skin);
        controlsTable.defaults().space(5);
        controlsTable.add(minusButton).size(40).expandX().fillX().right().padRight(20);
        controlsTable.add(hudMessages.get("value") + ":", "big");
        controlsTable.add(ValueLabelBuilder.newLabel(skin, "big").withValue(() -> getGameState().getData().getValue()).build());
        controlsTable.add(plusButton).size(40).expandX().fillX().left().padLeft(20);

        Table container = new Table(skin);
        container.setFillParent(true);
        container.top().left().pad(10);
        container.defaults().align(Align.center).expandX().fillX().uniformX();
        
        container.row().width(new SameWidth(loadButton, exportButton, saveButton, importButton));
        container.add(saveButton);
        container.add(loadButton);
        container.add(exportButton);
        container.add(importButton);
        
        container.row();
        container.add(controlsTable).colspan(container.getColumns()).fillY().expandY();
        
        uiStage.addActor(container);
    }
    
    private GameState getGameState() {
        return gameStateMapper.get(tagManager.getEntity(Tag.GameState));
    }

    @Override
    protected boolean checkProcessing() {
        return false;
    }

    @Override
    protected void processSystem() { }
    
}
