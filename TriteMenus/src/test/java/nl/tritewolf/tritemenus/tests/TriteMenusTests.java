package nl.tritewolf.tritemenus.tests;

import nl.tritewolf.tritejection.TriteJection;
import nl.tritewolf.tritemenus.TriteMenus;
import nl.tritewolf.tritemenus.menu.TriteMenuContainer;
import nl.tritewolf.tritemenus.menu.TriteMenuObject;
import nl.tritewolf.tritemenus.menu.TriteMenuProcessor;
import nl.tritewolf.tritemenus.menu.providers.TriteGlobalMenuProvider;
import nl.tritewolf.tritemenus.utils.Pair;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Map;
import java.util.logging.Logger;

public class TriteMenusTests {

    static TriteMenus triteMenus;

    @BeforeAll
    static void setup() {
        Logger.getAnonymousLogger().info("Starting testing:");
        triteMenus = new TriteMenus();
    }

    @DisplayName("Injection test")
    @Test
    public void testInjection() {
        Assertions.assertNotNull(triteMenus);

        TriteMenuContainer triteMenuContainer = triteMenus.getTriteMenus().getTriteJection(TriteMenuContainer.class);
        Assertions.assertNotNull(triteMenuContainer);
    }

    @DisplayName("Menu binding")
    @Test
    public void testMenuBinding() {
        TriteMenuContainer triteMenuContainer = triteMenus.getTriteMenus().getTriteJection(TriteMenuContainer.class);

        Assertions.assertEquals(0, triteMenuContainer.getTriteMenus().size());
        Map<Class<?>, Pair<TriteGlobalMenuProvider, TriteMenuObject>> triteGlobalMenus = triteMenuContainer.getTriteGlobalMenus();
        Assertions.assertEquals(1, triteGlobalMenus.size());

        triteMenus.getMenuProcessor().openMenu(TestMenu.class, null);
    }

}
