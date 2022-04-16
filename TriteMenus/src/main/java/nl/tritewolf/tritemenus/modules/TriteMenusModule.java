package nl.tritewolf.tritemenus.modules;

import nl.tritewolf.tritejection.module.TriteJectionModule;
import nl.tritewolf.tritemenus.menu.TriteMenuBinding;
import nl.tritewolf.tritemenus.menu.TriteMenuContainer;

public class TriteMenusModule extends TriteJectionModule {

    @Override
    public void bindings() {
        bind(TriteMenuContainer.class).asEagerSingleton();
        bind(TriteMenuBinding.class).asEagerSingleton();
    }
}
