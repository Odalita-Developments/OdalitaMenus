package nl.tritewolf.tritemenus.modules;

import nl.tritewolf.tritejection.module.TriteJectionModule;
import nl.tritewolf.tritemenus.menu.TriteMenuBinding;
import nl.tritewolf.tritemenus.menu.TriteMenuContainer;
import nl.tritewolf.tritemenus.menu.TriteMenuProcessor;

public class TriteMenusModule extends TriteJectionModule {

    @Override
    public void bindings() {
        bind(TriteMenuProcessor.class).asEagerSingleton();
        bind(TriteMenuContainer.class).asEagerSingleton();
        bind(TriteMenuBinding.class).asEagerSingleton();
    }
}
