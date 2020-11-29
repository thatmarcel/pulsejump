package com.thatmarcel.jpmg.helpers.platform;

import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.Entity;
import com.thatmarcel.jpmg.helpers.platform.internal.PlatformBlockFactory;
import com.thatmarcel.jpmg.helpers.platform.internal.PlatformBlockType;
import javafx.util.Duration;

import java.util.ArrayList;

public class PlatformRow {
    public ArrayList<Entity> blocks;

    public int length = 1;
    public int rows = 1;

    public Boolean shouldAllowLeadingTexture = true;
    public Boolean shouldAllowTrailingTexture = true;
    public Boolean shouldFadeIn = false;
    public Duration fadeInDelay = Duration.ZERO;
    public int startXPosition = 0;
    public int distance = 0;

    public PlatformRow() {
        blocks = new ArrayList<>();
    }

    public static PlatformRow withLength(int length) {
        PlatformRow platformRow = new PlatformRow();
        platformRow.length = length;
        return platformRow;
    }

    public PlatformRow withRows(int rows) {
        this.rows = rows;
        return this;
    }

    public PlatformRow enableFadeInWithDelay(Duration delay) {
        this.shouldFadeIn = true;
        this.fadeInDelay = delay;
        return this;
    }

    public PlatformRow enableFadeIn() {
        this.shouldFadeIn = true;
        return this;
    }

    public PlatformRow disableFadeIn() {
        this.shouldFadeIn = false;
        return this;
    }

    public PlatformRow enableLeadingTexture() {
        this.shouldAllowLeadingTexture = true;
        return this;
    }

    public PlatformRow disableLeadingTexture() {
        this.shouldAllowLeadingTexture = false;
        return this;
    }

    public PlatformRow enableTrailingTexture() {
        this.shouldAllowTrailingTexture = true;
        return this;
    }

    public PlatformRow disableTrailingTexture() {
        this.shouldAllowTrailingTexture = false;
        return this;
    }

    public PlatformRow withStartX(int startX) {
        this.startXPosition = startX;
        return this;
    }

    public PlatformRow after(PlatformRow platformRow) {
        this.startXPosition = platformRow.getEnd();
        return this;
    }

    public PlatformRow withDistance(int distance) {
        this.distance = distance;
        return this;
    }

    public PlatformRow attach() {
        Factory factory = new Factory();
        factory.length = length;
        factory.rows = rows;
        factory.startXPosition = startXPosition;
        factory.distance = distance;
        factory.shouldAllowLeadingTexture = shouldAllowLeadingTexture;
        factory.shouldAllowTrailingTexture = shouldAllowTrailingTexture;
        blocks = factory.produce();

        for (Entity block : blocks) {
            block.setZ(-100);
            FXGL.getGameWorld().addEntity(block);
            if (shouldFadeIn) {
                block.setOpacity(0);
            }
        }

        if (shouldFadeIn) {
            FXGL.animationBuilder()
                    .delay(fadeInDelay)
                    .duration(Duration.seconds(1))
                    .fade(blocks)
                    .from(0)
                    .to(1)
                    .buildAndPlay();
        }

        return this;
    }

    public int getEnd() {
        return startXPosition + distance + (length * PlatformBlockFactory.size);
    }

    public void remove() {
        for (Entity block : blocks) {
            block.removeFromWorld();
        }

        blocks = new ArrayList<>();
    }

    static class Factory {
        public int length = 1;
        public int rows = 1;
        public int startXPosition = 0;
        public int distance = 0;
        public Boolean shouldAllowLeadingTexture;
        public Boolean shouldAllowTrailingTexture;

        public ArrayList<Entity> produce() {
            ArrayList<Entity> blocks = new ArrayList<>();

            int appHeight = FXGL.getAppHeight();

            for (int i = 0; i < (length * rows); i++) {
                double row = 1 + (i % rows);
                int index = i / rows;

                PlatformBlockType blockType = PlatformBlockType.DIRT;

                if (row == rows) {
                    blockType = PlatformBlockType.GRASS;
                }

                Entity platformBlock = PlatformBlockFactory
                        .start()
                        .withStart(shouldAllowLeadingTexture && index == 0)
                        .withEnd(shouldAllowTrailingTexture && index == length - 1)
                        .withType(blockType)
                        .produce();
                platformBlock.setY(appHeight - (platformBlock.getHeight() * row));
                platformBlock.setX(startXPosition + distance + (platformBlock.getWidth() * index));

                blocks.add(platformBlock);
            }

            return blocks;
        }
    }

}

