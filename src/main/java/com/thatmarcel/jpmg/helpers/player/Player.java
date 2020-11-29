package com.thatmarcel.jpmg.helpers.player;

import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.physics.BoundingShape;
import com.almasb.fxgl.physics.HitBox;
import com.almasb.fxgl.physics.PhysicsComponent;
import com.almasb.fxgl.physics.box2d.dynamics.BodyType;
import com.almasb.fxgl.physics.box2d.dynamics.FixtureDef;
import com.almasb.fxgl.texture.AnimatedTexture;
import com.almasb.fxgl.texture.AnimationChannel;
import com.thatmarcel.jpmg.helpers.config.Config;
import com.thatmarcel.jpmg.helpers.position.RelativePosition;
import com.thatmarcel.jpmg.helpers.types.EntityType;
import javafx.geometry.Point2D;
import javafx.scene.image.Image;
import javafx.util.Duration;

@SuppressWarnings({"FieldCanBeLocal", "FieldMayBeFinal", "IntegerDivisionInFloatingPointContext"})
public class Player {
    public Entity entity;
    public PhysicsComponent physics;

    public static final int size = Config.Player.size;

    // Use 4x texture for better sharpness on high DPI displays (JavaFX does smooth upscaling so normal upscaling looks bad for pixel art)
    public static final int textureScaleFactor = 4;

    public Boolean shouldFadeIn = false;
    public Duration fadeInDelay = Duration.ZERO;

    public static Player activeInstance;

    private Image textureImage;
    private AnimationChannel walkingAnimationChannel;
    private AnimationChannel idleAnimationChannel;
    private AnimatedTexture animatedTexture;

    private int availableJumps = Config.Player.jumps;
    private Boolean canStartNextJump = true;

    public Boolean autoRun = Config.Gameplay.autoRun;

    public static Player create() {
        return new Player();
    }

    Player() {
        activeInstance = this;

        int aspectRatio = 570 / 18;

        // Use 4x texture for better sharpness on high DPI displays (JavaFX does smooth upscaling so normal upscaling looks bad for pixel art)
        int textureHeight = size * textureScaleFactor;
        int textureWidth = textureHeight * aspectRatio;

        textureImage = FXGL.image("dino-red.png", textureWidth, textureHeight);

        walkingAnimationChannel = new AnimationChannel(
                textureImage,
                24,
                textureWidth / 24,
                textureHeight,
                Duration.seconds(0.5),
                4,
                9
        );

        idleAnimationChannel = new AnimationChannel(
                textureImage,
                24,
                textureWidth / 24,
                textureHeight,
                Duration.seconds(1),
                0,
                0
        );

        animatedTexture = new AnimatedTexture(idleAnimationChannel);

        // Scale texture down 4x to make it original size again
        animatedTexture.setTranslateX(-(size * (textureScaleFactor - 1) / 2) - 2 * (size * (1 / (double) textureScaleFactor)));
        animatedTexture.setTranslateY(-(size * (textureScaleFactor - 1) / 2));
        animatedTexture.setScaleX(1 / (double) textureScaleFactor);
        animatedTexture.setScaleY(1 / (double) textureScaleFactor);
        animatedTexture.setScaleZ(1 / (double) textureScaleFactor);

        animatedTexture.play();

        /* HitBox headBox = new HitBox(new Point2D(size * 0.3,0), BoundingShape.box(size * 0.7, size * 0.5));
        HitBox legBox = new HitBox(new Point2D(size * 0.3,size * 0.5), BoundingShape.box(size * 0.4, size * 0.6)); */

        HitBox hitBox = new HitBox(new Point2D(size * 0.07,0), BoundingShape.box(size * 0.4, size));

        entity = FXGL.entityBuilder()
                .zIndex(100)
                .type(EntityType.PLAYER)
                .bbox(hitBox)
                .view(animatedTexture)
                .collidable()
                .build();

        entity.setX(RelativePosition.combinedHalfPercentage(6));
        entity.setY(RelativePosition.heightPercentage(50));

        FXGL.getGameScene().getViewport().bindToEntity(entity, entity.getX() + RelativePosition.combinedHalfPercentage(32), entity.getY());
        FXGL.getGameScene().getViewport().setLazy(true);
        FXGL.getGameScene().getViewport().setBounds(0, 0, Integer.MAX_VALUE, FXGL.getAppHeight());

        physics = new PhysicsComponent();
        physics.setBodyType(BodyType.DYNAMIC);
        FixtureDef fd = new FixtureDef();
        /* fd.setDensity(2f);
        fd.setRestitution(0f); */
        fd.setFriction(0f);
        physics.setFixtureDef(fd);

        FXGL.onCollision(EntityType.PLAYER, EntityType.PLATFORM_BLOCK, (player, platform) -> {
            availableJumps = Config.Player.jumps;
        });

        entity.addComponent(physics);
    }

    public void move(MovementType movementType) {
        if (entity == null || physics == null) {
            return;
        }

        switch (movementType) {
            case IDLE:
                physics.setVelocityX(0);
                switchToIdle();
                break;
            case LEFT:
                entity.setScaleX(-1);
                physics.setVelocityX(-RelativePosition.combinedHalfPercentage(28));

                switchToWalking();
                break;
            case RIGHT:
                entity.setScaleX(1);
                physics.setVelocityX(RelativePosition.combinedHalfPercentage(28));
                switchToWalking();
                break;
            case JUMP:
                if (availableJumps > 0 && canStartNextJump) {
                    physics.setVelocityY(-Config.Player.jumpHeight);

                    canStartNextJump = false;
                    FXGL.getGameTimer().runOnceAfter(() -> {
                        availableJumps--;
                        canStartNextJump = true;
                    }, Config.Player.jumpTimeout);
                }
                break;
            case FAR_JUMP:
                if (availableJumps >= 2 && canStartNextJump) {
                    physics.setVelocityY(-(Config.Player.jumpHeight * Config.Player.farJumpMultiplier));
                    physics.setVelocityX(Config.Player.farJumpXVelocity);

                    canStartNextJump = false;
                    FXGL.getGameTimer().runOnceAfter(() -> {
                        availableJumps -= 2;
                        canStartNextJump = true;
                        move(MovementType.RIGHT);
                    }, Config.Player.farJumpDuration);
                }
                break;
            case HIGH_FAR_JUMP:
                if (availableJumps >= 2 && canStartNextJump) {
                    physics.setVelocityY(-(Config.Player.jumpHeight * Config.Player.highFarJumpMultiplier));
                    physics.setVelocityX(Config.Player.highFarJumpXVelocity);

                    canStartNextJump = false;
                    FXGL.getGameTimer().runOnceAfter(() -> {
                        availableJumps -= 2;
                        canStartNextJump = true;
                        move(MovementType.RIGHT);
                    }, Config.Player.highFarJumpDuration);
                }
                break;
        }
    }

    private void switchToIdle() {
        /* animatedTexture.setOnCycleFinished(() -> {
            animatedTexture.playAnimationChannel(idleAnimationChannel);
        }); */
        if (animatedTexture.getAnimationChannel() != idleAnimationChannel) {
            animatedTexture.playAnimationChannel(idleAnimationChannel);
        }
    }

    private void switchToWalking() {
        if (animatedTexture.getAnimationChannel() != walkingAnimationChannel) {
            animatedTexture.loopAnimationChannel(walkingAnimationChannel);
        }
    }

    public Player enableFadeInWithDelay(Duration delay) {
        this.shouldFadeIn = true;
        this.fadeInDelay = delay;
        return this;
    }

    public Player enableFadeIn() {
        this.shouldFadeIn = true;
        return this;
    }

    public Player disableFadeIn() {
        this.shouldFadeIn = false;
        return this;
    }

    public Player attach() {
        FXGL.getGameWorld().addEntity(entity);

        if (shouldFadeIn) {
            if (fadeInDelay == null) {
                fadeInDelay = Duration.ZERO;
            }

            FXGL.animationBuilder()
                    .delay(fadeInDelay)
                    .duration(Duration.seconds(1))
                    .fadeIn(entity)
                    .buildAndPlay();
        }

        if (autoRun) {
            FXGL.getGameTimer().runOnceAfter(() -> move(MovementType.RIGHT), Duration.seconds(1));
        }

        return this;
    }

    public void remove() {
        FXGL.getGameScene().getViewport().unbind();

        entity.removeFromWorld();
        entity = null;
        physics = null;

        activeInstance = null;
    }
}
