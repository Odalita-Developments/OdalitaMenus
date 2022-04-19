package nl.tritewolf.tritemenus.modules;

import lombok.AllArgsConstructor;
import nl.tritewolf.tritejection.module.TriteJectionModule;
import nl.tritewolf.tritejection.multibinder.TriteJectionMultiBinder;
import nl.tritewolf.tritemenus.items.TriteItemProcessor;
import nl.tritewolf.tritemenus.listeners.TriteInventoryListener;
import nl.tritewolf.tritemenus.menu.TriteMenuBinding;
import nl.tritewolf.tritemenus.menu.TriteMenuContainer;
import nl.tritewolf.tritemenus.menu.TriteMenuProcessor;
import nl.tritewolf.tritemenus.modules.multibindings.ListenerBinding;
import nl.tritewolf.tritemenus.tasks.TriteMenuUpdateTask;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Arrays;
import java.util.List;

@AllArgsConstructor
public final class TriteMenusModule extends TriteJectionModule {

    private final JavaPlugin javaPlugin;

    @Override
    public void bindings() {
        bind(TriteMenuProcessor.class).asEagerSingleton();
        bind(TriteItemProcessor.class).asEagerSingleton();
        bind(TriteMenuContainer.class).asEagerSingleton();
        bind(TriteMenuBinding.class).asEagerSingleton();
        bind(TriteMenuUpdateTask.class).asEagerSingleton();

        bindListeners(new TriteInventoryListener());
    }

    @Override
    public List<TriteJectionMultiBinder> registerMultiBindings() {
        return List.of(new ListenerBinding(this.javaPlugin));
    }

    private void bindListeners(Listener... listeners) {
        Arrays.stream(listeners).forEach(listener -> bind(listener.getClass()).toMultiBinder(Listener.class).asEagerSingleton());
    }
}