package org.settings;

public abstract class GameOption<T> {
    abstract protected T getDefaultValue();

    protected T value = getDefaultValue();

    abstract protected boolean validator(T validate);

    public void setValue(T newValue) {
        if (this.validator(newValue)) value = newValue;
        else System.out.println("Default value used.");
    }

    public T getValue() {
        return value;
    }
}
