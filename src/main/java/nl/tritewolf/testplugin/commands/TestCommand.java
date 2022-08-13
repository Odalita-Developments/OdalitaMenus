package nl.tritewolf.testplugin.commands;

import nl.tritewolf.testplugin.TestPlayerMenu;
import nl.tritewolf.testplugin.TestPlugin;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class TestCommand implements CommandExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        Player player = (Player) commandSender;

        if (strings.length > 0 && strings[0].equalsIgnoreCase("test")) {
            int xAxis = (strings.length > 1) ? Integer.parseInt(strings[1]) : 0;
            int yAxis = (strings.length > 2) ? Integer.parseInt(strings[2]) : 0;

            TestPlugin.getTriteMenus().getMenuProcessor().openMenuBuilder(new TestPlayerMenu(44), player)
                    .scrollable("test", xAxis, yAxis)
                    .open();
            return false;
        }

        TestPlugin.getTriteMenus().getMenuProcessor().openMenu(new TestPlayerMenu(), player);
        return false;
    }
}
