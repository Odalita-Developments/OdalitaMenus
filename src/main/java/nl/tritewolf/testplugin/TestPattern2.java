package nl.tritewolf.testplugin;

import nl.tritewolf.tritemenus.iterators.MenuIteratorType;
import nl.tritewolf.tritemenus.patterns.DirectionPattern;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.List;

public class TestPattern2 implements DirectionPattern {

    @Override
    public @NotNull List<String> getPattern() {
        return Arrays.asList(
                "01##0910",
                "02##08##",
                "03##07##",
                "040506##"
        );
    }

    @Override
    public boolean shouldContinuePattern() {
        return true;
    }

    @Override
    public MenuIteratorType menuIteratorType() {
        return MenuIteratorType.HORIZONTAL;
    }
}
