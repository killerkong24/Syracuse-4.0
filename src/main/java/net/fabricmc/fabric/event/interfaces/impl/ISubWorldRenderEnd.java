package net.fabricmc.fabric.event.interfaces.impl;


import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderContext;

public interface ISubWorldRenderEnd extends ISubscription {
    void onWorldRenderEnd(WorldRenderContext ctx);
}