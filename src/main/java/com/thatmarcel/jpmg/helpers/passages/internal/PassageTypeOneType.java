package com.thatmarcel.jpmg.helpers.passages.internal;

import java.util.Random;

public enum PassageTypeOneType {
    UP_DOWN,
    DOWN_UP;

    public static PassageTypeOneType random() {
        return (new Random().nextInt(2) == 0) ? UP_DOWN : DOWN_UP;
    }
}
