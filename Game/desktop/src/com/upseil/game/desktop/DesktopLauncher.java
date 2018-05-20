package com.upseil.game.desktop;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.upseil.game.GameApplication;
import com.upseil.game.Savegame;
import com.upseil.game.SerializationContext;
import com.upseil.gdx.serialization.desktop.DesktopCompressingMapper;

public class DesktopLauncher {
    
    public static void main(String[] args) throws Exception {
        Properties gameInit = new Properties();
        try (InputStream fileStream = new FileInputStream("game.init")) {
            gameInit.load(fileStream);
        } catch (IOException e) {
            System.err.println("Error loading the init file");
            throw e;
        }
        
        LwjglApplicationConfiguration configuration = new LwjglApplicationConfiguration();
        configuration.title = gameInit.getProperty("Title");
        configuration.width = Integer.parseInt(gameInit.getProperty("Width"));
        configuration.height = Integer.parseInt(gameInit.getProperty("Height"));
        configuration.resizable = !Boolean.parseBoolean(gameInit.getProperty("FixedSize"));
        
        DesktopCompressingMapper<Savegame> savegameMapper = new DesktopCompressingMapper<>(Savegame.class);
        savegameMapper.setCompressing(true);
        
        SerializationContext context = new SerializationContext(savegameMapper);
        new LwjglApplication(new GameApplication(context, /*systemAccessClipboard*/ null), configuration);
    }
    
}
