package nl.tritewolf.tritemenus.scrollable;

import java.util.List;
import java.util.NavigableMap;

record ScrollableDirectionPatternCache(ScrollableDirectionPattern.Direction patternDirection, List<String> pattern, NavigableMap<Integer, Integer> index, int height,
                                       int width, int amountOfIndexes, int highestIndex) {

    public ScrollableDirectionPatternCache {
        amountOfIndexes = (int) index.values().stream()
                .filter(i -> i > -1)
                .count();

        highestIndex = height * width;
    }
}