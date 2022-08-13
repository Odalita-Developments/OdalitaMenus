package nl.tritewolf.tritemenus.modules;

import lombok.AllArgsConstructor;
import nl.tritewolf.tritejection.module.TriteJectionModule;
import nl.tritewolf.tritejection.multibinder.TriteJectionMultiBinder;
import nl.tritewolf.tritemenus.annotations.AnnotationBinding;
import nl.tritewolf.tritemenus.items.ItemProcessor;
import nl.tritewolf.tritemenus.listeners.InventoryListener;
import nl.tritewolf.tritemenus.menu.MenuProcessor;
import nl.tritewolf.tritemenus.menu.type.SupportedMenuTypes;
import nl.tritewolf.tritemenus.modules.multibindings.ListenerBinding;
import nl.tritewolf.tritemenus.patterns.PatternContainer;
import nl.tritewolf.tritemenus.tasks.MenuUpdateTask;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.List;

@AllArgsConstructor
public final class MenusModule extends TriteJectionModule {

    private final JavaPlugin javaPlugin;

    @Override
    public void bindings() {
        this.bind(MenuProcessor.class).asEagerSingleton();
        this.bind(ItemProcessor.class).asEagerSingleton();
        this.bind(SupportedMenuTypes.class).asEagerSingleton();
        this.bind(AnnotationBinding.class).asEagerSingleton();
        this.bind(MenuUpdateTask.class).asEagerSingleton();
        this.bind(PatternContainer.class).asEagerSingleton();

        this.bindListeners(new InventoryListener());
    }

    @Override
    public List<TriteJectionMultiBinder> registerMultiBindings() {
        return List.of(new ListenerBinding(this.javaPlugin));
    }

    private void bindListeners(Listener @NotNull ... listeners) {
        Arrays.stream(listeners).forEach(listener -> this.bind(listener.getClass()).toMultiBinder(Listener.class).asEagerSingleton());
    }
}