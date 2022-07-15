package nl.tritewolf.tritemenus.scrollable;

import nl.tritewolf.tritemenus.iterators.MenuIterator;
import nl.tritewolf.tritemenus.patterns.MenuPattern;
import org.apache.commons.lang.math.NumberUtils;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public interface ScrollableDirectionPattern extends MenuPattern<ScrollableDirectionPatternCache> {

    @Override
    default @NotNull ScrollableDirectionPatternCache getCache() {
        return this.initializeIndex();
    }

    @Override
    default void handle(@NotNull MenuIterator menuIterator) {
    }

    default ScrollableDirectionPatternCache initializeIndex() {
        int currentIndex = 0;
        int highestWidth = 0;

        Map<Integer, Integer> index = new HashMap<>();

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

        return new ScrollableDirectionPatternCache(this.getPattern(), index, this.getPattern().size(), highestWidth);
    }
}
