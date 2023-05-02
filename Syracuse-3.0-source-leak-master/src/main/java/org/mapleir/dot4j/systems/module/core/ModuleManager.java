package org.mapleir.dot4j.systems.module.core;

import org.mapleir.dot4j.event.interfaces.Subscriptions;
import org.mapleir.dot4j.event.interfaces.impl.ISubscription;
import org.mapleir.dot4j.systems.module.impl.Cpvp.*;
import org.mapleir.dot4j.systems.module.impl.KillerKlient.Arraylist;
import org.mapleir.dot4j.systems.module.impl.KillerKlient.HUD;
import org.mapleir.dot4j.systems.module.impl.KillerKlient.Spoofer;
import org.mapleir.dot4j.systems.module.impl.KillerKlient.Theme;
import org.mapleir.dot4j.systems.module.impl.combat.*;
import org.mapleir.dot4j.systems.module.impl.misc.Plugins;
import org.mapleir.dot4j.systems.module.impl.misc.SelfDestruct;
import org.mapleir.dot4j.systems.module.impl.movement.*;
import org.mapleir.dot4j.systems.module.impl.player.Criticals;
import org.mapleir.dot4j.systems.module.impl.player.NoFall;
import org.mapleir.dot4j.systems.module.impl.player.NoSlow;
import org.mapleir.dot4j.systems.module.impl.render.ESP;

import java.util.ArrayList;
import java.util.List;

public class ModuleManager {

    public static final ModuleManager INSTANCE = new ModuleManager();
    private List<Module> modules = new ArrayList<>();

    public ModuleManager() {
        init();
    }

    private void init() {

        // RENDER MODULES:
        add(new Arraylist());
        add(new ESP());
        add(new HUD());
        add(new Theme());
        // CPVP MODULES
        add(new AutoCrystal());
        add(new AutoTotem());
        add(new CrystalPop());
        add(new CrystalPlace());

        // COMBAT MODULES
        add(new AimAssist());
        add(new AutoPot());
        add(new AutoStun());
        add(new Criticals());
        add(new Hitboxes());
        add(new InfiniteReach());
        add(new Reach());
        add(new KillAura());

        // MOVEMENT MODULES
        add(new Speed());
        add(new AirJump());
        add(new InventoryMove());
        add(new Sprint());
        add(new Step());
        add(new Strafe());
        add(new Fly());
        add(new LegitScaffold());
        add(new FastFall());
        add(new FakeLag());

        // MISC MODULES
        add(new Plugins());
        add(new SelfDestruct());
        add(new Spoofer());

        // PLAYER MODULES
        add(new NoSlow());
        add(new NoFall());

    }

    public void add(Module m) {
        modules.add(m);
        if (m instanceof ISubscription) {
            Subscriptions.addSub((ISubscription) m);
        }
    }

    public void remove(Module m) {
        modules.remove(m);
    }

    public List<Module> getModules() {
        return modules;
    }

    public void setModules(List<Module> modules) {
        this.modules = modules;
    }

    public Module getModuleByName(String name) {
        for (Module module : modules) {
            if (module.getName().toLowerCase().equals(name.toLowerCase())) return module;
        }

        return null;
    }



    public ArrayList<Module> getModulesByCategory(Category category) {
        ArrayList<Module> modules = new ArrayList<>();
        for (Module m : this.modules) {
            if (m.getCategory().equals(category)) {
                modules.add(m);
            }
        }
        return modules;
    }

    public Module getModuleByClass(Class<? extends Module> cls) {
        for (Module m : modules) {
            if (m.getClass() != cls) {
                continue;
            }
            return m;
        }
        return null;
    }

    public List<Module> getEnabledModules() {
        List<Module> enabled = new ArrayList<>();
        for (Module m : getModules()) {
            if (m.isEnabled()) {
                enabled.add(m);
            }
        }

        return enabled;
    }
}
