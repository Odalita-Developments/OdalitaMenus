package nl.tritewolf.tritemenus.contents;

import nl.tritewolf.tritemenus.menu.providers.frame.MenuFrameProvider;

public record MenuFrameData(String id, int height, int width, int startRow, int startColumn,
                            Class<? extends MenuFrameProvider> frameClass) {
}