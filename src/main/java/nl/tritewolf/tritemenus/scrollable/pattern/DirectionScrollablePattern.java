package nl.tritewolf.tritemenus.scrollable.pattern;

import lombok.Getter;
import org.apache.commons.lang.math.NumberUtils;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Getter
public class DirectionScrollablePattern {

    /**
     * X = empty
     * | = Seperator
     * int = position;
     */

    private final List<String> pattern;
    private final Map<Integer, Integer> index = new HashMap<>();

    public DirectionScrollablePattern(@NotNull List<@NotNull String> pattern) {
        this.pattern = pattern;
        this.initializeIndex();
    }

    public void initializeIndex() {
        int currentIndex = 0;

        for (String patternLine : this.pattern) {
            for (String s : patternLine.split("\\|")) {
                if (s.equalsIgnoreCase("X")) {
                    this.index.put(currentIndex++, -1);
                    continue;
                }

                if (NumberUtils.isDigits(s)) {
                    this.index.put(currentIndex++, Integer.parseInt(s));
                }
            }
        }
    }
}
