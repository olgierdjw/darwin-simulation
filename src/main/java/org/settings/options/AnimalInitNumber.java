package org.settings.options;

import org.settings.GameOption;

public class AnimalInitNumber extends GameOption<Integer> {

    @Override
    protected Integer getDefaultValue() {
        return 14;
    }

    @Override
    protected boolean validator(Integer validate) {
        return validate < 40;
    }
}
