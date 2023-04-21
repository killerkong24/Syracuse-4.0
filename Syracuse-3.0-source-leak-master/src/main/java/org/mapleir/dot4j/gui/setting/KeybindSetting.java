package org.mapleir.dot4j.gui.setting;

public class KeybindSetting extends Setting {

    private int key;
    private boolean enabled;

    public KeybindSetting(String name, int defaultKey) {
        super(name);
        this.key = defaultKey;
    }

    public int getKey() {
        return key;
    }

    public void setKey(int key) {
        this.key = key;
    }

    public void toggle() {
        this.enabled = !this.enabled;
    }
}