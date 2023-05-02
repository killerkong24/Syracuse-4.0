package org.mapleir.dot4j.event.impl;

import org.mapleir.dot4j.event.Event;

public class EventUpdate extends Event {

    private static final EventUpdate instance = new EventUpdate();

    public static EventUpdate get() {
        instance.setCancelled(false);
        return instance;
    }
}
