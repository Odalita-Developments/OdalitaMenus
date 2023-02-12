package nl.odalitadevelopments.menus.examples.providers;

import nl.odalitadevelopments.menus.providers.providers.ColorProvider;
import org.bukkit.ChatColor;
import org.jetbrains.annotations.NotNull;

public final class CustomColorProvider implements ColorProvider {

    @Override
    public @NotNull String handle(@NotNull String value) {
        return ChatColor.translateAlternateColorCodes('&', value); // Use anything you want here to colorize a text
    }
}