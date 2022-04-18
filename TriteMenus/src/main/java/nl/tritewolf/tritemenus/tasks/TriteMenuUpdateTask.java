package nl.tritewolf.tritemenus.tasks;

import net.minecraft.server.v1_16_R3.EntityPlayer;
import net.minecraft.server.v1_16_R3.ItemStack;
import net.minecraft.server.v1_16_R3.PacketPlayOutSetSlot;
import nl.tritewolf.tritejection.annotations.TriteJect;
import nl.tritewolf.tritemenus.contents.TriteSlotPos;
import nl.tritewolf.tritemenus.items.TriteMenuItem;
import nl.tritewolf.tritemenus.items.TriteUpdatableItem;
import nl.tritewolf.tritemenus.menu.TriteMenuObject;
import nl.tritewolf.tritemenus.menu.TriteMenuProcessor;
import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_16_R3.entity.CraftPlayer;
import org.bukkit.craftbukkit.v1_16_R3.inventory.CraftItemStack;
import org.bukkit.entity.Player;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;

public class TriteMenuUpdateTask implements Runnable {

    @TriteJect
    private TriteMenuProcessor triteMenuProcessor;

    private static final AtomicInteger TICKS = new AtomicInteger(0);

    @Override
    public void run() {
        int ticks = TICKS.incrementAndGet();

        for (Map.Entry<UUID, Map<Class<?>, TriteMenuObject>> menus : triteMenuProcessor.getMenus().entrySet()) {
            TriteMenuObject triteMenuObject = menus.getValue().values().stream().filter(menuObject -> menuObject.isHasUpdatableItems() && menuObject.isHasMenuOpened()).findFirst().orElse(null);
            Player player = Bukkit.getPlayer(menus.getKey());

            if (triteMenuObject != null && player != null && player.isOnline()) {
                for (Map.Entry<TriteSlotPos, TriteMenuItem> menuItemEntry : triteMenuObject.getContents().entrySet()) {
                    if (menuItemEntry.getValue() instanceof TriteUpdatableItem) {
                        TriteUpdatableItem updatableItem = (TriteUpdatableItem) menuItemEntry.getValue();

                        if (ticks % updatableItem.getUpdateTicks() == 0) {
                            int slot = menuItemEntry.getKey().getSlot();
                            org.bukkit.inventory.ItemStack item = menuItemEntry.getValue().getItemStack();

                            this.updateItem(player, slot, item);
                        }
                    }
                }
            }
        }
    }

    private void updateItem(Player player, int slot, org.bukkit.inventory.ItemStack itemStack) {
        EntityPlayer handle = ((CraftPlayer) player).getHandle();
        int windowId = handle.activeContainer.windowId;

        ItemStack copy = CraftItemStack.asNMSCopy(itemStack);

        PacketPlayOutSetSlot pack = new PacketPlayOutSetSlot(windowId, slot, copy);
        handle.playerConnection.sendPacket(pack);
    }
}