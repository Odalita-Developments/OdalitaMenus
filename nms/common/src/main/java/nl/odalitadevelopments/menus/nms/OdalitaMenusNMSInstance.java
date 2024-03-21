package nl.odalitadevelopments.menus.nms;

import lombok.Getter;

final class OdalitaMenusNMSInstance {

    @Getter
    private static OdalitaMenusNMS nms;

    static void init(OdalitaMenusNMS instance) {
        if (nms != null) {
            throw new IllegalStateException("OdalitaMenusNMS instance is already initialized.");
        }

        nms = instance;
    }
}