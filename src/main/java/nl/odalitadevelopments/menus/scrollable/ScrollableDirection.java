package nl.odalitadevelopments.menus.scrollable;

import org.jetbrains.annotations.NotNull;

enum ScrollableDirection {

    VERTICALLY,
    HORIZONTALLY,
    ALL;

    public @NotNull ScrollableDirection getCacheInitializationDirection() {
        return switch (this) {
            case VERTICALLY, ALL -> HORIZONTALLY;
            case HORIZONTALLY -> VERTICALLY;
        };
    }
}