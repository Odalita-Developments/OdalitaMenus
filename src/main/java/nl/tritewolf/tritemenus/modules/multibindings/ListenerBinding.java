package nl.tritewolf.tritemenus.modules.multibindings;

import lombok.AllArgsConstructor;
import nl.tritewolf.tritejection.multibinder.TriteJectionMultiBinder;
import org.bukkit.Bukkit;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

@AllArgsConstructor
public final class ListenerBinding implements TriteJectionMultiBinder {

    private final JavaPlugin javaPlugin;

    @Override
    public Class<?> getMultiBindingClass() {
        return Listener.class;
    }

    @Override
    public void handleMultiBinding(Object listener) {
        Bukkit.getPluginManager().registerEvents((Listener) listener, this.javaPlugin);
    }
}