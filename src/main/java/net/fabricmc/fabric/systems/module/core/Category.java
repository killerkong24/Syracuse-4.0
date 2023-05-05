package net.fabricmc.fabric.systems.module.core;

public enum Category {
    KILLERKLIENT("KLIENT"),
    CPVP("CPVP"),
    COMBAT("Combat"),
    MOVEMENT("Movement"),
    PLAYER("Player"),
    WORLD("World"),
    RENDER("Render"),
    MISC("Misc");

    public String name;

    Category(String name) {
        this.name = name;
    }
}
