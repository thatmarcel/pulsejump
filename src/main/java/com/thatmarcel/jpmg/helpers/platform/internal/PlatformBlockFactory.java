package com.thatmarcel.jpmg.helpers.platform.internal;

import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.physics.BoundingShape;
import com.almasb.fxgl.physics.HitBox;
import com.almasb.fxgl.physics.PhysicsComponent;
import com.almasb.fxgl.texture.Texture;
import com.thatmarcel.jpmg.helpers.config.Config;
import com.thatmarcel.jpmg.helpers.types.EntityType;
import javafx.geometry.Point2D;

public class PlatformBlockFactory {
    public Boolean isStart;
    public Boolean isEnd;

    public static final int size = Config.Objects.Platform.size;

    public PlatformBlockType type = PlatformBlockType.GRASS;

    public static PlatformBlockFactory start() {
        return new PlatformBlockFactory();
    }

    public PlatformBlockFactory withStart(Boolean isStart) {
        this.isStart = isStart;
        return this;
    }

    public PlatformBlockFactory withEnd(Boolean isEnd) {
        this.isEnd = isEnd;
        return this;
    }

    public PlatformBlockFactory withType(PlatformBlockType type) {
        this.type = type;
        return this;
    }

    public Entity produce() {
        String textureName;

        switch (type) {
            case DIRT:
                if (isStart) {
                    textureName = "dirt-left-1.png";
                } else if (isEnd) {
                    textureName = "dirt-right-1.png";
                } else {
                    textureName = "dirt-mid-1.png";
                }
                break;
            case GRASS:
                if (isStart) {
                    textureName = "grass-platform-left-1.png";
                } else if (isEnd) {
                    textureName = "grass-platform-right-1.png";
                } else {
                    textureName = "grass-platform-mid-1.png";
                }
                break;
            default:
                return null;
        }

        Texture platformTexture = FXGL.getAssetLoader().loadTexture(textureName);
        platformTexture.setFitWidth(size);
        platformTexture.setFitHeight(size);

        BoundingShape boundingShape = getShape();
        HitBox hitBox = new HitBox(boundingShape);

        return FXGL.entityBuilder()
                .zIndex(-1)
                .type(EntityType.PLATFORM_BLOCK)
                .bbox(hitBox)
                .view(platformTexture)
                .with(new PhysicsComponent())
                .collidable()
                .build();
    }

    public static BoundingShape getShape() {
        return BoundingShape.chain(
                new Point2D(0, size * 0.15),
                new Point2D(size, size * 0.15),
                new Point2D(size, size),
                new Point2D(0, size)
        );
    }
}
