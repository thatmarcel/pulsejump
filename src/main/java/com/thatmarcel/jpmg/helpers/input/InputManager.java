package com.thatmarcel.jpmg.helpers.input;

import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.input.UserAction;
import com.thatmarcel.jpmg.JPMGApp;
import com.thatmarcel.jpmg.helpers.misc.GenericManager;
import com.thatmarcel.jpmg.helpers.config.Config;
import com.thatmarcel.jpmg.helpers.player.MovementType;
import com.thatmarcel.jpmg.helpers.player.Player;
import com.thatmarcel.jpmg.helpers.pulse.PulseAction;
import com.thatmarcel.jpmg.helpers.pulse.internal.PulseType;
import com.thatmarcel.jpmg.helpers.state.GameState;
import javafx.scene.input.KeyCode;

public class InputManager implements GenericManager {
    private GameState currentState;

    public Boolean autoRun = Config.Gameplay.autoRun;
    public Boolean autoRunAllowsJumping = Config.Gameplay.autoRunAllowsJumping;

    public Boolean jumpKeyHeld = false;

    @Override
    public void initialize() {
        try {
            FXGL.getInput().addAction(new UserAction("Confirm") {
                @Override
                protected void onAction() {
                    if (currentState == GameState.MAIN_MENU) {
                        JPMGApp.activeInstance.stateManager.activateState(GameState.PLAYING1);
                    }
                }

                @Override
                protected void onActionEnd() {
                }
            }, Config.Controls.confirmKey);

            FXGL.getInput().addAction(new UserAction("Open menu") {
                @Override
                protected void onAction() {
                    if (currentState != GameState.MAIN_MENU) {
                        JPMGApp.activeInstance.stateManager.activateState(GameState.MAIN_MENU);
                    }
                }

                @Override
                protected void onActionEnd() {
                }
            }, Config.Controls.menuKey);

            FXGL.getInput().addAction(new UserAction("Jump") {
                @Override
                protected void onAction() {
                    if (jumpKeyHeld || (autoRun && !autoRunAllowsJumping)) {
                        return;
                    }

                    jumpKeyHeld = true;

                    if (currentState != GameState.MAIN_MENU && Player.activeInstance != null) {
                        Player.activeInstance.move(MovementType.FAR_JUMP);
                    }
                }

                @Override
                protected void onActionEnd() {
                    jumpKeyHeld = false;
                }
            }, Config.Controls.jumpKey);

            FXGL.getInput().addAction(new UserAction("Left") {
                @Override
                protected void onAction() {
                    if (autoRun) {
                        return;
                    }

                    if (currentState != GameState.MAIN_MENU && Player.activeInstance != null) {
                        Player.activeInstance.move(MovementType.LEFT);
                    }
                }

                @Override
                protected void onActionEnd() {
                    if (autoRun) {
                        return;
                    }

                    if (currentState != GameState.MAIN_MENU && Player.activeInstance != null) {
                        Player.activeInstance.move(MovementType.IDLE);
                    }
                }
            }, Config.Controls.leftKey);

            FXGL.getInput().addAction(new UserAction("Right") {
                @Override
                protected void onAction() {
                    if (autoRun) {
                        return;
                    }

                    if (currentState != GameState.MAIN_MENU && Player.activeInstance != null) {
                        Player.activeInstance.move(MovementType.RIGHT);
                    }
                }

                @Override
                protected void onActionEnd() {
                    if (autoRun) {
                        return;
                    }

                    if (currentState != GameState.MAIN_MENU && Player.activeInstance != null) {
                        Player.activeInstance.move(MovementType.IDLE);
                    }
                }
            }, Config.Controls.rightKey);

            if (Config.PulseAction.isTesting) {
                FXGL.onKey(KeyCode.UP, () -> PulseAction.keyPressed(PulseType.UP));
                FXGL.onKey(KeyCode.DOWN, () -> PulseAction.keyPressed(PulseType.DOWN));
            }
        } catch (Exception ignored) { }
    }

    @Override
    public void activateState(GameState state) {
        currentState = state;
    }
}
