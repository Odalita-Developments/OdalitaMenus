package nl.odalitadevelopments.menus.contents.frame;

import nl.odalitadevelopments.menus.menu.providers.frame.MenuFrameProvider;

public record MenuFrameData(String id, int height, int width, int startRow, int startColumn,
                            Class<? extends MenuFrameProvider> frameClass) {
}