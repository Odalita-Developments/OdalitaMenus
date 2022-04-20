package nl.tritewolf.tritemenus.patterns;

import nl.tritewolf.tritemenus.iterators.MenuIterator;

import java.util.List;

public interface MenuPattern {

    List<String> getPattern();

    void handle(MenuIterator menuIterator);
}
