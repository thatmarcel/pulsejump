package com.thatmarcel.jpmg.helpers.misc;

import com.thatmarcel.jpmg.helpers.state.GameState;

public interface GenericManager {
    void initialize();
    void activateState(GameState state);
}
