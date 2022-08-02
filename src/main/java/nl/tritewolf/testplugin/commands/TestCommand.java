package nl.tritewolf.testplugin.commands;

import nl.tritewolf.testplugin.TestPlayerMenu;
import nl.tritewolf.testplugin.TestPlugin;
import nl.tritewolf.tritemenus.utils.Pair;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Set;

public class TestCommand implements CommandExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        Player player = (Player) commandSender;

        if (strings.length > 0 && strings[0].equalsIgnoreCase("test")) {
            TestPlugin.getTriteMenus().getMenuProcessor().openMenuBuilder(new TestPlayerMenu(44), player)
                    .paginationPages(() -> Set.of(
                            Pair.of("test", 1)
                    ))
                    .open();
            return false;
        }

        TestPlugin.getTriteMenus().getMenuProcessor().openMenu(new TestPlayerMenu(), player);
        return false;
    }
}
