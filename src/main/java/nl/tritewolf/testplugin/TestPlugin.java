package nl.tritewolf.testplugin;

import lombok.Getter;
import nl.tritewolf.testplugin.commands.TestCommand;
import nl.tritewolf.tritemenus.TriteMenus;
import org.bukkit.plugin.java.JavaPlugin;

public class TestPlugin extends JavaPlugin {

    @Getter
    private static TriteMenus triteMenus;

    @Override
    public void onEnable() {
        triteMenus = new TriteMenus(this);

        getCommand("TestCommand").setExecutor(new TestCommand());
    }
}
