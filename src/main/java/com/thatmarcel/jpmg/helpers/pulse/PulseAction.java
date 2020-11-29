package com.thatmarcel.jpmg.helpers.pulse;

import com.almasb.fxgl.dsl.FXGL;
import com.thatmarcel.jpmg.JPMGApp;
import com.thatmarcel.jpmg.helpers.config.Config;
import com.thatmarcel.jpmg.helpers.misc.BooleanCallback;
import com.thatmarcel.jpmg.helpers.pulse.internal.PulseType;
import com.thatmarcel.jpmg.helpers.serial.SerialCommunication;
import com.thatmarcel.jpmg.helpers.ui.UIManager;
import com.thatmarcel.jpmg.helpers.ui.internal.PulseActionUI;
import javafx.util.Duration;

import java.util.Random;

public class PulseAction {
    public static PulseAction create() {
        return new PulseAction();
    }

    public static PulseAction activeInstance;

    public PulseType requestedType;
    public BooleanCallback callback;

    private int lastRequestId = -1;

    PulseAction() {
        activeInstance = this;
    }

    public PulseAction start(PulseType requestedType, BooleanCallback callback) {
        this.requestedType = requestedType;
        this.callback = callback;
        UIManager.activeInstance.showPulseActionUI();

        int previousBPM = SerialCommunication.lastBPM;

        int requestId = new Random().nextInt(10000);
        lastRequestId = requestId;

        if (!Config.PulseAction.isTesting) {
            FXGL.getGameTimer().runOnceAfter(() -> {
                // Don't continue if the game has been manually exited before
                // the 10 seconds are over
                if (requestId != lastRequestId) {
                    return;
                }

                resultAvailable(
                        SerialCommunication.lastBPM > previousBPM
                                ? PulseType.UP
                                : SerialCommunication.lastBPM < previousBPM ? PulseType.DOWN : PulseType.STAY
                );
            }, Duration.seconds(10));
        }

        return this;
    }

    private void resultAvailable(PulseType type) {
        callback.execute(type == requestedType);
        if (type == requestedType) {
            UIManager.activeInstance.hidePulseActionUI();
        }
        activeInstance = null;
    }

    public static void keyPressed(PulseType keyType) {
        if (activeInstance != null) {
            activeInstance.resultAvailable(keyType);
        }
    }
}
