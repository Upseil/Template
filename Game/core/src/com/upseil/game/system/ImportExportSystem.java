package com.upseil.game.system;

import java.util.function.Consumer;

import com.artemis.BaseSystem;
import com.artemis.ComponentMapper;
import com.artemis.annotations.Wire;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.I18NBundle;
import com.upseil.game.Constants.Tag;
import com.upseil.game.ImportDialog;
import com.upseil.gdx.artemis.component.Scene;
import com.upseil.gdx.artemis.system.TagManager;
import com.upseil.gdx.scene2d.dialog.LargeTextInputDialog;

public class ImportExportSystem extends BaseSystem {

    private TagManager<Tag> tagManager;
    private ComponentMapper<Scene> sceneMapper;

    @Wire(name="Skin") private Skin skin;
    @Wire(name="UI") private I18NBundle uiMessages;
    
    private ImportDialog importDialog;
    private Consumer<String> exportConsumer;

    public ImportExportSystem(ImportDialog importDialog, Consumer<String> exportConsumer) {
        this.importDialog = importDialog;
        this.exportConsumer = exportConsumer;
    }
    
    @Override
    protected void initialize() {
        Consumer<String> importGame = world.getSystem(LoadSystem.class)::importGame;
        if (importDialog == null) {
            LargeTextInputDialog defaultImportDialog = new LargeTextInputDialog(uiMessages.get("import"), null, uiMessages.get("cancel"),
                                                                                uiMessages.get("import"), skin, importGame);
            defaultImportDialog.getTextFieldCell().size(300, 200);
            importDialog = new ImportDialog() {
                @Override
                public void show() {
                    defaultImportDialog.getTextField().setText(null);
                    defaultImportDialog.show(sceneMapper.get(tagManager.getEntityId(Tag.UiStage)).getStage());
                }
                @Override
                public void setAction(Consumer<String> action) { }
            };
        }
        importDialog.setAction(importGame);
        
        if (exportConsumer == null) {
            exportConsumer = Gdx.app.getClipboard()::setContents;
        }
    }
    
    public void exportText(String text) {
        exportConsumer.accept(text);
    }
    
    public void showImportDialog() {
        importDialog.show();
    }
    
    @Override
    protected boolean checkProcessing() {
        return false;
    }
    
    @Override
    protected void processSystem() { }
    
}
