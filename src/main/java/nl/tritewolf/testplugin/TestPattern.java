package nl.tritewolf.testplugin;

import nl.tritewolf.tritemenus.patterns.IteratorPattern;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.List;

public class TestPattern implements IteratorPattern {

    @Override
    public @NotNull List<String> getPattern() {
        return Arrays.asList(
                "###@@@###",
                "@#@###@#@",
                "@#@###@#@",
                "###@@@###"
        );
    }
}
