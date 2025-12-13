package nl.odalitadevelopments.menus.nms.utils;

import org.bukkit.Bukkit;

public final class ReflectionUtils {

    private ReflectionUtils() {
    }

    private static final String CRAFTBUKKIT_PACKAGE = Bukkit.getServer().getClass().getPackage().getName();
    private static final String OBC_PACKAGE = "org.bukkit.craftbukkit";
    private static final String PAPER_PACKAGE = "io.papermc.paper";

    public static String cbClassName(String className) {
        return (CRAFTBUKKIT_PACKAGE + "." + className);
    }

    public static Class<?> cbClass(String className) throws ClassNotFoundException {
        return Class.forName(cbClassName(className));
    }

    public static String getServerProtocolVersion() {
        String bv = Bukkit.getServer().getClass().getPackage().getName();
        return bv.substring(bv.lastIndexOf('.') + 1);
    }

    public static String obcClassName(String className) {
        return OBC_PACKAGE + '.' + getServerProtocolVersion() + '.' + className;
    }

    public static Class<?> obcClass(String className) throws ClassNotFoundException {
        return Class.forName(obcClassName(className));
    }

    public static String paperClassName(String className) {
        return PAPER_PACKAGE + '.' + className;
    }

    public static Class<?> paperClass(String className) throws ClassNotFoundException {
        return Class.forName(paperClassName(className));
    }
}