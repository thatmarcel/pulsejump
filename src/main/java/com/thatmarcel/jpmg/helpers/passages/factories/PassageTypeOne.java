package com.thatmarcel.jpmg.helpers.passages.factories;

import com.almasb.fxgl.dsl.EntityBuilder;
import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.physics.PhysicsComponent;
import com.thatmarcel.jpmg.JPMGApp;
import com.thatmarcel.jpmg.helpers.config.Config;
import com.thatmarcel.jpmg.helpers.passages.internal.GenericPassage;
import com.thatmarcel.jpmg.helpers.passages.internal.PassageTypeOneType;
import com.thatmarcel.jpmg.helpers.platform.PlatformRow;
import com.thatmarcel.jpmg.helpers.player.MovementType;
import com.thatmarcel.jpmg.helpers.player.Player;
import com.thatmarcel.jpmg.helpers.position.RelativePosition;
import com.thatmarcel.jpmg.helpers.pulse.PulseAction;
import com.thatmarcel.jpmg.helpers.pulse.internal.PulseType;
import com.thatmarcel.jpmg.helpers.state.GameState;
import com.thatmarcel.jpmg.helpers.state.StateManager;
import com.thatmarcel.jpmg.helpers.types.EntityType;
import javafx.geometry.Point2D;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

import java.util.ArrayList;

@SuppressWarnings("IntegerDivisionInFloatingPointContext")
public class PassageTypeOne implements GenericPassage {
    public static PassageTypeOne create() {
        return new PassageTypeOne();
    }

    public ArrayList<PlatformRow> platformRows;

    public int startXPosition = 0;
    public int distance = Config.Passages.TypeOne.defaultExternalDistance;

    public PassageTypeOneType type;

    public Entity finishDetector;
    public Entity actionDetector;

    PassageTypeOne() {
        type = PassageTypeOneType.random();
        platformRows = new ArrayList<>();
    }

    @Override
    public PassageTypeOne attach() {
        platformRows.add(
                PlatformRow
                        .withLength(Config.Passages.TypeOne.platformLength)
                        .withStartX(startXPosition)
                        .withDistance(distance)
                        .withRows(type == PassageTypeOneType.UP_DOWN ? 3 : 1)
                        .disableLeadingTexture()
        );

        platformRows.add(
                PlatformRow
                        .withLength(Config.Passages.TypeOne.platformLength)
                        .after(platformRows.get(0))
                        .withDistance(Config.Passages.TypeOne.internalDistance)
                        .withRows(type == PassageTypeOneType.UP_DOWN ? 1 : 3)
                        .disableTrailingTexture()
        );

        platformRows.forEach(PlatformRow::attach);

        finishDetector = new EntityBuilder()
                .type(EntityType.PASSAGE_FINISH_MARK)
                .at(new Point2D(platformRows.get(0).getEnd() + distance, 0))
                .viewWithBBox(new Rectangle(1, FXGL.getAppHeight(), Color.TRANSPARENT))
                .collidable()
                .buildAndAttach();

        actionDetector = new EntityBuilder()
                .type(EntityType.PASSAGE_ACTION_MARK)
                .at(new Point2D(platformRows.get(0).getEnd() - (Config.Passages.TypeOne.internalDistance / 2), 0))
                .viewWithBBox(new Rectangle(1, FXGL.getAppHeight(), Color.TRANSPARENT))
                .collidable()
                .buildAndAttach();

        return this;
    }

    @Override
    public PassageTypeOne remove() {
        platformRows.forEach(PlatformRow::remove);

        if (finishDetector != null) {
            finishDetector.removeFromWorld();
            finishDetector= null;
        }

        if (actionDetector != null) {
            actionDetector.removeFromWorld();
            actionDetector = null;
        }

        return this;
    }

    @Override
    public PassageTypeOne withStartX(int startX) {
        this.startXPosition = startX;
        return this;
    }

    @Override
    public PassageTypeOne after(GenericPassage passage) {
        this.startXPosition = passage.getEnd();

        if (passage instanceof PassageTypeOne) {
            this.type = ((PassageTypeOne) passage).type == PassageTypeOneType.DOWN_UP ? PassageTypeOneType.UP_DOWN : PassageTypeOneType.DOWN_UP;
            this.distance = 0;
        }

        return this;
    }

    @Override
    public PassageTypeOne withDistance(int distance) {
        this.distance = distance;
        return this;
    }

    @Override
    public void removeFinishDetector() {
        this.finishDetector.removeFromWorld();
        this.finishDetector = null;
    }

    @Override
    public void removeActionDetector() {
        this.actionDetector.removeFromWorld();
        this.actionDetector = null;
    }

    @Override
    public void action() {
        if (Player.activeInstance == null) {
            return;
        }

        Player.activeInstance.move(MovementType.IDLE);

        PulseAction.create().start(type == PassageTypeOneType.UP_DOWN ? PulseType.DOWN : PulseType.UP, success -> {
            if (success) {
                Player.activeInstance.move(type == PassageTypeOneType.UP_DOWN ? MovementType.FAR_JUMP : MovementType.HIGH_FAR_JUMP);
            } else {
                JPMGApp.activeInstance.stateManager.activateState(GameState.MAIN_MENU);
            }
        });
    }

    @Override
    public int getEnd() {
        return platformRows.get(platformRows.size() - 1).getEnd();
    }
}
