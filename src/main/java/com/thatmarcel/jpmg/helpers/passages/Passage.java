package com.thatmarcel.jpmg.helpers.passages;

import com.thatmarcel.jpmg.helpers.passages.factories.PassageTypeOne;
import com.thatmarcel.jpmg.helpers.passages.internal.GenericPassage;

public class Passage {
    public static GenericPassage random() {
        return PassageTypeOne.create();
    }
}
