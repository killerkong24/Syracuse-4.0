package org.mapleir.dot4j.helper.utils;

public class Animation {

    private float end, current;
    private boolean done;

    public Animation(int start, int end) {
        this.end = end;
        this.current = start;
        done = false;
    }

    public void update() {
        update(false, 20);
    }

    public void update(boolean cast) {
        update(cast, 20);
    }

    public void update(boolean cast, float speed) {
        if (current != end) {
            current += ((end - current) / speed);
            if (cast) current = (int) current;
            if (Math.round(current) == end) current = end;
        } else done = true;
    }

    public boolean hasEnded() {
        return done;
    }

    public float getValue() {
        return current;
    }

    public void setValue(float current) {
        this.current = current;
    }

    public float getEnd() {
        return end;
    }

    public void setEnd(float end) {
        this.end = end;
        done = false;
    }
}
