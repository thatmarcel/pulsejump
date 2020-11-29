package com.thatmarcel.jpmg.helpers.position;

import com.almasb.fxgl.dsl.FXGL;
import com.thatmarcel.jpmg.JPMGApp;

public class RelativePosition {
    public static Integer widthPercentage(Integer percentage) {
        return FXGL.getAppWidth() / 100 * percentage;
    }

    public static Integer heightPercentage(Integer percentage) {
        return FXGL.getAppHeight() / 100 * percentage;
    }

    /**
     * @param units Number of units
     * @return A number used to measure the screen in responsive units (used to make the game look the same on different resolutions)
     */
    public static Integer combinedHalfPercentage(Integer units) {
        return (FXGL.getAppHeight() + FXGL.getAppWidth()) / 200 * units;
    }
}
