package nl.tritewolf.tritemenus;

import lombok.Getter;
import nl.tritewolf.tritejection.TriteJection;
import nl.tritewolf.tritejection.utils.AnnotationDetector;
import nl.tritewolf.tritemenus.annotations.AnnotationBinding;
import nl.tritewolf.tritemenus.menu.MenuProcessor;
import nl.tritewolf.tritemenus.modules.MenusModule;
import nl.tritewolf.tritemenus.tasks.MenuSchedulerTask;
import nl.tritewolf.tritemenus.tasks.MenuUpdateTask;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Arrays;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

@Getter
public final class TriteMenus {

    @Getter
    private static TriteJection triteMenus;

    private final JavaPlugin javaPlugin;
    private final MenuProcessor menuProcessor;

    public TriteMenus(JavaPlugin javaPlugin) {
        this.javaPlugin = javaPlugin;

        triteMenus = TriteJection.createTriteJection(new MenusModule(javaPlugin));
        this.menuProcessor = triteMenus.getTriteJection(MenuProcessor.class);

        try {
            AnnotationDetector menuDetector = new AnnotationDetector(triteMenus.getTriteJection(AnnotationBinding.class));

            ClassLoader classLoader = javaPlugin.getClass().getClassLoader();
            String[] objects = Arrays.stream(Package.getPackages()).map(Package::getName).toArray(String[]::new);
            menuDetector.detect(classLoader, objects);
        } catch (Exception e) {
            e.printStackTrace();
        }

        Executors.newSingleThreadScheduledExecutor().scheduleAtFixedRate(triteMenus.getTriteJection(MenuUpdateTask.class), 0, 50, TimeUnit.MILLISECONDS);
        Executors.newSingleThreadScheduledExecutor().scheduleAtFixedRate(triteMenus.getTriteJection(MenuSchedulerTask.class), 0, 50, TimeUnit.MILLISECONDS);
    }
}