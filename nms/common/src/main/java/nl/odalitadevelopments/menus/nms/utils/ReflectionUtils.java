package nl.odalitadevelopments.menus.nms.utils;


import nl.odalitadevelopments.menus.nms.utils.version.ProtocolVersion;
import org.bukkit.Bukkit;

public final class ReflectionUtils {

    private ReflectionUtils() {
    }

    private static final String CRAFTBUKKIT_PACKAGE = Bukkit.getServer().getClass().getPackage().getName();
    private static final String NM_PACKAGE = "net.minecraft";
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

    public static String nmsClassName(String post1_17package, String className) {
        String classPackage = (ProtocolVersion.getServerVersion().isHigherOrEqual(ProtocolVersion.MINECRAFT_1_17_1))
                ? post1_17package == null || post1_17package.isEmpty() ? NM_PACKAGE : NM_PACKAGE + '.' + post1_17package
                : NM_PACKAGE + ".server." + getServerProtocolVersion();

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

    public static String paperClassName(String className) {
        return PAPER_PACKAGE + '.' + className;
    }

    public static Class<?> paperClass(String className) throws ClassNotFoundException {
        return Class.forName(paperClassName(className));
    }
}