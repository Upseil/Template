package com.upseil.game.client;

import java.util.function.Consumer;

import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.upseil.game.ImportDialog;

public class GwtImportDialog implements ImportDialog {
    
    private final DialogBox dialog;
    private final TextArea textArea;
    
    private Consumer<String> action;
    
    public GwtImportDialog(UiConstants uiConstants) {
        dialog = new DialogBox();
        dialog.setText(uiConstants.importMessage());
        dialog.setModal(true);
        dialog.setGlassEnabled(true);
        dialog.setAnimationEnabled(true);
        dialog.setAutoHideEnabled(true);
        
        VerticalPanel content = new VerticalPanel();
        content.setSpacing(4);
        dialog.setWidget(content);
        
        textArea = new TextArea();
        textArea.addStyleName("import-dialog-textarea");
        content.add(textArea);
        content.setCellHorizontalAlignment(textArea, HasHorizontalAlignment.ALIGN_CENTER);
        
        FlowPanel controls = new FlowPanel();
        controls.addStyleName("import-dialog-controls");
        content.add(controls);
        content.setCellHorizontalAlignment(controls, HasHorizontalAlignment.ALIGN_CENTER);
        
        Button cancelButton = new Button(uiConstants.cancel());
        cancelButton.addStyleName("button");
        cancelButton.addClickHandler(e -> dialog.hide());
        controls.add(cancelButton);
        
        Button importButton = new Button(uiConstants.importMessage());
        importButton.addStyleName("button");
        importButton.addClickHandler(e -> {
            dialog.hide();
            if (action != null) {
                String text = textArea.getText();
                text = text.isEmpty() ? null : text;
                action.accept(text);
            }
        });
        controls.add(importButton);
    }

    @Override
    public void show() {
        textArea.setText(null);
        textArea.setFocus(true);
        dialog.center();
        dialog.show();
    }
    
    @Override
    public void setAction(Consumer<String> action) {
        this.action = action;
    }
    
}
