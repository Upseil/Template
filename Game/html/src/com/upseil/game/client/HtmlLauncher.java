package com.upseil.game.client;

import java.util.Map;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.backends.gwt.GwtApplication;
import com.badlogic.gdx.backends.gwt.GwtApplicationConfiguration;
import com.badlogic.gdx.backends.gwt.preloader.Preloader.PreloaderCallback;
import com.badlogic.gdx.backends.gwt.preloader.Preloader.PreloaderState;
import com.github.nmorel.gwtjackson.client.JsonDeserializationContext;
import com.github.nmorel.gwtjackson.client.JsonSerializationContext;
import com.github.nmorel.gwtjackson.client.ObjectMapper;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Element;
import com.google.gwt.event.logical.shared.ResizeEvent;
import com.google.gwt.event.logical.shared.ResizeHandler;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.RootPanel;
import com.upseil.game.GameApplication;
import com.upseil.game.Savegame;
import com.upseil.game.SerializationContext;
import com.upseil.gdx.gwt.serialization.HtmlCompressingMapper;
import com.upseil.gdx.gwt.util.BrowserConsoleLogger;
import com.upseil.gdx.gwt.util.SystemAccessClipboard;

public class HtmlLauncher extends GwtApplication {
    
    public interface SavegameMapper extends ObjectMapper<Savegame> { }
    
    private Map<String, String> gameInit;
    private boolean fixedSize;
    
    @Override
    public void onModuleLoad() {
        gameInit = Resources.Instance.gameInit();
        fixedSize = Boolean.parseBoolean(gameInit.get("FixedSize"));
        
        if (!fixedSize) {
            Window.enableScrolling(false);
        }
        super.onModuleLoad();
    }
    
    @Override
    public ApplicationListener createApplicationListener() {
        setLoadingListener(new LoadingListener() {
            @Override
            public void beforeSetup() {
                setApplicationLogger(new BrowserConsoleLogger());
            }
            @Override
            public void afterSetup() {
                if (!fixedSize) {
                    setupResizing();
                }
            }
        });
        
        JsonSerializationContext serializationContext = JsonSerializationContext.builder().indent(false).build();
        JsonDeserializationContext deserializationContext = JsonDeserializationContext.builder().failOnUnknownProperties(false).build();
        
        SavegameMapper savegameMapper = GWT.create(SavegameMapper.class);
        HtmlCompressingMapper<Savegame> htmlSavegameMapper = new HtmlCompressingMapper<>(savegameMapper, serializationContext, deserializationContext);
        htmlSavegameMapper.setCompressing(true);
        
        SerializationContext context = new SerializationContext(htmlSavegameMapper);
        return new GameApplication(context, new SystemAccessClipboard("clipboard-textarea", "clipboard"));
    }
    
    private void setupResizing() {
        getRootPanel().setWidth("100%");
        getRootPanel().setHeight("100%");
        
        Window.addResizeHandler(new ResizeHandler() {
            @Override
            public void onResize(ResizeEvent event) {
                getGraphics().setWindowedMode(event.getWidth(), event.getHeight());
            }
        });
    }
    
    @Override
    public GwtApplicationConfiguration getConfig() {
        int width, height;
        if (fixedSize) {
            width = Integer.parseInt(gameInit.get("Width"));
            height = Integer.parseInt(gameInit.get("Height"));
        } else {
            width = Window.getClientWidth();
            height = Window.getClientHeight();
        }
        
        GwtApplicationConfiguration configuration = new GwtApplicationConfiguration(width, height);
        configuration.preferFlash = false;
        configuration.rootPanel = createGamePanel(width, height);
        return configuration;
    }

    private Panel createGamePanel(int width, int height) {
        Panel gamePanel = new FlowPanel() {
            @Override
            public void onBrowserEvent(Event event) {
                int eventType = DOM.eventGetType(event);
                if (eventType == Event.ONMOUSEDOWN || eventType == Event.ONMOUSEUP) {
                    event.preventDefault();
                    event.stopPropagation();
                    Element target = event.getEventTarget().cast();
                    target.getStyle().setProperty("cursor", eventType == Event.ONMOUSEDOWN ? "default" : "");
                }
                super.onBrowserEvent(event);
            }
        };
        gamePanel.setWidth("" + width + "px");
        gamePanel.setHeight("" + height + "px");
        gamePanel.addStyleName("root");
        RootPanel.get().add(gamePanel);
        return gamePanel;
    }
    
    @Override
    public PreloaderCallback getPreloaderCallback() {
        final Label title = new Label(gameInit.get("Title"));
        title.addStyleName("game-title");
        getRootPanel().add(title);
        
        return new PreloaderCallback() {
            @Override
            public void error (String file) {
                System.out.println("error: " + file);
            }
            @Override
            public void update (PreloaderState state) {
                // TODO Add some kind of loading state
            }           
        };
    }
    
}
