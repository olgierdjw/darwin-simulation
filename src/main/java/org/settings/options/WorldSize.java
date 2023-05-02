package org.settings.options;

import org.settings.GameOption;

public class WorldSize extends GameOption<Integer> {


    @Override
    protected Integer getDefaultValue() {
        return 14;
    }

    @Override
    protected boolean validator(Integer validate) {
        return validate > 0 && validate < 19;
    }

}
