package nl.odalitadevelopments.menus.examples.anvil_input_menu.usage;

import nl.odalitadevelopments.menus.OdalitaMenus;
import nl.odalitadevelopments.menus.examples.anvil_input_menu.AnvilInputMenu;
import org.bukkit.entity.Player;

public class AnvilInputMenuUsage {

    public void openAnvilInputMenu(OdalitaMenus instance, Player player) {
        instance.openMenu(new AnvilInputMenu((input) -> {
            player.sendMessage("You entered: " + input);
        }), player);
    }
}