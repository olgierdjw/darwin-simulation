package org.settings.options;

import org.settings.GameOption;

public class GenomeLengthOption extends GameOption<Integer> {


    @Override
    protected Integer getDefaultValue() {
        return 5;
    }

    @Override
    protected boolean validator(Integer validate) {
        return true;
    }
}
