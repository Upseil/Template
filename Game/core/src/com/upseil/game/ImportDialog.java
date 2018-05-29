package com.upseil.game;

import java.util.function.Consumer;

public interface ImportDialog {
    
    void show();
    
    void setAction(Consumer<String> action);
    
}
