package com.thatmarcel.jpmg.helpers.ui.internal;

import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.texture.ColoredTexture;
import com.almasb.fxgl.texture.Texture;
import com.almasb.fxgl.ui.FontFactory;
import com.thatmarcel.jpmg.helpers.position.RelativePosition;
import com.thatmarcel.jpmg.helpers.pulse.PulseAction;
import com.thatmarcel.jpmg.helpers.pulse.internal.PulseType;
import com.thatmarcel.jpmg.helpers.strings.Strings;
import javafx.animation.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.util.Duration;

@SuppressWarnings("DuplicatedCode")
public class PulseActionUI {
    private Boolean added = false;

    private Text titleText;
    private Texture backgroundTexture;

    public void add() {
        if (added) {
            return;
        }

        setupBackground();
        setupTitleText();

        added = true;
    }

    private void setupBackground() {
        backgroundTexture = new ColoredTexture(FXGL.getAppWidth(), FXGL.getAppHeight(), Color.PINK);
        backgroundTexture.setOpacity(0);
        FXGL.getGameScene().addUINode(backgroundTexture);

        FadeTransition ft = new FadeTransition(Duration.seconds(10), backgroundTexture);
        ft.setFromValue(0);
        ft.setToValue(0.6);
        ft.play();
    }

    private void setupTitleText() {
        int xStart = RelativePosition.combinedHalfPercentage(12);
        int yStart = RelativePosition.combinedHalfPercentage(30);

        titleText = new Text();
        titleText.setStyle("-fx-fill: darkred");
        titleText.setOpacity(0);
        titleText.setText(PulseAction.activeInstance.requestedType == PulseType.UP ? Strings.higherPulseRequestTitle : Strings.lowerPulseRequestTitle);
        int inputHintYPosition = yStart + RelativePosition.combinedHalfPercentage(3);
        titleText.setTranslateX(xStart + RelativePosition.combinedHalfPercentage(2));
        titleText.setTranslateY(inputHintYPosition);
        titleText.setRotate(-6);
        FXGL.getGameScene().addUINode(titleText);

        FontFactory fontFactory = FXGL.getAssetLoader().loadFont("Teko-Bold.ttf");
        Font font = fontFactory.newFont(RelativePosition.combinedHalfPercentage(12));
        titleText.setFont(font);

        Timeline timeline = new Timeline();
        timeline.setAutoReverse(true);
        timeline.setCycleCount(Animation.INDEFINITE);
        KeyValue keyValueX = new KeyValue(titleText.scaleXProperty(), 0.8);
        KeyValue keyValueY = new KeyValue(titleText.scaleYProperty(), 0.8);
        timeline.getKeyFrames().add(new KeyFrame(Duration.seconds(1), event -> {} , keyValueX, keyValueY));
        timeline.play();

        FadeTransition ft = new FadeTransition(Duration.seconds(1), titleText);
        ft.setFromValue(0);
        ft.setToValue(1);
        ft.play();
    }

    public void forceRemove() {
        if (!added) {
            return;
        }

        FXGL.getGameScene().removeUINode(titleText);
        FXGL.getGameScene().removeUINode(backgroundTexture);

        added = false;
    }

    public void remove() {
        if (!added) {
            return;
        }

        FadeTransition ft = new FadeTransition(Duration.millis(1000), titleText);
        ft.setFromValue(1);
        ft.setToValue(0);
        ft.setOnFinished(event -> FXGL.getGameScene().removeUINode(titleText));
        ft.play();

        FadeTransition ft2 = new FadeTransition(Duration.millis(1000), backgroundTexture);
        ft2.setFromValue(1);
        ft2.setToValue(0);
        ft2.setOnFinished(event -> FXGL.getGameScene().removeUINode(backgroundTexture));
        ft2.play();

        added = false;
    }
}
