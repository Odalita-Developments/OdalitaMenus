package nl.tritewolf.testplugin;

import nl.tritewolf.tritemenus.iterators.MenuIteratorType;
import nl.tritewolf.tritemenus.patterns.DirectionPattern;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.List;

public class TestPattern3 implements DirectionPattern {

    @Override
    public @NotNull List<String> getPattern() {
        return Arrays.asList(
                "010203040506070809",
                "################10",
                "191817161514131211",
                "20################"
        );
    }

    @Override
    public boolean shouldContinuePattern() {
        return true;
    }

    @Override
    public MenuIteratorType menuIteratorType() {
        return MenuIteratorType.VERTICAL;
    }
}
