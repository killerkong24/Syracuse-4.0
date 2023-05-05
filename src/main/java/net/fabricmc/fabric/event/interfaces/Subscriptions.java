package net.fabricmc.fabric.event.interfaces;


import net.fabricmc.fabric.event.interfaces.impl.ISubWorldRenderEnd;
import net.fabricmc.fabric.event.interfaces.impl.ISubscription;

import java.util.ArrayList;
import java.util.List;

public class Subscriptions {
    public static final List<ISubWorldRenderEnd> WORLD_RENDER_END = new ArrayList<>();

    public static void addSub(ISubscription s) {
        if (s instanceof ISubWorldRenderEnd) {
            WORLD_RENDER_END.add((ISubWorldRenderEnd) s);
        }
    }
}