package nl.odalitadevelopments.menus.providers.processors;


import nl.odalitadevelopments.menus.providers.providers.ColorProvider;
import org.bukkit.ChatColor;
import org.jetbrains.annotations.NotNull;

public final class ColorProcessor implements ColorProvider {

    @Override
    public @NotNull String handle(@NotNull String input) {
        return ChatColor.translateAlternateColorCodes('&', input);
    }
}