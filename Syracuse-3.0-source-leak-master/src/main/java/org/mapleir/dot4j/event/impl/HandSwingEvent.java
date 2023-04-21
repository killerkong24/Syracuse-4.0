package org.mapleir.dot4j.event.impl;

import net.minecraft.util.Hand;
import org.mapleir.dot4j.event.Event;

public class HandSwingEvent extends Event {

    private Hand hand;

    public HandSwingEvent(Hand hand) {
        this.hand = hand;
    }

    public Hand getHand() {
        return hand;
    }

    public void setHand(Hand hand) {
        this.hand = hand;
    }
}
