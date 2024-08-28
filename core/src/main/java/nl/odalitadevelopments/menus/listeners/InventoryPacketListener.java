package nl.odalitadevelopments.menus.listeners;

import nl.odalitadevelopments.menus.OdalitaMenus;
import nl.odalitadevelopments.menus.contents.action.PlayerInventoryItemMetaChanger;
import nl.odalitadevelopments.menus.menu.MenuProcessor;
import nl.odalitadevelopments.menus.menu.MenuSession;
import nl.odalitadevelopments.menus.providers.providers.PacketListenerProvider;
import nl.odalitadevelopments.menus.utils.packet.OdalitaSetSlotPacket;
import nl.odalitadevelopments.menus.utils.packet.OdalitaSetContentsPacket;
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
        this.instance.getProvidersContainer().getPacketListenerProvider().listenClientbound(PacketListenerProvider.ClientboundPacketType.SET_SLOT, (player, odalitaMenuPacket) -> {
            if (!(odalitaMenuPacket instanceof OdalitaSetSlotPacket packet)) return;

            MenuSession menuSession = this.menuProcessor.getOpenMenuSession(player);
            if (menuSession == null) return;

            PlayerInventoryItemMetaChanger itemMetaChanger = menuSession.getCache().getItemMetaChanger();
            if (itemMetaChanger == null) return;

            ItemStack itemStack = packet.item();
            if (isEmpty(itemStack)) return;

            int slot = packet.slot();
            int topInventorySize = menuSession.getInventory().getSize();
            if (slot < topInventorySize) return;

            itemMetaChanger.apply(this.convertSlot(topInventorySize, slot), itemStack);
        });
    }

    private void listenWindowItems() {
        this.instance.getProvidersContainer().getPacketListenerProvider().listenClientbound(PacketListenerProvider.ClientboundPacketType.SET_CONTENTS, (player, odalitaMenuPacket) -> {
            if (!(odalitaMenuPacket instanceof OdalitaSetContentsPacket packet)) return;

            MenuSession menuSession = this.menuProcessor.getOpenMenuSession(player);
            if (menuSession == null) return;

            PlayerInventoryItemMetaChanger itemMetaChanger = menuSession.getCache().getItemMetaChanger();
            if (itemMetaChanger == null) return;

            List<@NotNull ItemStack> items = packet.items();

            int topInventorySize = menuSession.getInventory().getSize();
            for (int i = topInventorySize; i < items.size(); i++) {
                ItemStack itemStack = items.get(i);
                if (isEmpty(itemStack)) continue;

                itemMetaChanger.apply(this.convertSlot(topInventorySize, i), itemStack);
            }
        });
    }

    private boolean isEmpty(ItemStack itemStack) {
        return itemStack == null || itemStack.getType() == Material.AIR || itemStack.getAmount() <= 0;
    }

    private int convertSlot(int topSize, int rawSlot) {
        // Index from the top inventory as having slots from [0,size]
        if (rawSlot < topSize) {
            return rawSlot;
        }

        // Move down the slot index by the top size
        int slot = rawSlot - topSize;

        // 27 = 36 - 9
        if (slot >= 27) {
            // Put into hotbar section
            slot -= 27;
        } else {
            // Take out of hotbar section
            // 9 = 36 - 27
            slot += 9;
        }

        return slot;
    }
}