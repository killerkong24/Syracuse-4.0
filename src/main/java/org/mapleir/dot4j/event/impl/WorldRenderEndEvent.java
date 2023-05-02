package org.mapleir.dot4j.event.impl;

import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderContext;
import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderEvents;
import org.mapleir.dot4j.event.interfaces.Subscriptions;
import org.mapleir.dot4j.event.interfaces.impl.IDispatch;
import org.mapleir.dot4j.event.interfaces.impl.ISubWorldRenderEnd;

public class WorldRenderEndEvent {
    public static void init() {
        WorldRenderEvents.END.register(WorldRenderEndEvent::dispatchEnd);
    }

    private static void dispatchEnd(WorldRenderContext ctx) {
        for (ISubWorldRenderEnd iswre : Subscriptions.WORLD_RENDER_END) {
            if (iswre instanceof IDispatch d && !d.shouldDispatch()) continue;
            iswre.onWorldRenderEnd(ctx);
        }
    }
}
