package net.fabricmc.fabric.command;

import net.fabricmc.fabric.command.impl.Bind;
import net.fabricmc.fabric.command.impl.Vclip;

import java.util.ArrayList;
import java.util.List;

public class CommandManager {

    public static CommandManager INSTANCE = new CommandManager();
    private final List<Command> cmds = new ArrayList<>();

    public CommandManager() {
        init();
    }

    private void init() {
        add(new Bind());
        add(new Vclip());
    }

    public void add(Command command) {
        if (!cmds.contains(command)) {
            cmds.add(command);
        }
    }

    public void remove(Command command) {
        cmds.remove(command);
    }

    public List<Command> getCmds() {
        return cmds;
    }
}