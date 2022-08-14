package nl.tritewolf.tritemenus.scrollable;

import nl.tritewolf.tritemenus.iterators.MenuIterator;
import nl.tritewolf.tritemenus.patterns.MenuPattern;
import org.apache.commons.lang.math.NumberUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;

public interface ScrollableDirectionPattern extends MenuPattern<ScrollableDirectionPatternCache> {

    @Override
    default @NotNull ScrollableDirectionPatternCache getCache() {
        ScrollableDirectionPatternCache cache = new PatternInitializer(this.getPattern())
                .initializeIndex();

        if (cache == null) {
            throw new IllegalStateException("Could not initialize index of pattern: '" + this.getClass().getName() + "'");
        }

        return cache;
    }

    @Override
    default void handle(@NotNull MenuIterator menuIterator) {
    }

    record PatternInitializer(List<String> pattern) {

        private @Nullable ScrollableDirectionPatternCache initializeIndex() {
            if (this.pattern.isEmpty()) return null;

            Map<ScrollableDirection, NavigableMap<Integer, Integer>> directionIndex = new HashMap<>();
            Map<ScrollableDirection, Integer> directionWidth = new HashMap<>();

            NavigableMap<Integer, Integer> horizontalIndex = directionIndex.computeIfAbsent(ScrollableDirection.HORIZONTALLY, direction -> new TreeMap<>());
            NavigableMap<Integer, Integer> verticalIndex = directionIndex.computeIfAbsent(ScrollableDirection.VERTICALLY, direction -> new TreeMap<>());

            directionWidth.computeIfAbsent(ScrollableDirection.HORIZONTALLY, direction -> this.initializeHorizontal(horizontalIndex));
            directionWidth.computeIfAbsent(ScrollableDirection.VERTICALLY, direction -> this.initializeVertical(verticalIndex));

            return new ScrollableDirectionPatternCache(this.pattern, this.pattern.size(), directionIndex, directionWidth);
        }

        private int initializeHorizontal(NavigableMap<Integer, Integer> index) {
            int currentIndex = 0;
            int width = 0;

            for (String patternLine : this.pattern) {
                String[] splitArray = patternLine.split("\\|");

                if (width == 0) width = splitArray.length;

                if (splitArray.length != width) {
                    throw new IllegalStateException("Pattern line '" + patternLine + "' has a different width than the previous lines.");
                }

                for (String currentIndexValue : splitArray) {
                    currentIndex = this.handleCurrentIndexValue(index, currentIndexValue, currentIndex);
                }
            }

            return width;
        }

        private int initializeVertical(NavigableMap<Integer, Integer> index) {
            int currentIndex = 0;
            int width = this.pattern.get(0).split("\\|").length;

            for (int column = 0; column < width; column++) {
                for (String patternLine : this.pattern) {
                    String[] splitArray = patternLine.split("\\|");

                    if (splitArray.length != width) {
                        throw new IllegalStateException("Pattern line '" + patternLine + "' has a different width than the previous lines.");
                    }

                    String currentIndexValue = splitArray[column];
                    currentIndex = this.handleCurrentIndexValue(index, currentIndexValue, currentIndex);
                }
            }

            return width;
        }

        private int handleCurrentIndexValue(NavigableMap<Integer, Integer> index, String currentIndexValue, int currentIndex) {
            if (currentIndexValue.equalsIgnoreCase("##")) {
                index.put(currentIndex++, -1);
                return currentIndex;
            }

            if (NumberUtils.isDigits(currentIndexValue)) {
                index.put(currentIndex++, Integer.parseInt(currentIndexValue));
            }

            return currentIndex;
        }
    }
}
