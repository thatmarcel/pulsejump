package com.thatmarcel.jpmg.helpers.ui;

import com.thatmarcel.jpmg.helpers.misc.GenericManager;
import com.thatmarcel.jpmg.helpers.state.GameState;
import com.thatmarcel.jpmg.helpers.ui.internal.InGameUI;
import com.thatmarcel.jpmg.helpers.ui.internal.MainMenuUI;
import com.thatmarcel.jpmg.helpers.ui.internal.PulseActionUI;

public class UIManager implements GenericManager {
    private MainMenuUI mainMenuUI;
    private PulseActionUI pulseActionUI;
    private InGameUI inGameUI;

    public static UIManager activeInstance;

    @Override
    public void initialize() {
        this.mainMenuUI = new MainMenuUI();
        this.pulseActionUI = new PulseActionUI();
        this.inGameUI = new InGameUI();

        activeInstance = this;
    }

    @Override
    public void activateState(GameState state) {
        mainMenuUI.remove();
        pulseActionUI.forceRemove();
        inGameUI.remove();

        switch (state) {
            case MAIN_MENU -> mainMenuUI.add();
            case PLAYING1 -> inGameUI.add();
        }
    }

    public void showPulseActionUI() {
        pulseActionUI.add();
    }

    public void hidePulseActionUI() {
        pulseActionUI.remove();
    }

    public void doHeartbeat() {
        inGameUI.doHeartbeat();
    }

    public void updateBPM(int bpm) {
        inGameUI.updateBPM(bpm);
    }
}
