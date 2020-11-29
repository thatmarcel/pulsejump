package com.thatmarcel.jpmg.helpers.ui.internal;

import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.texture.Texture;
import com.thatmarcel.jpmg.JPMGApp;
import com.thatmarcel.jpmg.helpers.position.RelativePosition;
import com.thatmarcel.jpmg.helpers.strings.Strings;
import javafx.animation.FadeTransition;
import javafx.animation.PauseTransition;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.text.Text;
import javafx.util.Duration;

@SuppressWarnings("DuplicatedCode")
public class MainMenuUI {
    private Boolean added = false;

    private Texture titleText;
    private Text inputHint;
    private Text attributionNote;

    public void add() {
        if (added) {
            return;
        }

        int xStart = RelativePosition.combinedHalfPercentage(6);
        int yStart = RelativePosition.combinedHalfPercentage(5);

        titleText = FXGL.getAssetLoader().loadTexture("name-1.png");
        double titleAspectRatio = titleText.getWidth() / titleText.getHeight();
        int titleHeight = RelativePosition.combinedHalfPercentage(18);
        titleText.setFitHeight(titleHeight);
        titleText.setFitWidth(titleHeight * titleAspectRatio);
        titleText.setTranslateX(xStart);
        titleText.setTranslateY(yStart);
        FXGL.getGameScene().addUINode(titleText);

        inputHint = new Text();
        inputHint.setText(Strings.mainMenuInputHint);
        inputHint.setStyle("-fx-font: " + RelativePosition.combinedHalfPercentage(3) + " Verdana");
        int inputHintYPosition = yStart + titleHeight + RelativePosition.combinedHalfPercentage(3);
        inputHint.setTranslateX(xStart + RelativePosition.combinedHalfPercentage(2));
        inputHint.setTranslateY(inputHintYPosition);
        FXGL.getGameScene().addUINode(inputHint);

        attributionNote = new Text();
        attributionNote.setText(Strings.mainMenuAttributionNote);
        attributionNote.setStyle("-fx-font: " + RelativePosition.combinedHalfPercentage(1) + " Verdana");
        int attributionNoteYPosition = FXGL.getAppHeight() - RelativePosition.combinedHalfPercentage(5);
        attributionNote.setTranslateX(xStart + RelativePosition.combinedHalfPercentage(2));
        attributionNote.setTranslateY(attributionNoteYPosition);
        FXGL.getGameScene().addUINode(attributionNote);

        added = true;
    }

    public void remove() {
        if (!added) {
            return;
        }

        // Remove the old controls after the transition
        // even if new controls have been added
        final Texture titleText       = this.titleText;
        final Text    inputHint       = this.inputHint;
        final Text    attributionNote = this.attributionNote;

        this.titleText = null;
        this.inputHint = null;
        this.attributionNote = null;

        FadeTransition ft = new FadeTransition(Duration.millis(1000), titleText);
        ft.setFromValue(1);
        ft.setToValue(0);
        ft.setOnFinished(event -> FXGL.getGameScene().removeUINode(titleText));
        ft.play();

        FadeTransition ft2 = new FadeTransition(Duration.millis(1000), inputHint);
        ft2.setDelay(Duration.millis(500));
        ft2.setFromValue(1);
        ft2.setToValue(0);
        ft2.play();

        FadeTransition ft3 = new FadeTransition(Duration.millis(1000), attributionNote);
        ft3.setDelay(Duration.millis(1000));
        ft3.setFromValue(1);
        ft3.setToValue(0);
        ft3.play();

        JPMGApp.activeInstance.stateManager.isTransitioning = true;

        // FadeTransitions with a delay for some reason call the onFinish handler
        // to early, so a PauseTransition is used to remove the inputHint
        // after the fade-out is actually done
        PauseTransition pt = new PauseTransition(Duration.millis(2000));
        pt.setOnFinished(event -> {
            JPMGApp.activeInstance.stateManager.isTransitioning = false;
            FXGL.getGameScene().removeUINode(inputHint);
            FXGL.getGameScene().removeUINode(attributionNote);
        });
        pt.play();

        added = false;
    }
}
