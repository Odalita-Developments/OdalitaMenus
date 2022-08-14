package nl.tritewolf.testplugin;

import lombok.Getter;
import nl.tritewolf.testplugin.commands.TestCommand;
import nl.tritewolf.testplugin.sb.TestExtraMenuProvider;
import nl.tritewolf.tritemenus.TriteMenus;
import org.bukkit.plugin.java.JavaPlugin;

public class TestPlugin extends JavaPlugin {

    @Getter
    private static TriteMenus triteMenus;

    @Override
    public void onEnable() {
        triteMenus = new TriteMenus(this);

        triteMenus.getMenuProcessor().registerProviderLoader(TestExtraMenuProvider.class, TestExtraMenuProvider.loader());

        triteMenus.getPatternContainer().registerPattern(new TestScrollablePattern());

        getCommand("TestCommand").setExecutor(new TestCommand());
    }
}
