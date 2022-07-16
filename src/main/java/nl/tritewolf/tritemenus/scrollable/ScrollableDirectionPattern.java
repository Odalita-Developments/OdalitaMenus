package nl.tritewolf.tritemenus.scrollable;

import nl.tritewolf.tritemenus.iterators.MenuIterator;
import nl.tritewolf.tritemenus.patterns.MenuPattern;
import org.apache.commons.lang.math.NumberUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.NavigableMap;
import java.util.TreeMap;

public interface ScrollableDirectionPattern extends MenuPattern<ScrollableDirectionPatternCache> {

    default @NotNull Direction direction() {
        return Direction.HORIZONTALLY;
    }

    @Override
    default @NotNull ScrollableDirectionPatternCache getCache() {
        ScrollableDirectionPatternCache cache = this.initializeIndex();
        if (cache == null) {
            throw new IllegalStateException("Could not initialize index of pattern: '" + this.getClass().getName() + "'");
        }

        return cache;
    }

    @Override
    default void handle(@NotNull MenuIterator menuIterator) {
    }

    default @Nullable ScrollableDirectionPatternCache initializeIndex() {
        if (this.getPattern().isEmpty()) return null;

        Direction direction = this.direction();

        int currentIndex = 0;
        int highestWidth = 0;

        NavigableMap<Integer, Integer> index = new TreeMap<>();

        if (direction == Direction.HORIZONTALLY) {
            for (String patternLine : this.getPattern()) {
                int width = 0;
                for (String s : patternLine.split("\\|")) {
                    if (s.equalsIgnoreCase("##")) {
                        index.put(currentIndex++, -1);
                        width++;
                        continue;
                    }

                    if (NumberUtils.isDigits(s)) {
                        index.put(currentIndex++, Integer.parseInt(s));
                        width++;
                    }
                }

                if (width > highestWidth) {
                    highestWidth = width;
                }
            }
        } else {
            highestWidth = this.getPattern().get(0).split("\\|").length;
            for (int column = 0; column < highestWidth; column++) {
                for (int row = 0; row < this.getPattern().size(); row++) {
                    String s = this.getPattern().get(row);
                    String currentIndexValue = s.split("\\|")[column];

                    if (currentIndexValue.equalsIgnoreCase("##")) {
                        index.put(currentIndex++, -1);
                        continue;
                    }

                    if (NumberUtils.isDigits(currentIndexValue)) {
                        index.put(currentIndex++, Integer.parseInt(currentIndexValue));
                    }
                }
            }
        }

        return new ScrollableDirectionPatternCache(direction, this.getPattern(), index, this.getPattern().size(), highestWidth, 0, 0);
    }

    enum Direction {

        HORIZONTALLY,
        VERTICALLY;

        public static @NotNull Direction fromScrollableDirection(@NotNull Direction direction) {
            if (direction == Direction.HORIZONTALLY) {
                return Direction.VERTICALLY;
            }

            return Direction.HORIZONTALLY;
        }
    }
}
