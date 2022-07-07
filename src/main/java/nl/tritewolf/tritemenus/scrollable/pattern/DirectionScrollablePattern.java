package nl.tritewolf.tritemenus.scrollable.pattern;

import com.google.common.collect.Maps;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import nl.tritewolf.tritemenus.items.MenuItem;
import org.apache.commons.lang.math.NumberUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

@Getter
public class DirectionScrollablePattern {

    /**
     * X = empty
     * | = Seperator
     * int = position;
     */

    private final List<String> pattern;
    private final Map<ScrollablePatternIndex, Supplier<MenuItem>> index = new HashMap<>();

    public DirectionScrollablePattern(List<String> pattern) {
        this.pattern = pattern;

        initializeIndex();
    }

    public void initializeIndex() {
        int currentIndex = 0;
        for (String patternLine : pattern) {
            for (String seperated : patternLine.toUpperCase().split("X")) {
                if (seperated.isBlank() || seperated.isEmpty()) {
                    index.put(new ScrollablePatternIndex(currentIndex++, -1), () -> null);
                    continue;
                }

                for (String s : seperated.split("\\|")) {
                    if (NumberUtils.isDigits(s)) {
                        index.put(new ScrollablePatternIndex(currentIndex++, Integer.parseInt(s)), () -> null);
                    }
                }
            }
        }
    }
}
