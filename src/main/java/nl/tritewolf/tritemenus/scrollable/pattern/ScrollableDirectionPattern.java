package nl.tritewolf.tritemenus.scrollable.pattern;

import lombok.Getter;
import org.apache.commons.lang.math.NumberUtils;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Getter
public class ScrollableDirectionPattern {

    /**
     * ## = empty
     * | = Seperator
     * int = position;
     */

    private final List<String> pattern;
    private final Map<Integer, Integer> index = new HashMap<>();

    private int width;
    private final int height;

    public ScrollableDirectionPattern(@NotNull List<@NotNull String> pattern) {
        this.pattern = pattern;

        this.width = 0;
        this.height = pattern.size();

        this.initializeIndex();
    }

    public void initializeIndex() {
        int currentIndex = 0;

        for (String patternLine : this.pattern) {
            int width = 0;
            for (String s : patternLine.split("\\|")) {
                if (s.equalsIgnoreCase("##")) {
                    this.index.put(currentIndex++, -1);
                    width++;
                    continue;
                }

                if (NumberUtils.isDigits(s)) {
                    this.index.put(currentIndex++, Integer.parseInt(s));
                    width++;
                }
            }

            if (width > this.width) {
                this.width = width;
            }
        }
    }
}
