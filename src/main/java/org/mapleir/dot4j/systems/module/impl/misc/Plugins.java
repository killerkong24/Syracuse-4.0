package org.mapleir.dot4j.systems.module.impl.misc;

import com.mojang.brigadier.suggestion.Suggestion;
import net.minecraft.network.packet.c2s.play.RequestCommandCompletionsC2SPacket;
import net.minecraft.network.packet.s2c.play.CommandSuggestionsS2CPacket;
import org.mapleir.dot4j.event.EventTarget;
import org.mapleir.dot4j.event.impl.CommandSuggestEvent;
import org.mapleir.dot4j.helper.utils.PacketHelper;
import org.mapleir.dot4j.systems.module.core.Category;
import org.mapleir.dot4j.systems.module.core.Module;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Module.Info(name = "Plugins", description = "Get the plugins on the server", category = Category.MISC)

public class Plugins extends Module {

    @Override
    public void onEnable() {
        super.onEnable();
        if (mc.player != null) {
            PacketHelper.sendPacket(new RequestCommandCompletionsC2SPacket(new Random().nextInt(200), "/"));
        }
    }


    @EventTarget
    public void onCmdSuggest(CommandSuggestEvent event) {
        CommandSuggestionsS2CPacket packet = event.getPacket();
        List<String> plugins = new ArrayList<>();
        List<Suggestion> cmdList = packet.getSuggestions().getList();

        for (Suggestion s : cmdList) {
            String[] cmd = s.getText().split(":");

            if (cmd.length > 1) {
                String pluginName = cmd[0].replace("/", "");

                if (!plugins.contains(pluginName)) {
                    plugins.add(pluginName);
                }
            }
        }

        if (!plugins.isEmpty()) {
            StringBuilder sb = new StringBuilder();
            for (String s : plugins) {
                if (s.equalsIgnoreCase("itemeditor")) {
                    sb.append("§c").append(s).append(", §a");
                } else if (s.equalsIgnoreCase("bettershulkerboxes")) {
                    sb.append("§c").append(s).append(", §a");
                } else if (s.equalsIgnoreCase("vulcan") || s.equalsIgnoreCase("ncp")) {
                    sb.append("§b").append(s).append(", §a");
                } else sb.append(s).append(", ");
            }

            sendMsg("&7Plugins: &a" + sb);
        } else {
            sendMsg("&cNo plugins found!");
        }


        toggle();
    }
}
