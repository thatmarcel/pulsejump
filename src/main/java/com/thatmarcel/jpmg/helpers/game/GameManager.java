package com.thatmarcel.jpmg.helpers.game;

import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.dsl.views.ScrollingBackgroundView;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.physics.PhysicsComponent;
import com.almasb.fxgl.texture.ColoredTexture;
import com.thatmarcel.jpmg.helpers.misc.GenericManager;
import com.thatmarcel.jpmg.helpers.passages.Passage;
import com.thatmarcel.jpmg.helpers.passages.internal.GenericPassage;
import com.thatmarcel.jpmg.helpers.player.Player;
import com.thatmarcel.jpmg.helpers.position.RelativePosition;
import com.thatmarcel.jpmg.helpers.state.GameState;
import com.thatmarcel.jpmg.helpers.types.EntityType;
import javafx.animation.PauseTransition;
import javafx.geometry.Point2D;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;

import java.util.ArrayList;

public class GameManager implements GenericManager {
    private Boolean isBackgroundColored = false;

    private GameState lastState;

    private Player player;
    private Entity backgroundEntity;

    // Stops the player from leaving the game by walking left
    private Entity gameLeadingBlockEntity;

    public ArrayList<GenericPassage> passages;

    @Override
    public void initialize() {
        passages = new ArrayList<>();
    }

    @Override
    public void activateState(GameState state) {
        if (state == GameState.MAIN_MENU) {
            if (backgroundEntity != null) {
                backgroundEntity.removeFromWorld();
                backgroundEntity = null;
            }
            if (player != null) {
                player.remove();
                player = null;
            }
            if (gameLeadingBlockEntity != null) {
                gameLeadingBlockEntity.removeFromWorld();
                gameLeadingBlockEntity = null;
            }
            passages.forEach(GenericPassage::remove);
            isBackgroundColored = false;
            lastState = state;
            return;
        }

        if (lastState == GameState.MAIN_MENU) {
            PauseTransition pt = new PauseTransition(Duration.seconds(2));
            pt.setOnFinished(event -> loadGame());
            pt.play();
        } else {
            loadGame();
        }

        lastState = state;
    }

    private void loadGame() {
        loadBackground();

        FXGL.getPhysicsWorld().setGravity(0, RelativePosition.combinedHalfPercentage(180));

        player = Player
                .create()
                .enableFadeIn()
                .attach();

        passages.add(Passage.random().withDistance(0).attach());

        FXGL.onCollision(EntityType.PLAYER, EntityType.PASSAGE_FINISH_MARK, (player, platform) -> {
            GenericPassage lastPassage = passages.get(passages.size() - 1);
            lastPassage.removeFinishDetector();
            passages.add(Passage.random().after(lastPassage).attach());

            if (passages.size() >= 3) {
                passages.get(0).remove();
                passages.remove(0);
            }
        });

        FXGL.onCollision(EntityType.PLAYER, EntityType.PASSAGE_ACTION_MARK, (player, platform) -> {
            GenericPassage lastPassage = passages.get(passages.size() - 1);
            lastPassage.removeActionDetector();

            lastPassage.action();
        });
    }

    private void loadBackground() {
        if (isBackgroundColored) {
            return;
        }

        backgroundEntity = FXGL.entityBuilder()
                .view(new ScrollingBackgroundView(new ColoredTexture(FXGL.getAppWidth(), FXGL.getAppHeight(), Color.LIGHTBLUE)))
                .zIndex(-100)
                .buildAndAttach();

        gameLeadingBlockEntity = FXGL.entityBuilder()
                .at(new Point2D(Player.size * 0.3, 0))
                .viewWithBBox(new Rectangle(1, FXGL.getAppHeight(), Color.TRANSPARENT))
                .collidable()
                .with(new PhysicsComponent())
                .buildAndAttach();

        FXGL.animationBuilder()
                .duration(Duration.seconds(1))
                .fadeIn(backgroundEntity)
                .buildAndPlay();

        isBackgroundColored = true;
    }
}
