package net.fabricmc.fabric.systems.module.impl.render;

import net.fabricmc.fabric.systems.module.core.Category;
import net.fabricmc.fabric.systems.module.core.Module;

@Module.Info(name = "NoHurtCam", description = "Disables hurt camera bobbing", category = Category.RENDER)

public class NoHurtCam extends Module {
    // handled in WorldRendererMixin
}
