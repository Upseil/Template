package com.upseil.game.system;

import com.artemis.EntityEdit;
import com.artemis.annotations.Wire;
import com.badlogic.gdx.Application.ApplicationType;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextArea;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.I18NBundle;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.upseil.gdx.artemis.component.InputHandler;
import com.upseil.gdx.artemis.component.Layer;
import com.upseil.gdx.artemis.component.Scene;
import com.upseil.gdx.artemis.system.LayeredSceneRenderSystem;
import com.upseil.gdx.scene2d.util.SameWidth;

public class RenderSystem extends LayeredSceneRenderSystem {
    
    private SaveSystem saveSystem;
    private LoadSystem loadSystem;

    @Wire(name="Skin") private Skin skin;
    @Wire(name="UI") private I18NBundle hudMessages;
    
    //TODO Remove me
    private Stage uiStage;
    
    @Override
    protected void initialize() {
        uiStage = new Stage(new ScreenViewport(), globalBatch);
        EntityEdit hud = world.createEntity().edit();
        hud.create(Layer.class).setZIndex(1);
        hud.create(Scene.class).initialize(uiStage);
        hud.create(InputHandler.class).setProcessor(uiStage);
        fillHud();
    }

    private void fillHud() {
        TextButton saveButton = new TextButton(hudMessages.get("save"), skin);
        saveButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                saveSystem.saveToAutoSlot();
            }
        });
        TextButton loadButton = new TextButton(hudMessages.get("load"), skin);
        loadButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                loadSystem.loadFromAutoSlot();
            }
        });
        
        TextButton exportButton = new TextButton(hudMessages.get("export"), skin);
        exportButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                saveSystem.exportSave(data -> saveSystem.getSystemAccessClipboard().setContents(data));
            }
        });
        TextButton importButton = new TextButton(hudMessages.get("import"), skin);
        ImportDialog importDialog = new ImportDialog();
        importButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                if (Gdx.app.getType() == ApplicationType.WebGL) {
                    loadSystem.importGame(saveSystem.getSystemAccessClipboard().getContents());
                } else {
                    importDialog.show();
                }
            }
        });
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
    protected void dispose() {
        super.dispose();
        skin.dispose();
    }
    
    private class ImportDialog extends Dialog {
        
        private static final int textAreaWidth = 500;
        private static final int dialogHeight = 400;
        private static final String notNull = "";
        
        private final TextArea textArea;

        public ImportDialog() {
            super(hudMessages.get("import"), skin, "modal-dialog");
            setMovable(false);
            textArea = new TextArea("", skin);
            
            button(hudMessages.get("cancel"), null);
            key(Keys.ESCAPE, null);
            button(hudMessages.get("import"), notNull);
            key(Keys.ENTER, notNull);

            // Adding textArea last to calculate the height
            float textAreaHeight = dialogHeight - getPrefHeight();
            getContentTable().add(textArea).size(textAreaWidth, textAreaHeight);
        }
        
        public void show() {
            super.show(uiStage);
            textArea.setText(null);
            getStage().setKeyboardFocus(textArea);
        }
        
        @Override
        protected void result(Object object) {
            if (object != null) {
                loadSystem.importGame(textArea.getText());
            }
        }
        
    }
    
}
