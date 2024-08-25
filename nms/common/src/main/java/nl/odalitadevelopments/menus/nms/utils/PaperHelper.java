package nl.odalitadevelopments.menus.nms.utils;

public final class PaperHelper {

    private PaperHelper() {
    }

    public static final boolean IS_PAPER = checkIsPaper();

    @SuppressWarnings("all")
    private static boolean checkIsPaper() {
        try {
            return Class.forName("com.destroystokyo.paper.PaperConfig") != null || Class.forName("io.papermc.paper.configuration.PaperConfigurations") != null;
        } catch (ClassNotFoundException ex) {
            return false;
        }
    }
}