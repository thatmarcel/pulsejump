package com.thatmarcel.jpmg.helpers.config;

import com.thatmarcel.jpmg.helpers.position.RelativePosition;
import javafx.scene.input.KeyCode;
import javafx.util.Duration;

public class Config {
    public static class Window {
        public static final Boolean shouldUseFullscreen = false;

        // Only used when the game is not running in fullscreen
        public static final int width = 1280;
        public static final int height = 720;

        public static class ConnectionPopup {
            public static final int width = 600;
            public static final int height = 300;
        }
    }

    public static class Controls {
        public static final KeyCode confirmKey = KeyCode.ENTER;
        public static final KeyCode menuKey = KeyCode.ESCAPE;

        public static final KeyCode jumpKey = KeyCode.SPACE;
        public static final KeyCode leftKey = KeyCode.LEFT;
        public static final KeyCode rightKey = KeyCode.RIGHT;
    }

    public static class Objects {
        public static class Platform {
            public static final int size = RelativePosition.combinedHalfPercentage(12);
        }
    }

    public static class Player {
        public static final int size = RelativePosition.combinedHalfPercentage(12);

        public static final int jumps = 2;
        public static final int jumpHeight = RelativePosition.combinedHalfPercentage(76);
        public static final Duration jumpTimeout = Duration.seconds(0.3);

        public static double farJumpMultiplier = 1.25;
        public static final Duration farJumpDuration = jumpTimeout.multiply(4);
        public static final int farJumpXVelocity = RelativePosition.combinedHalfPercentage(50);

        public static double highFarJumpMultiplier = 1.75;
        public static final Duration highFarJumpDuration = jumpTimeout.multiply(4);
        public static final int highFarJumpXVelocity = RelativePosition.combinedHalfPercentage(50);
    }

    public static class Gameplay {
        public static final Boolean autoRun = true;
        public static final Boolean autoRunAllowsJumping = false;
    }

    public static class Passages {
        public static class TypeOne {
            public static int platformLength = 16;
            public static int internalDistance = RelativePosition.combinedHalfPercentage(32);
            public static int defaultExternalDistance = internalDistance;
        }
    }

    public static class PulseAction {
        public static Boolean isTesting = false;
    }

    public static class SerialConnection {
        public static Boolean isTesting = false;
    }
}
