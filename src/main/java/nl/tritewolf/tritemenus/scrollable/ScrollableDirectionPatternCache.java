package nl.tritewolf.tritemenus.scrollable;

import java.util.List;
import java.util.Map;

record ScrollableDirectionPatternCache(List<String> pattern, Map<Integer, Integer> index, int height, int width) {
}