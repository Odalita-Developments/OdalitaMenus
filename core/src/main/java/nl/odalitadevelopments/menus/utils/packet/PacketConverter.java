package nl.odalitadevelopments.menus.utils.packet;

import nl.odalitadevelopments.menus.nms.OdalitaMenusNMS;
import nl.odalitadevelopments.menus.nms.utils.version.ProtocolVersion;
import nl.odalitadevelopments.menus.providers.providers.PacketListenerProvider;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public final class PacketConverter {

    private PacketConverter() {
    }

    public static @Nullable OdalitaMenuPacket convertClientboundPacket(@NotNull PacketListenerProvider.ClientboundPacketType type, @NotNull Object packetObject) {
        if (type == PacketListenerProvider.ClientboundPacketType.SET_SLOT) {
            return convertSetSlotPacket(packetObject);
        }

        if (type == PacketListenerProvider.ClientboundPacketType.WINDOW_ITEMS) {
            return convertWindowItemsPacket(packetObject);
        }

        return null;
    }

    public static void updateClientboundPacket(@NotNull PacketListenerProvider.ClientboundPacketType type, @NotNull OdalitaMenuPacket packet, @NotNull Object packetObject) {
        if (type == PacketListenerProvider.ClientboundPacketType.SET_SLOT) {
            updateSetSlotPacket(packet, packetObject);
            return;
        }

        if (type == PacketListenerProvider.ClientboundPacketType.WINDOW_ITEMS) {
            updateWindowItemsPacket(packet, packetObject);
        }
    }

    private static OdalitaSetSlotPacket convertSetSlotPacket(@NotNull Object packetObject) {
        try {
            boolean is1171 = ProtocolVersion.getServerVersion().isHigherOrEqual(ProtocolVersion.MINECRAFT_1_17_1);

            Class<?> packetClass = packetObject.getClass();
            int windowId = getField(packetClass, (is1171) ? "c" : "a").getInt(packetObject);
            int slot = getField(packetClass, (is1171) ? "e" : "b").getInt(packetObject);
            Object nmsItem = getField(packetClass, (is1171) ? "f" : "c").get(packetObject);
            ItemStack item = OdalitaMenusNMS.getInstance().itemStackFromNMS(nmsItem);

            if (is1171) {
                int stateId = getField(packetClass, "d").getInt(packetObject);
                return new OdalitaSetSlotPacket(windowId, stateId, slot, item);
            }

            return new OdalitaSetSlotPacket(windowId, slot, item);
        } catch (Exception exception) {
            exception.printStackTrace();
            return null;
        }
    }

    @SuppressWarnings("unchecked")
    private static OdalitaWindowItemsPacket convertWindowItemsPacket(@NotNull Object packetObject) {
        try {
            boolean is1171 = ProtocolVersion.getServerVersion().isHigherOrEqual(ProtocolVersion.MINECRAFT_1_17_1);

            Class<?> packetClass = packetObject.getClass();
            int windowId = getField(packetClass, "a").getInt(packetObject);
            List<Object> nmsItems = (List<Object>) getField(packetClass, (is1171) ? "c" : "b").get(packetObject);

            List<ItemStack> items = new ArrayList<>(nmsItems.size());
            for (Object nmsItem : nmsItems) {
                items.add(OdalitaMenusNMS.getInstance().itemStackFromNMS(nmsItem));
            }

            if (is1171) {
                int stateId = getField(packetClass, "b").getInt(packetObject);
                Object nmsCarriedItem = getField(packetClass, "d").get(packetObject);
                ItemStack carriedItem = OdalitaMenusNMS.getInstance().itemStackFromNMS(nmsCarriedItem);

                return new OdalitaWindowItemsPacket(windowId, stateId, items, carriedItem);
            }

            return new OdalitaWindowItemsPacket(windowId, items);
        } catch (Exception exception) {
            exception.printStackTrace();
            return null;
        }
    }

    private static void updateSetSlotPacket(@NotNull OdalitaMenuPacket odalitaMenuPacket, @NotNull Object packetObject) {
        if (!(odalitaMenuPacket instanceof OdalitaSetSlotPacket packet)) return;

        try {
            boolean is1171 = ProtocolVersion.getServerVersion().isHigherOrEqual(ProtocolVersion.MINECRAFT_1_17_1);

            Class<?> packetClass = packetObject.getClass();
            Object nmsItem = OdalitaMenusNMS.getInstance().itemStackToNMS(packet.item());

            getField(packetClass, (is1171) ? "f" : "c").set(packetObject, nmsItem);
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    private static void updateWindowItemsPacket(@NotNull OdalitaMenuPacket odalitaMenuPacket, @NotNull Object packetObject) {
        if (!(odalitaMenuPacket instanceof OdalitaWindowItemsPacket packet)) return;

        try {
            boolean is1171 = ProtocolVersion.getServerVersion().isHigherOrEqual(ProtocolVersion.MINECRAFT_1_17_1);

            Class<?> packetClass = packetObject.getClass();
            List<Object> nmsItems = new ArrayList<>();
            for (ItemStack item : packet.items()) {
                nmsItems.add(OdalitaMenusNMS.getInstance().itemStackToNMS(item));
            }

            getField(packetClass, (is1171) ? "c" : "b").set(packetObject, nmsItems);
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    private static Field getField(Class<?> clazz, String name) throws NoSuchFieldException {
        Field field = clazz.getDeclaredField(name);
        field.setAccessible(true);
        return field;
    }
}