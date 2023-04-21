package org.mapleir.dot4j.gui.clickgui.components;

import net.minecraft.client.util.math.MatrixStack;
import org.mapleir.dot4j.gui.clickgui.ClickGUI;
import org.mapleir.dot4j.gui.setting.ModeSetting;
import org.mapleir.dot4j.helper.utils.RenderUtils;
import org.mapleir.dot4j.helper.utils.Theme;

public class ModeComp extends Component {

    private final ModeSetting setting;
    private final ClickGUI parent;
    private final float x;
    private float y;

    public ModeComp(ModeSetting setting, ClickGUI parent, float x, float y) {
        this.setting = setting;
        this.parent = parent;
        this.x = x;
        this.y = y;
    }

    @Override
    public void drawScreen(MatrixStack matrices, int mouseX, int mouseY, float delta) {
        RenderUtils.renderRoundedQuad(matrices, x + parent.windowX + 445 + parent.settingsFieldX, y + 2, x + parent.windowX + parent.width - 5, y + 22, 2, 20, Theme.MODE_SETTING_BG);
        RenderUtils.renderRoundedQuad(matrices, x + parent.windowX + 446 + parent.settingsFieldX, y + 3, x + parent.windowX + parent.width - 6, y + 21, 2, 20, Theme.MODE_SETTING_FILL);
        mc.textRenderer.draw(matrices, setting.getName() + ":" + setting.getMode(), x + parent.windowX + 455 + parent.settingsFieldX, y + 10, Theme.NORMAL_TEXT_COLOR.getRGB());
        mc.textRenderer.draw(matrices, ">", x + parent.windowX + parent.width - 15, y + 9, Theme.MODULE_TEXT.getRGB());
    }

    @Override
    public void mouseClicked(double mouseX, double mouseY, int button) {

        if (isHovered(x + parent.windowX + 445 + parent.settingsFieldX, y + 2, x + parent.windowX + parent.width - 5, y + 22, mouseX, mouseY) && button == 0) {
            setting.cycle();
        }
    }

    public void setY(float y) {
        this.y = y;
    }
}
