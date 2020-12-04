package com.thatmarcel.jpmg;

import com.almasb.fxgl.app.ApplicationMode;
import com.almasb.fxgl.app.GameApplication;
import com.almasb.fxgl.app.GameSettings;

import com.almasb.fxgl.dsl.FXGL;
import com.thatmarcel.jpmg.helpers.config.Config;
import com.thatmarcel.jpmg.helpers.state.GameState;
import com.thatmarcel.jpmg.helpers.state.StateManager;
import com.thatmarcel.jpmg.helpers.strings.Strings;
import javafx.util.Duration;

@SuppressWarnings("FieldCanBeLocal")
public class JPMGApp extends GameApplication {
    public StateManager stateManager;

    private final Boolean shouldUseFullscreen = Config.Window.shouldUseFullscreen;

    // Only used when the game is not running in fullscreen
    public Integer width = Config.Window.width;
    public Integer height = Config.Window.height;

    public static Boolean isRunning;

    public static JPMGApp activeInstance;

    @Override
    protected void initSettings(GameSettings settings) {
        settings.setFullScreenAllowed(shouldUseFullscreen);
        settings.setFullScreenFromStart(shouldUseFullscreen);

        if (shouldUseFullscreen) {
            settings.setFullScreenAllowed(true);
            settings.setFullScreenFromStart(true);
            width = settings.getWidth();
            height = settings.getHeight();
        } else {
            settings.setWidth(this.width);
            settings.setHeight(this.height);
        }

        String version = getClass().getPackage().getImplementationVersion();
        // Use a placeholder string when the game is ran
        // during development as the implementation version
        // is only set when the game is running from a packaged
        // .jar file
        if (version == null) {
            version = Strings.developmentVersion;
            settings.setApplicationMode(ApplicationMode.DEVELOPER);
        }

        settings.setTitle(Strings.gameTitle + " [" + version + "]"); // Set the window title
        settings.setVersion(""); // Stop FXGL from displaying the version string themselves

        settings.setGameMenuEnabled(false);
        settings.setDeveloperMenuEnabled(true);
        settings.setProfilingEnabled(false);
    }

    @Override
    protected void initGame() {
        activeInstance = this;
        stateManager = new StateManager();
        stateManager.initialize();
        stateManager.activateState(GameState.MAIN_MENU);

        // Will run after the game window has initialized,
        // used in SerialCommunication to exit the loop
        // when the game window is closed
        FXGL.getGameTimer().runOnceAfter(() -> {
            isRunning = true;
            FXGL.getGameScene().getRoot().getScene().getWindow().setOnCloseRequest(ev -> isRunning = false);
        }, Duration.millis(10));
    }

    public static void main(String[] args) {
        launch(args);
    }
}
