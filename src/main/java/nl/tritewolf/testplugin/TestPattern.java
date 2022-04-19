package nl.tritewolf.testplugin;

import nl.tritewolf.tritemenus.annotations.TritePattern;
import nl.tritewolf.tritemenus.iterators.patterns.TriteIteratorPattern;

import java.util.Arrays;
import java.util.List;

@TritePattern
public class TestPattern implements TriteIteratorPattern {

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
