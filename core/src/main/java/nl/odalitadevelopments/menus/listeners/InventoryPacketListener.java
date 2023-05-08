package nl.odalitadevelopments.menus.listeners;

import nl.odalitadevelopments.menus.OdalitaMenus;
import nl.odalitadevelopments.menus.contents.action.PlayerInventoryLoreApplier;
import nl.odalitadevelopments.menus.menu.MenuProcessor;
import nl.odalitadevelopments.menus.menu.MenuSession;
import nl.odalitadevelopments.menus.providers.providers.PacketListenerProvider;
import nl.odalitadevelopments.menus.utils.packet.OdalitaSetSlotPacket;
import nl.odalitadevelopments.menus.utils.packet.OdalitaWindowItemsPacket;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public final class InventoryPacketListener {

    private final OdalitaMenus instance;
    private final MenuProcessor menuProcessor;

    public InventoryPacketListener(OdalitaMenus instance, MenuProcessor menuProcessor) {
        this.instance = instance;
        this.menuProcessor = menuProcessor;

        this.listenSetSlot();
        this.listenWindowItems();
    }

    private void listenSetSlot() {
        this.instance.getProvidersContainer().getPacketListenerProvider().interceptClientbound(PacketListenerProvider.ClientboundPacketType.SET_SLOT, (player, odalitaMenuPacket) -> {
            if (!(odalitaMenuPacket instanceof OdalitaSetSlotPacket packet)) return false;

            MenuSession menuSession = this.menuProcessor.getOpenMenuSession(player);
            if (menuSession == null) return false;

            PlayerInventoryLoreApplier loreApplier = menuSession.getCache().getLoreApplier();
            if (loreApplier == null) return false; // No lore applier, no need to intercept

            ItemStack itemStack = packet.item();
            if (isEmpty(itemStack)) return false;

            int slot = packet.slot();
            if (slot < 0) return false;

            loreApplier.apply(slot, itemStack);
            return false;
        });
    }

    private void listenWindowItems() {
        this.instance.getProvidersContainer().getPacketListenerProvider().interceptClientbound(PacketListenerProvider.ClientboundPacketType.WINDOW_ITEMS, (player, odalitaMenuPacket) -> {
            if (!(odalitaMenuPacket instanceof OdalitaWindowItemsPacket packet)) return false;

            MenuSession menuSession = this.menuProcessor.getOpenMenuSession(player);
            if (menuSession == null) return false;

            PlayerInventoryLoreApplier loreApplier = menuSession.getCache().getLoreApplier();
            if (loreApplier == null) return false; // No lore applier, no need to intercept

            List<@NotNull ItemStack> items = packet.items();

            int topInventorySize = menuSession.getInventory().getSize();
            for (int i = topInventorySize; i < items.size(); i++) {
                ItemStack itemStack = items.get(i);
                if (isEmpty(itemStack)) continue;

                loreApplier.apply(i, itemStack);
            }

            return false;
        });
    }

    private boolean isEmpty(ItemStack itemStack) {
        return itemStack == null || itemStack.getType() == Material.AIR || itemStack.getAmount() <= 0;
    }
}