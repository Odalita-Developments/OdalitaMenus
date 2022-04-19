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
        TestPlugin.getTriteMenus().getMenuProcessor().openMenu(TestPlayerMenu.class, player);
        return false;
    }
}
