package nl.odalitadevelopments.menus.nms.v1_16_R3;

import io.netty.channel.Channel;
import io.papermc.paper.text.PaperComponents;
import net.minecraft.server.v1_16_R3.*;
import nl.odalitadevelopments.menus.nms.OdalitaMenusNMS;
import nl.odalitadevelopments.menus.nms.utils.OdalitaLogger;
import nl.odalitadevelopments.menus.nms.utils.PaperHelper;
import nl.odalitadevelopments.menus.nms.utils.ReflectionUtils;
import org.bukkit.craftbukkit.v1_16_R3.entity.CraftPlayer;
import org.bukkit.craftbukkit.v1_16_R3.inventory.CraftInventory;
import org.bukkit.craftbukkit.v1_16_R3.inventory.CraftItemStack;
import org.bukkit.craftbukkit.v1_16_R3.util.CraftChatMessage;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.lang.reflect.Field;
import java.util.List;

public final class OdalitaMenusNMS_v1_16_R5 implements OdalitaMenusNMS {

    private static Class<?> MINECRAFT_INVENTORY;

    private static Field MINECRAFT_INVENTORY_TITLE_FIELD;
    private static Field PAPER_MINECRAFT_INVENTORY_TITLE_FIELD;
    private static Field TITLE_FIELD;
    private static Field WINDOW_ID_FIELD;

    static {
        try {
            MINECRAFT_INVENTORY = ReflectionUtils.obcClass("inventory.CraftInventoryCustom$MinecraftInventory");

            MINECRAFT_INVENTORY_TITLE_FIELD = MINECRAFT_INVENTORY.getDeclaredField("title");

            if (PaperHelper.IS_PAPER) {
                PAPER_MINECRAFT_INVENTORY_TITLE_FIELD = MINECRAFT_INVENTORY.getDeclaredField("adventure$title");
            }

            TITLE_FIELD = Container.class.getDeclaredField("title");
            WINDOW_ID_FIELD = Container.class.getDeclaredField(ObfuscatedNames_v1_16_R5.WINDOW_ID);
        } catch (Exception exception) {
            OdalitaLogger.error(exception);
        }
    }

    @Override
    public Object itemStackToNMS(ItemStack itemStack) {
        return CraftItemStack.asNMSCopy(itemStack);
    }

    @Override
    public ItemStack itemStackFromNMS(Object item) {
        return CraftItemStack.asBukkitCopy((net.minecraft.server.v1_16_R3.ItemStack) item);
    }

    @Override
    public Channel getPacketChannel(Player player) {
        EntityPlayer serverPlayer = ((CraftPlayer) player).getHandle();
        return serverPlayer.playerConnection.networkManager.channel;
    }

    @Override
    public void sendPacket(Player player, Object packetObject) {
        if (!(packetObject instanceof Packet<?> packet)) {
            throw new IllegalArgumentException("Packet inside packet wrapper is not an instance of a minecraft packet!");
        }

        EntityPlayer serverPlayer = ((CraftPlayer) player).getHandle();
        serverPlayer.playerConnection.sendPacket(packet);
    }

    @Override
    public IChatBaseComponent createChatBaseComponent(String string) {
        return CraftChatMessage.fromJSONOrNull("{\"text\":\"" + string + "\"}");
    }

    @Override
    public synchronized void setInventoryItem(Player player, int slot, ItemStack itemStack, Inventory inventory) {
        EntityPlayer serverPlayer = ((CraftPlayer) player).getHandle();
        Container activeContainer = serverPlayer.activeContainer;
        int windowId = activeContainer.windowId;
        if (activeContainer instanceof ContainerPlayer || windowId <= 0) return;

        net.minecraft.server.v1_16_R3.ItemStack nmsItemStack = CraftItemStack.asNMSCopy(itemStack);

        IInventory nmsInventory = ((CraftInventory) inventory).getInventory();
        List<net.minecraft.server.v1_16_R3.ItemStack> contents = nmsInventory.getContents();
        if (contents.size() <= slot) {
            return;
        }

        contents.set(slot, nmsItemStack);

        PacketPlayOutSetSlot packet = new PacketPlayOutSetSlot(windowId, slot, nmsItemStack);
        this.sendPacket(player, packet);
    }

    @Override
    public synchronized void changeInventoryTitle(Inventory inventory, String title) throws Exception {
        if (inventory.getViewers().isEmpty()) {
            IInventory nmsInventory = ((CraftInventory) inventory).getInventory();
            Object minecraftInventory = MINECRAFT_INVENTORY.cast(nmsInventory);

            MINECRAFT_INVENTORY_TITLE_FIELD.setAccessible(true);
            MINECRAFT_INVENTORY_TITLE_FIELD.set(minecraftInventory, title);
            MINECRAFT_INVENTORY_TITLE_FIELD.setAccessible(false);

            if (PaperHelper.IS_PAPER) {
                PAPER_MINECRAFT_INVENTORY_TITLE_FIELD.setAccessible(true);
                PAPER_MINECRAFT_INVENTORY_TITLE_FIELD.set(minecraftInventory, PaperComponents.plainSerializer().deserialize(title));
                PAPER_MINECRAFT_INVENTORY_TITLE_FIELD.setAccessible(false);
            }

            return;
        }

        IChatBaseComponent titleComponent = this.createChatBaseComponent(title);

        for (HumanEntity viewer : inventory.getViewers()) {
            if (!(viewer instanceof Player player)) continue;

            EntityPlayer serverPlayer = ((CraftPlayer) player).getHandle();
            Container activeContainer = serverPlayer.activeContainer;
            int windowId = activeContainer.windowId;
            if (windowId <= 0) continue;

            Containers<?> type = activeContainer.getType();

            PacketPlayOutOpenWindow packet = new PacketPlayOutOpenWindow(windowId, type, titleComponent);
            this.sendPacket(player, packet);

            TITLE_FIELD.setAccessible(true);
            TITLE_FIELD.set(activeContainer, titleComponent);
            TITLE_FIELD.setAccessible(false);

            serverPlayer.a(activeContainer, activeContainer.items);
        }
    }

    @Override
    public synchronized void setInventoryProperty(Inventory inventory, int propertyIndex, int value) {
        for (HumanEntity viewer : inventory.getViewers()) {
            if (!(viewer instanceof Player player)) continue;

            EntityPlayer serverPlayer = ((CraftPlayer) player).getHandle();
            Container activeContainer = serverPlayer.activeContainer;

            activeContainer.a(propertyIndex, value);
        }
    }

    @Override
    public void openInventory(Player player, Object inventory, String title) throws Exception {
        if (inventory instanceof Inventory bukkitInventory) {
            player.openInventory(bukkitInventory);
            return;
        }

        Container nmsInventory = (Container) inventory;

        EntityPlayer serverPlayer = ((CraftPlayer) player).getHandle();

        int windowId = serverPlayer.nextContainerCounter();
        WINDOW_ID_FIELD.setAccessible(true);
        WINDOW_ID_FIELD.set(nmsInventory, windowId);
        WINDOW_ID_FIELD.setAccessible(false);

        serverPlayer.activeContainer = nmsInventory;

        Containers<?> type = nmsInventory.getType();
        IChatBaseComponent titleComponent = this.createChatBaseComponent(title);

        PacketPlayOutOpenWindow packet = new PacketPlayOutOpenWindow(windowId, type, titleComponent);
        this.sendPacket(player, packet);

        serverPlayer.syncInventory();
    }

    @Override
    public Object createAnvilInventory(Player player) {
        EntityPlayer serverPlayer = ((CraftPlayer) player).getHandle();
        PlayerInventory playerInventory = serverPlayer.inventory;

        ContainerAnvil anvilMenu = new ContainerAnvil(-1, playerInventory);
        anvilMenu.checkReachable = false;

        return anvilMenu;
    }

    @Override
    public Object createCartographyInventory(Player player) {
        EntityPlayer serverPlayer = ((CraftPlayer) player).getHandle();
        PlayerInventory playerInventory = serverPlayer.inventory;

        ContainerCartography cartographyTableMenu = new ContainerCartography(-1, playerInventory, ContainerAccess.at(serverPlayer.getWorldServer(), serverPlayer.getChunkCoordinates()));
        cartographyTableMenu.checkReachable = false;

        return cartographyTableMenu;
    }

    @Override
    public Object createCraftingInventory(Player player) {
        EntityPlayer serverPlayer = ((CraftPlayer) player).getHandle();
        PlayerInventory playerInventory = serverPlayer.inventory;

        ContainerWorkbench craftingMenu = new ContainerWorkbench(-1, playerInventory, ContainerAccess.at(serverPlayer.getWorldServer(), serverPlayer.getChunkCoordinates()));
        craftingMenu.checkReachable = false;

        return craftingMenu;
    }

    @Override
    public Object createEnchantingInventory(Player player) {
        EntityPlayer serverPlayer = ((CraftPlayer) player).getHandle();
        PlayerInventory playerInventory = serverPlayer.inventory;

        ContainerEnchantTable enchantmentMenu = new ContainerEnchantTable(-1, playerInventory, ContainerAccess.at(serverPlayer.getWorldServer(), serverPlayer.getChunkCoordinates()));
        enchantmentMenu.checkReachable = false;

        return enchantmentMenu;
    }

    @Override
    public Object createLoomInventory(Player player) {
        EntityPlayer serverPlayer = ((CraftPlayer) player).getHandle();
        PlayerInventory playerInventory = serverPlayer.inventory;

        ContainerLoom loomMenu = new ContainerLoom(-1, playerInventory);
        loomMenu.checkReachable = false;

        return loomMenu;
    }

    @Override
    public Object createSmithingInventory(Player player) {
        EntityPlayer serverPlayer = ((CraftPlayer) player).getHandle();
        PlayerInventory playerInventory = serverPlayer.inventory;

        ContainerSmithing smithingMenu = new ContainerSmithing(-1, playerInventory);
        smithingMenu.checkReachable = false;

        return smithingMenu;
    }

    @Override
    public Object createStonecutterInventory(Player player) {
        EntityPlayer serverPlayer = ((CraftPlayer) player).getHandle();
        PlayerInventory playerInventory = serverPlayer.inventory;

        ContainerStonecutter stonecutterMenu = new ContainerStonecutter(-1, playerInventory);
        stonecutterMenu.checkReachable = false;

        return stonecutterMenu;
    }

    @Override
    public Inventory getInventoryFromNMS(Object nmsInventory) {
        if (!(nmsInventory instanceof Container menu)) {
            return null;
        }

        return menu.getBukkitView().getTopInventory();
    }
}