package nl.tritewolf.tritemenus.utils;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.ApiStatus;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@ApiStatus.Internal
public final class ReflectionUtils {

    private ReflectionUtils() {
    }

    private static final String NM_PACKAGE = "net.minecraft";
    private static final String OBC_PACKAGE = "org.bukkit.craftbukkit";

    public static Class<?> PACKET;
    public static Class<?> ENTITY_PLAYER;
    public static Class<?> ENTITY_HUMAN;
    public static Class<?> PLAYER_CONNECTION;
    public static Class<?> CONTAINER;
    public static Class<?> CONTAINERS;
    public static Class<?> IINVENTORY;
    public static Class<?> CHAT_BASE_COMPONENT;

    public static Class<?> CRAFT_PLAYER;
    public static Class<?> CRAFT_INVENTORY;
    public static Class<?> ITEM_STACK;
    public static Class<?> CRAFT_ITEM_STACK;
    public static Class<?> CRAFT_CONTAINER;

    public static Class<?> PACKET_PLAY_OUT_SET_SLOT;
    public static Class<?> PACKET_PLAY_OUT_OPEN_WINDOW;

    public static Method GET_NMS_ITEM_STACK;
    public static Method GET_NMS_INVENTORY;
    public static Method GET_NMS_INVENTORY_CONTENTS;
    public static Method SET_LIST;
    public static Method WINDOW_STATE_ID_METHOD;
    public static Method GET_NOTCH_INVENTORY_TYPE;

    public static Field ACTIVE_CONTAINER_FIELD;
    public static Field WINDOW_ID_FIELD;

    public static Constructor<?> PACKET_PLAY_OUT_SET_SLOT_CONSTRUCTOR;
    public static Constructor<?> PACKET_PLAY_OUT_OPEN_WINDOW_CONSTRUCTOR;

    public static MethodHandle GET_PLAYER_HANDLE_METHOD;
    public static MethodHandle GET_PLAYER_CONNECTION_METHOD;
    public static MethodHandle SEND_PACKET_METHOD;

    static {
        try {
            MethodHandles.Lookup lookup = MethodHandles.lookup();

            PACKET = nmsClass("network.protocol", "Packet");
            ENTITY_PLAYER = nmsClass("server.level", "EntityPlayer");
            ENTITY_HUMAN = nmsClass("world.entity.player", "EntityHuman");
            PLAYER_CONNECTION = nmsClass("server.network", "PlayerConnection");
            CONTAINER = nmsClass("world.inventory", "Container");
            CONTAINERS = nmsClass("world.inventory", "Containers");
            IINVENTORY = nmsClass("world", "IInventory");
            CHAT_BASE_COMPONENT = nmsClass("network.chat", "IChatBaseComponent");

            CRAFT_PLAYER = obcClass("entity.CraftPlayer");
            CRAFT_INVENTORY = obcClass("inventory.CraftInventory");
            ITEM_STACK = nmsClass("world.item", "ItemStack");
            CRAFT_ITEM_STACK = obcClass("inventory.CraftItemStack");
            CRAFT_CONTAINER = obcClass("inventory.CraftContainer");

            PACKET_PLAY_OUT_SET_SLOT = nmsClass("network.protocol.game", "PacketPlayOutSetSlot");
            PACKET_PLAY_OUT_OPEN_WINDOW = nmsClass("network.protocol.game", "PacketPlayOutOpenWindow");

            GET_NMS_ITEM_STACK = CRAFT_ITEM_STACK.getMethod("asNMSCopy", ItemStack.class);
            GET_NMS_INVENTORY = CRAFT_INVENTORY.getMethod("getInventory");
            GET_NMS_INVENTORY_CONTENTS = IINVENTORY.getMethod("getContents");
            SET_LIST = List.class.getMethod("set", int.class, Object.class);
            WINDOW_STATE_ID_METHOD = CONTAINER.getMethod("k");
            GET_NOTCH_INVENTORY_TYPE = CRAFT_CONTAINER.getMethod("getNotchInventoryType", Inventory.class);

            ACTIVE_CONTAINER_FIELD = ENTITY_PLAYER.getField("bV");
            WINDOW_ID_FIELD = CONTAINER.getField("j");

            PACKET_PLAY_OUT_SET_SLOT_CONSTRUCTOR = PACKET_PLAY_OUT_SET_SLOT.getConstructor(int.class, int.class, int.class, ITEM_STACK);
            PACKET_PLAY_OUT_OPEN_WINDOW_CONSTRUCTOR = PACKET_PLAY_OUT_OPEN_WINDOW.getConstructor(int.class, CONTAINERS, CHAT_BASE_COMPONENT);

            Field playerConnectionField = Arrays.stream(ENTITY_PLAYER.getFields())
                    .filter(field -> field.getType().isAssignableFrom(PLAYER_CONNECTION))
                    .findFirst().orElseThrow(NoSuchFieldException::new);

            Method sendPacketMethod = Arrays.stream(PLAYER_CONNECTION.getMethods())
                    .filter(m -> m.getParameterCount() == 1 && m.getParameterTypes()[0] == PACKET)
                    .findFirst().orElseThrow(NoSuchMethodException::new);

            GET_PLAYER_HANDLE_METHOD = lookup.findVirtual(CRAFT_PLAYER, "getHandle", MethodType.methodType(ENTITY_PLAYER));
            GET_PLAYER_CONNECTION_METHOD = lookup.unreflectGetter(playerConnectionField);
            SEND_PACKET_METHOD = lookup.unreflect(sendPacketMethod);
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    public static String getServerProtocolVersion() {
        String bv = Bukkit.getServer().getClass().getPackage().getName();
        return bv.substring(bv.lastIndexOf('.') + 1);
    }

    private static String nmsClassName(String post1_17package, String className) {
        String classPackage = post1_17package == null || post1_17package.isEmpty() ? NM_PACKAGE : NM_PACKAGE + '.' + post1_17package;
        return classPackage + '.' + className;
    }

    public static Class<?> nmsClass(String post1_17package, String className) throws ClassNotFoundException {
        return Class.forName(nmsClassName(post1_17package, className));
    }

    public static String obcClassName(String className) {
        return OBC_PACKAGE + '.' + getServerProtocolVersion() + '.' + className;
    }

    public static Class<?> obcClass(String className) throws ClassNotFoundException {
        return Class.forName(obcClassName(className));
    }

    public static Optional<Class<?>> optionalClass(String className) {
        try {
            return Optional.of(Class.forName(className));
        } catch (ClassNotFoundException e) {
            return Optional.empty();
        }
    }

    public static void sendPacket(Player player, Object packet) {
        try {
            Object entityPlayer = GET_PLAYER_HANDLE_METHOD.invoke(player);
            Object playerConnection = GET_PLAYER_CONNECTION_METHOD.invoke(entityPlayer);
            SEND_PACKET_METHOD.invoke(playerConnection, packet);
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }
    }

    public static Object createChatBaseComponent(String string) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        return CHAT_BASE_COMPONENT.getDeclaredClasses()[0].getMethod("a", String.class).invoke(null, "{\"text\":\"" + string + "\"}");
    }
}