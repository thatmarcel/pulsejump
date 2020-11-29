package com.thatmarcel.jpmg.helpers.passages.internal;

import com.thatmarcel.jpmg.helpers.passages.factories.PassageTypeOne;

@SuppressWarnings({"unused", "UnusedReturnValue"})
public interface GenericPassage {
    PassageTypeOne attach();
    PassageTypeOne remove();
    PassageTypeOne withStartX(int startX);
    PassageTypeOne after(GenericPassage passage);
    PassageTypeOne withDistance(int distance);
    void removeFinishDetector();
    void removeActionDetector();
    void action();
    int getEnd();
}
