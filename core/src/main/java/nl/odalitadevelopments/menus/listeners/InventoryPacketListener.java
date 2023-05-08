package nl.odalitadevelopments.menus.listeners;

import nl.odalitadevelopments.menus.OdalitaMenus;
import nl.odalitadevelopments.menus.contents.action.PlayerInventoryLoreApplier;
import nl.odalitadevelopments.menus.menu.MenuProcessor;
import nl.odalitadevelopments.menus.menu.MenuSession;
import nl.odalitadevelopments.menus.providers.providers.PacketListenerProvider;

public final class InventoryPacketListener {

    private final OdalitaMenus instance;
    private final MenuProcessor menuProcessor;

    public InventoryPacketListener(OdalitaMenus instance, MenuProcessor menuProcessor) {
        this.instance = instance;
        this.menuProcessor = menuProcessor;

        this.listenWindowClick();
    }

    private void listenWindowClick() {
        this.instance.getProvidersContainer().getPacketListenerProvider().interceptServerbound(PacketListenerProvider.ServerboundPacketType.CLICK_WINDOW, (player, packet) -> {
            MenuSession menuSession = this.menuProcessor.getOpenMenuSession(player);
            if (menuSession == null) return false;

            PlayerInventoryLoreApplier loreApplier = menuSession.getCache().getLoreApplier();
            return loreApplier != null;
        });
    }
}