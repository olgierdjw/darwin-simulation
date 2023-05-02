package org.settings.options;

import org.settings.GameOption;

public class StartingEnergy extends GameOption<Integer> {


    @Override
    protected Integer getDefaultValue() {
        return 9;
    }

    @Override
    protected boolean validator(Integer validate) {
        return validate > 0 && validate < 1000;
    }
}
