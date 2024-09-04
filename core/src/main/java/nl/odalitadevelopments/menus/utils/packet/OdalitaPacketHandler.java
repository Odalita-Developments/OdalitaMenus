package nl.odalitadevelopments.menus.utils.packet;

import nl.odalitadevelopments.menus.nms.utils.OdalitaLogger;
import org.bukkit.Bukkit;

public final class OdalitaPacketHandler {

    static {
        OdalitaLogger.info("OdalitaPacketHandler initialized");
        OdalitaLogger.info(OdalitaPacketHandler.class.getClassLoader().getName());
        OdalitaLogger.info(Bukkit.class.getClassLoader().getName());
    }
}