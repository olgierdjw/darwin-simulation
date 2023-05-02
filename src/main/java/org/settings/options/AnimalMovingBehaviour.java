package org.settings.options;

import org.animal.behaviour.AnimalMovingBehaviourEnum;
import org.settings.GameOption;

public class AnimalMovingBehaviour extends GameOption<AnimalMovingBehaviourEnum> {
    @Override
    protected AnimalMovingBehaviourEnum getDefaultValue() {
        return AnimalMovingBehaviourEnum.PREDESTINATION;
    }

    @Override
    protected boolean validator(AnimalMovingBehaviourEnum validate) {
        return true;
    }
}
