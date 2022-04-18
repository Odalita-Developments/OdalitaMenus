package nl.tritewolf.tritemenus.tasks;

import net.minecraft.server.v1_16_R3.EntityPlayer;
import net.minecraft.server.v1_16_R3.ItemStack;
import net.minecraft.server.v1_16_R3.PacketPlayOutSetSlot;
import nl.tritewolf.tritejection.annotations.TriteJect;
import nl.tritewolf.tritemenus.contents.TriteSlotPos;
import nl.tritewolf.tritemenus.items.TriteMenuItem;
import nl.tritewolf.tritemenus.menu.TriteMenuObject;
import nl.tritewolf.tritemenus.menu.TriteMenuProcessor;
import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_16_R3.entity.CraftPlayer;
import org.bukkit.craftbukkit.v1_16_R3.inventory.CraftItemStack;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Collection;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Collectors;

public class TriteMenuUpdateTask extends BukkitRunnable {

    @TriteJect
    private TriteMenuProcessor triteMenuProcessor;

    @Override
    public void run() {
        for (Map.Entry<UUID, Map<Class<?>, TriteMenuObject>> menus : triteMenuProcessor.getMenus().entrySet()) {
            TriteMenuObject triteMenuObject = menus.getValue().values().stream().filter(menuObject -> menuObject.isHasUpdatableItems() && menuObject.isHasMenuOpened()).findFirst().orElse(null);
            if (triteMenuObject != null) {
                for (Map.Entry<TriteSlotPos, TriteMenuItem> menuItemEntry : triteMenuObject.getContents().entrySet()) {

                    triteMenuObject.getInventory().setItem(menuItemEntry.getKey().getSlot(), menuItemEntry.getValue().getItemStack());
                }
            }
        }

    }

    public static void updateItem(Player player, int slot, org.bukkit.inventory.ItemStack itemStack) {
        EntityPlayer handle = ((CraftPlayer) player).getHandle();

        ItemStack copy = CraftItemStack.asNMSCopy(itemStack);

        PacketPlayOutSetSlot pack = new PacketPlayOutSetSlot(-1, slot, copy);
        handle.playerConnection.sendPacket(pack);
    }
}
