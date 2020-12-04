package com.thatmarcel.jpmg.helpers.ui.internal;

import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.texture.Texture;
import com.almasb.fxgl.ui.FontFactory;
import com.thatmarcel.jpmg.helpers.position.RelativePosition;
import com.thatmarcel.jpmg.helpers.pulse.PulseAction;
import com.thatmarcel.jpmg.helpers.pulse.internal.PulseType;
import com.thatmarcel.jpmg.helpers.strings.Strings;
import javafx.animation.FadeTransition;
import javafx.animation.ScaleTransition;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.util.Duration;

@SuppressWarnings("DuplicatedCode")
public class InGameUI {
    private Boolean added = false;

    private Texture heartTexture;
    private Text bpmText;

    private final int xStart = RelativePosition.combinedHalfPercentage(4);
    private final int yStart = RelativePosition.combinedHalfPercentage(4);

    public void add() {
        if (added) {
            return;
        }

        setupHeartTexture();
        setupBPMText();

        added = true;
    }

    public void updateBPM(Integer bpm) {
        if (added) {
            bpmText.setText(bpm.toString());
        }
    }

    public void doHeartbeat() {
        if (!added) {
            return;
        }

        ScaleTransition st1 = new ScaleTransition(Duration.seconds(0.1), heartTexture);
        st1.setFromX(1);
        st1.setFromY(1);
        st1.setToX(1.25);
        st1.setToY(1.25);
        st1.play();

        ScaleTransition st2 = new ScaleTransition(Duration.seconds(0.2), heartTexture);
        st2.setDelay(Duration.seconds(0.1));
        st2.setFromX(1.25);
        st2.setFromY(1.25);
        st2.setToX(1);
        st2.setToY(1);
        st2.play();
    }

    @SuppressWarnings("IntegerDivisionInFloatingPointContext")
    private void setupHeartTexture() {
        heartTexture = FXGL.getAssetLoader().loadTexture("heart-1.png");
        heartTexture.setOpacity(0);
        heartTexture.setFitHeight(RelativePosition.combinedHalfPercentage(2) * 1.25);
        heartTexture.setFitWidth(RelativePosition.combinedHalfPercentage(2) * 1.25);
        heartTexture.setTranslateX(xStart);
        heartTexture.setTranslateY(yStart - (RelativePosition.combinedHalfPercentage(1) / 2));
        FXGL.getGameScene().addUINode(heartTexture);

        FadeTransition ft = new FadeTransition(Duration.seconds(1), heartTexture);
        ft.setDelay(Duration.seconds(1));
        ft.setFromValue(0);
        ft.setToValue(1);
        ft.play();
    }

    private void setupBPMText() {
        bpmText = new Text();
        bpmText.setOpacity(0);
        bpmText.setStyle("-fx-fill: #e15241");
        bpmText.setText("--");
        bpmText.setTranslateX(xStart + RelativePosition.combinedHalfPercentage(4));
        bpmText.setTranslateY(yStart * 1.475);
        FXGL.getGameScene().addUINode(bpmText);

        FontFactory fontFactory = FXGL.getAssetLoader().loadFont(FXGL.getSettings().getFontUI());
        Font font = fontFactory.newFont(RelativePosition.combinedHalfPercentage(3));
        bpmText.setFont(font);

        FadeTransition ft = new FadeTransition(Duration.seconds(1), bpmText);
        ft.setDelay(Duration.seconds(1));
        ft.setFromValue(0);
        ft.setToValue(1);
        ft.play();
    }

    public void remove() {
        if (!added) {
            return;
        }

        FXGL.getGameScene().removeUINode(heartTexture);
        FXGL.getGameScene().removeUINode(bpmText);

        added = false;
    }
}
