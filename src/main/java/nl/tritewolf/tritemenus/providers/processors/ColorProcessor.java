package nl.tritewolf.tritemenus.providers.processors;


import nl.tritewolf.tritemenus.providers.providers.ColorProvider;
import org.bukkit.ChatColor;
import org.jetbrains.annotations.NotNull;

final class ColorProcessor implements ColorProvider {

    @Override
    public @NotNull String handle(@NotNull String input) {
        return ChatColor.translateAlternateColorCodes('&', input);
    }
}
