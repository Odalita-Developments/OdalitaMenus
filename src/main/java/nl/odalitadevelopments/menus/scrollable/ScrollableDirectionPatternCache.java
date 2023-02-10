package nl.odalitadevelopments.menus.scrollable;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Map;
import java.util.NavigableMap;

@RequiredArgsConstructor(access = AccessLevel.PACKAGE)
final class ScrollableDirectionPatternCache {

    private final List<String> pattern;
    private final int height;

    private final Map<ScrollableDirection, NavigableMap<Integer, Integer>> directionIndex;
    private final Map<ScrollableDirection, Integer> directionWidth;

    public @NotNull Cache initialize(ScrollableDirection direction) {
        if (direction != ScrollableDirection.HORIZONTALLY && direction != ScrollableDirection.VERTICALLY) {
            throw new IllegalArgumentException("Direction is not supported '" + direction + "'");
        }

        NavigableMap<Integer, Integer> index = this.directionIndex.get(direction);
        int width = this.directionWidth.get(direction);

        int amountOfIndexes = (int) index.values().stream()
                .filter(i -> i > -1)
                .count();

        int highestIndex = this.height * width;
        return new Cache(this.pattern, index, this.height, width, amountOfIndexes, highestIndex);
    }

    record Cache(List<String> pattern, NavigableMap<Integer, Integer> index, int height, int width, int amountOfIndexes,
                 int highestIndex) {
    }
}