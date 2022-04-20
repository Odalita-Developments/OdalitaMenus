package nl.tritewolf.testplugin;

import nl.tritewolf.tritemenus.patterns.IteratorPattern;

import java.util.Arrays;
import java.util.List;

public class TestPattern implements IteratorPattern {

    @Override
    public Character ignoredSymbol() {
        return '#';
    }

    @Override
    public List<String> getPattern() {
        return Arrays.asList(
                "###@@@###",
                "@#@###@#@",
                "@#@###@#@",
                "###@@@###"
        );
    }
}
