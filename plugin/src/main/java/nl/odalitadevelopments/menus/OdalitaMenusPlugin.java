package nl.odalitadevelopments.menus;

import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

public final class OdalitaMenusPlugin extends JavaPlugin {

    public static @NotNull OdalitaMenusPlugin getInstance() {
        return JavaPlugin.getPlugin(OdalitaMenusPlugin.class);
    }

    private OdalitaMenus odalitaMenus;

    @Override
    public void onEnable() {
        this.odalitaMenus = OdalitaMenus.createInstance(this);
    }

    public @NotNull OdalitaMenus getOdalitaMenus() {
        return this.odalitaMenus;
    }
}