package org.mapleir.dot4j.gui.setting;

public class NumberSetting extends Setting {

    private final double min;
    private final double max;
    private final double increment;
    private double value;

    public NumberSetting(String name, double min, double max, double defaultValue, double increment) {
        super(name);
        this.min = min;
        this.max = max;
        this.value = defaultValue;
        this.increment = increment;
    }

    public double clamp(double value, double min, double max) {
        value = Math.max(min, value);
        value = Math.min(max, value);
        return value;
    }

    public double getValue() {
        return value;
    }

    public void setValue(double value) {
        value = clamp(value, this.min, this.max);
        value = Math.round(value * (1.0 / this.increment)) / (1.0 / this.increment);
        this.value = value;
    }

    public float getFloatValue() {
        return (float) value;
    }

    public void increment(boolean positive) {
        if (positive) {
            setValue(getValue() + getIncrement());
        } else {
            setValue(getFloatValue() - getIncrement());
        }
    }

    public double getMin() {
        return min;
    }

    public double getMax() {
        return max;
    }

    public double getIncrement() {
        return increment;
    }
}
