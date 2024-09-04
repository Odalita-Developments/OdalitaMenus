package nl.odalitadevelopments.menus.utils;

import nl.odalitadevelopments.menus.OdalitaMenus;
import nl.odalitadevelopments.menus.nms.utils.OdalitaLogger;
import nl.odalitadevelopments.menus.utils.packet.OdalitaPacketHandler;
import org.bukkit.Bukkit;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.ServicePriority;
import org.bukkit.plugin.java.JavaPlugin;

import java.lang.reflect.RecordComponent;
import java.util.Collection;

public final class OdalitaServicesHandler {

    static {
        Thread.currentThread().setContextClassLoader(Bukkit.class.getClassLoader());
        new OdalitaPacketHandler();
        Thread.currentThread().setContextClassLoader(OdalitaMenus.class.getClassLoader());
    }

    private static final String IDENTIFIER_NAME = OdalitaIdentifier.class.getName();

    private final JavaPlugin plugin;

    public OdalitaServicesHandler(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    public int generateId() {
        int highestId = -1;

        try {
            Collection<Class<?>> knownServices = Bukkit.getServicesManager().getKnownServices();

            for (Class<?> knownService : knownServices) {
                if (!knownService.getName().equals(IDENTIFIER_NAME)) {
                    continue;
                }

                Collection<? extends RegisteredServiceProvider<?>> registrations = Bukkit.getServicesManager().getRegistrations(knownService);
                for (RegisteredServiceProvider<?> registration : registrations) {
                    RecordComponent[] recordComponents = registration.getService().getRecordComponents();
                    for (RecordComponent recordComponent : recordComponents) {
                        if (recordComponent.getName().equals("id")) {
                            int id = (int) recordComponent.getAccessor().invoke(registration.getProvider());
                            if (id > highestId) {
                                highestId = id;
                            }
                        }
                    }
                }
            }

            highestId++;
            Bukkit.getServicesManager().register(OdalitaIdentifier.class, new OdalitaIdentifier(highestId), this.plugin, ServicePriority.Normal);
        } catch (Exception exception) {
            OdalitaLogger.error("Failed to generate id", exception);
        }

        return highestId;
    }

    private record OdalitaIdentifier(int id) {
    }
}