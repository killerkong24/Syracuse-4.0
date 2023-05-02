package org.mapleir.dot4j.systems.module.impl.render;

import org.mapleir.dot4j.systems.module.core.Category;
import org.mapleir.dot4j.systems.module.core.Module;

@Module.Info(name = "NoHurtCam", description = "Disables hurt camera bobbing", category = Category.RENDER)

public class NoHurtCam extends Module {
    // handled in WorldRendererMixin
}
