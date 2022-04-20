package nl.tritewolf.tritemenus.modules;

import lombok.AllArgsConstructor;
import nl.tritewolf.tritejection.module.TriteJectionModule;
import nl.tritewolf.tritejection.multibinder.TriteJectionMultiBinder;
import nl.tritewolf.tritemenus.items.ItemProcessor;
import nl.tritewolf.tritemenus.patterns.IteratorPatternContainer;
import nl.tritewolf.tritemenus.listeners.InventoryListener;
import nl.tritewolf.tritemenus.annotations.AnnotationBinding;
import nl.tritewolf.tritemenus.menu.MenuContainer;
import nl.tritewolf.tritemenus.menu.MenuProcessor;
import nl.tritewolf.tritemenus.modules.multibindings.ListenerBinding;
import nl.tritewolf.tritemenus.tasks.MenuUpdateTask;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Arrays;
import java.util.List;

@AllArgsConstructor
public final class MenusModule extends TriteJectionModule {

    private final JavaPlugin javaPlugin;

    @Override
    public void bindings() {
        bind(MenuProcessor.class).asEagerSingleton();
        bind(ItemProcessor.class).asEagerSingleton();
        bind(MenuContainer.class).asEagerSingleton();
        bind(AnnotationBinding.class).asEagerSingleton();
        bind(MenuUpdateTask.class).asEagerSingleton();
        bind(IteratorPatternContainer.class).asEagerSingleton();

        bindListeners(new InventoryListener());
    }

    @Override
    public List<TriteJectionMultiBinder> registerMultiBindings() {
        return List.of(new ListenerBinding(this.javaPlugin));
    }

    private void bindListeners(Listener... listeners) {
        Arrays.stream(listeners).forEach(listener -> bind(listener.getClass()).toMultiBinder(Listener.class).asEagerSingleton());
    }
}