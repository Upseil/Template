package com.upseil.game.client;

import java.util.HashMap;
import java.util.Map;

import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.TextResource;

public interface Resources extends ClientBundle {
    
    Resources Instance = GWT.create(Resources.class);
    
    @Source("../game.init")
    TextResource gameInitText();
    
    default Map<String, String> gameInit() {
        String gameInitText = gameInitText().getText();
        String[] gameInitLines = gameInitText.split("\n");
        Map<String, String> gameInit = new HashMap<>();
        for (String line : gameInitLines) {
            String trimmedLine = line.trim();
            if (trimmedLine.startsWith("#") || trimmedLine.startsWith("!")) {
                continue;
            }
            
            String[] property = line.split("=");
            if (property.length == 2) {
                gameInit.put(property[0].trim(), property[1].trim());
            }
        }
        return gameInit;
    }
    
}
