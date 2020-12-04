package com.thatmarcel.jpmg.helpers.state;

import com.almasb.fxgl.dsl.FXGL;
import com.thatmarcel.jpmg.JPMGApp;
import com.thatmarcel.jpmg.helpers.misc.GenericManager;
import com.thatmarcel.jpmg.helpers.game.GameManager;
import com.thatmarcel.jpmg.helpers.input.InputManager;
import com.thatmarcel.jpmg.helpers.pulse.PulseAction;
import com.thatmarcel.jpmg.helpers.serial.SerialCommunication;
import com.thatmarcel.jpmg.helpers.ui.UIManager;

public class StateManager implements GenericManager {
    private GameState currentState;

    private UIManager uiManager;
    private InputManager inputManager;
    private GameManager gameManager;

    public Boolean isTransitioning = false;

    @Override
    public void initialize() {
        this.uiManager = new UIManager();
        this.uiManager.initialize();

        this.inputManager = new InputManager();
        this.inputManager.initialize();

        this.gameManager = new GameManager();
        this.gameManager.initialize();

        SerialCommunication.start();
    }

    @Override
    public void activateState(GameState state) {
        if (state == currentState || isTransitioning) {
            return;
        }

        if (state != GameState.MAIN_MENU) {
            FXGL.getGameScene().setCursorInvisible();
        }

        this.uiManager.activateState(state);
        this.inputManager.activateState(state);
        this.gameManager.activateState(state);

        PulseAction.activeInstance = null;

        this.currentState = state;
    }
}
