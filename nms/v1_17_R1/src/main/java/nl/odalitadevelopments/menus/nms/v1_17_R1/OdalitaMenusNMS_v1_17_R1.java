package nl.odalitadevelopments.menus.nms.v1_17_R1;

import io.netty.channel.Channel;
import io.papermc.paper.text.PaperComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientboundContainerSetContentPacket;
import net.minecraft.network.protocol.game.ClientboundContainerSetSlotPacket;
import net.minecraft.network.protocol.game.ClientboundOpenScreenPacket;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.Container;
import net.minecraft.world.inventory.*;
import nl.odalitadevelopments.menus.nms.OdalitaMenusNMS;
import nl.odalitadevelopments.menus.nms.packet.ClientboundSetContentsPacket;
import nl.odalitadevelopments.menus.nms.packet.ClientboundSetSlotPacket;
import nl.odalitadevelopments.menus.nms.utils.OdalitaLogger;
import nl.odalitadevelopments.menus.nms.utils.PaperHelper;
import nl.odalitadevelopments.menus.nms.utils.ReflectionUtils;
import nl.odalitadevelopments.menus.nms.v1_17_R1.packet.ClientboundSetContentsPacket_v1_17_R1;
import nl.odalitadevelopments.menus.nms.v1_17_R1.packet.ClientboundSetSlotPacket_v1_17_R1;
import org.bukkit.craftbukkit.v1_17_R1.entity.CraftPlayer;
import org.bukkit.craftbukkit.v1_17_R1.inventory.CraftInventory;
import org.bukkit.craftbukkit.v1_17_R1.inventory.CraftItemStack;
import org.bukkit.craftbukkit.v1_17_R1.util.CraftChatMessage;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.lang.reflect.Field;
import java.util.List;

public final class OdalitaMenusNMS_v1_17_R1 implements OdalitaMenusNMS {

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

            TITLE_FIELD = AbstractContainerMenu.class.getDeclaredField("title");
            WINDOW_ID_FIELD = AbstractContainerMenu.class.getDeclaredField(ObfuscatedNames_v1_17_R1.WINDOW_ID);
        } catch (Exception exception) {
            OdalitaLogger.error(exception);
        }
    }

    @Override
    public Channel getPacketChannel(Player player) {
        ServerPlayer serverPlayer = ((CraftPlayer) player).getHandle();
        return serverPlayer.connection.connection.channel;
    }

    @Override
    public void sendPacket(Player player, Object packetObject) {
        if (!(packetObject instanceof Packet<?> packet)) {
            throw new IllegalArgumentException("Packet inside packet wrapper is not an instance of a minecraft packet!");
        }

        ServerPlayer serverPlayer = ((CraftPlayer) player).getHandle();
        serverPlayer.connection.send(packet);
    }

    @Override
    public Component createChatBaseComponent(String string) {
        return CraftChatMessage.fromJSONOrNull("{\"text\":\"" + string + "\"}");
    }

    @Override
    public synchronized void setInventoryItem(int slot, ItemStack itemStack, Inventory inventory) {
        net.minecraft.world.item.ItemStack nmsItemStack = CraftItemStack.asNMSCopy(itemStack);

        Container nmsInventory = ((CraftInventory) inventory).getInventory();
        List<net.minecraft.world.item.ItemStack> contents = nmsInventory.getContents();
        if (contents.size() <= slot) {
            return;
        }

        contents.set(slot, nmsItemStack);

        for (HumanEntity viewer : inventory.getViewers()) {
            if (!(viewer instanceof Player player)) continue;

            ServerPlayer serverPlayer = ((CraftPlayer) player).getHandle();
            AbstractContainerMenu activeContainer = serverPlayer.containerMenu;
            int windowId = activeContainer.containerId;
            int stateId = activeContainer.incrementStateId();
            if (activeContainer instanceof InventoryMenu || windowId <= 0) continue;

            ClientboundContainerSetSlotPacket packet = new ClientboundContainerSetSlotPacket(windowId, stateId, slot, nmsItemStack);
            this.sendPacket(player, packet);
        }
    }

    @Override
    public synchronized void changeInventoryTitle(Inventory inventory, String title) throws Exception {
        if (inventory.getViewers().isEmpty()) {
            Container nmsInventory = ((CraftInventory) inventory).getInventory();

            // If it's a custom inventory change the title, if not, do nothing cause the updated title will be sent when the inventory is opened
            if (MINECRAFT_INVENTORY.isInstance(nmsInventory)) {
                Object minecraftInventory = MINECRAFT_INVENTORY.cast(nmsInventory);

                MINECRAFT_INVENTORY_TITLE_FIELD.setAccessible(true);
                MINECRAFT_INVENTORY_TITLE_FIELD.set(minecraftInventory, title);
                MINECRAFT_INVENTORY_TITLE_FIELD.setAccessible(false);

                if (PaperHelper.IS_PAPER) {
                    PAPER_MINECRAFT_INVENTORY_TITLE_FIELD.setAccessible(true);
                    PAPER_MINECRAFT_INVENTORY_TITLE_FIELD.set(minecraftInventory, PaperComponents.plainSerializer().deserialize(title));
                    PAPER_MINECRAFT_INVENTORY_TITLE_FIELD.setAccessible(false);
                }
            }

            return;
        }

        Component titleComponent = this.createChatBaseComponent(title);

        for (HumanEntity viewer : inventory.getViewers()) {
            if (!(viewer instanceof Player player)) continue;

            ServerPlayer serverPlayer = ((CraftPlayer) player).getHandle();
            AbstractContainerMenu activeContainer = serverPlayer.containerMenu;
            int windowId = activeContainer.containerId;
            if (windowId <= 0) continue;

            MenuType<?> type = activeContainer.getType();

            ClientboundOpenScreenPacket packet = new ClientboundOpenScreenPacket(windowId, type, titleComponent);
            this.sendPacket(player, packet);

            TITLE_FIELD.setAccessible(true);
            TITLE_FIELD.set(activeContainer, titleComponent);
            TITLE_FIELD.setAccessible(false);

            activeContainer.sendAllDataToRemote();
        }
    }

    @Override
    public synchronized void setInventoryProperty(Inventory inventory, int propertyIndex, int value) {
        for (HumanEntity viewer : inventory.getViewers()) {
            if (!(viewer instanceof Player player)) continue;

            ServerPlayer serverPlayer = ((CraftPlayer) player).getHandle();
            AbstractContainerMenu activeContainer = serverPlayer.containerMenu;

            activeContainer.setData(propertyIndex, value);
        }
    }

    @Override
    public void openInventory(Player player, Object inventory, String title) throws Exception {
        if (inventory instanceof Inventory bukkitInventory) {
            player.openInventory(bukkitInventory);
            return;
        }

        AbstractContainerMenu nmsInventory = (AbstractContainerMenu) inventory;

        ServerPlayer serverPlayer = ((CraftPlayer) player).getHandle();

        int windowId = serverPlayer.nextContainerCounter();
        WINDOW_ID_FIELD.setAccessible(true);
        WINDOW_ID_FIELD.set(nmsInventory, windowId);
        WINDOW_ID_FIELD.setAccessible(false);

        Component titleComponent = this.createChatBaseComponent(title);

        TITLE_FIELD.setAccessible(true);
        TITLE_FIELD.set(nmsInventory, titleComponent);
        TITLE_FIELD.setAccessible(false);

        serverPlayer.containerMenu = nmsInventory;

        MenuType<?> type = nmsInventory.getType();

        ClientboundOpenScreenPacket packet = new ClientboundOpenScreenPacket(windowId, type, titleComponent);
        this.sendPacket(player, packet);

        serverPlayer.initMenu(nmsInventory);
    }

    @Override
    public Object createAnvilInventory(Player player) {
        ServerPlayer serverPlayer = ((CraftPlayer) player).getHandle();
        net.minecraft.world.entity.player.Inventory playerInventory = serverPlayer.getInventory();

        AnvilMenu anvilMenu = new AnvilMenu(-1, playerInventory);
        anvilMenu.checkReachable = false;

        return anvilMenu;
    }

    @Override
    public Object createCartographyInventory(Player player) {
        ServerPlayer serverPlayer = ((CraftPlayer) player).getHandle();
        net.minecraft.world.entity.player.Inventory playerInventory = serverPlayer.getInventory();

        CartographyTableMenu cartographyTableMenu = new CartographyTableMenu(-1, playerInventory, ContainerLevelAccess.create(serverPlayer.getLevel(), serverPlayer.blockPosition()));
        cartographyTableMenu.checkReachable = false;

        return cartographyTableMenu;
    }

    @Override
    public Object createCraftingInventory(Player player) {
        ServerPlayer serverPlayer = ((CraftPlayer) player).getHandle();
        net.minecraft.world.entity.player.Inventory playerInventory = serverPlayer.getInventory();

        CraftingMenu craftingMenu = new CraftingMenu(-1, playerInventory, ContainerLevelAccess.create(serverPlayer.getLevel(), serverPlayer.blockPosition()));
        craftingMenu.checkReachable = false;

        return craftingMenu;
    }

    @Override
    public Object createEnchantingInventory(Player player) {
        ServerPlayer serverPlayer = ((CraftPlayer) player).getHandle();
        net.minecraft.world.entity.player.Inventory playerInventory = serverPlayer.getInventory();

        EnchantmentMenu enchantmentMenu = new EnchantmentMenu(-1, playerInventory, ContainerLevelAccess.create(serverPlayer.getLevel(), serverPlayer.blockPosition()));
        enchantmentMenu.checkReachable = false;

        return enchantmentMenu;
    }

    @Override
    public Object createLoomInventory(Player player) {
        ServerPlayer serverPlayer = ((CraftPlayer) player).getHandle();
        net.minecraft.world.entity.player.Inventory playerInventory = serverPlayer.getInventory();

        LoomMenu loomMenu = new LoomMenu(-1, playerInventory);
        loomMenu.checkReachable = false;

        return loomMenu;
    }

    @Override
    public Object createSmithingInventory(Player player) {
        ServerPlayer serverPlayer = ((CraftPlayer) player).getHandle();
        net.minecraft.world.entity.player.Inventory playerInventory = serverPlayer.getInventory();

        SmithingMenu smithingMenu = new SmithingMenu(-1, playerInventory);
        smithingMenu.checkReachable = false;

        return smithingMenu;
    }

    @Override
    public Object createStonecutterInventory(Player player) {
        ServerPlayer serverPlayer = ((CraftPlayer) player).getHandle();
        net.minecraft.world.entity.player.Inventory playerInventory = serverPlayer.getInventory();

        StonecutterMenu stonecutterMenu = new StonecutterMenu(-1, playerInventory);
        stonecutterMenu.checkReachable = false;

        return stonecutterMenu;
    }

    @Override
    public Inventory getInventoryFromNMS(Object nmsInventory) {
        if (!(nmsInventory instanceof AbstractContainerMenu menu)) {
            return null;
        }

        return menu.getBukkitView().getTopInventory();
    }

    @Override
    public ClientboundSetSlotPacket readSetSlotPacket(Object packet) {
        if (!(packet instanceof ClientboundContainerSetSlotPacket clientboundContainerSetSlotPacket)) {
            return null;
        }

        return new ClientboundSetSlotPacket_v1_17_R1(clientboundContainerSetSlotPacket);
    }

    @Override
    public ClientboundSetContentsPacket readSetContentsPacket(Object packet) {
        if (!(packet instanceof ClientboundContainerSetContentPacket clientboundContainerSetContentPacket)) {
            return null;
        }

        return new ClientboundSetContentsPacket_v1_17_R1(clientboundContainerSetContentPacket);
    }

    @Override
    public String setSlotPacketName() {
        return ClientboundContainerSetSlotPacket.class.getSimpleName();
    }

    @Override
    public String windowItemsPacketName() {
        return ClientboundContainerSetContentPacket.class.getSimpleName();
    }
}