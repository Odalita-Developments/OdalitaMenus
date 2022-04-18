package nl.tritewolf.tritemenus;

import lombok.Getter;
import nl.tritewolf.tritejection.TriteJection;
import nl.tritewolf.tritejection.utils.AnnotationDetector;
import nl.tritewolf.tritemenus.menu.TriteMenuBinding;
import nl.tritewolf.tritemenus.menu.TriteMenuProcessor;
import nl.tritewolf.tritemenus.modules.TriteMenusModule;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Arrays;

@Getter
public class TriteMenus {

    @Getter
    private static TriteJection triteMenus;

    private final JavaPlugin javaPlugin;
    private final TriteMenuProcessor menuProcessor;

    public TriteMenus(JavaPlugin javaPlugin) {
        this.javaPlugin = javaPlugin;
        triteMenus = TriteJection.createTriteJection(new TriteMenusModule(javaPlugin));
        this.menuProcessor = triteMenus.getTriteJection(TriteMenuProcessor.class);

        try {
            AnnotationDetector annotationDetector = new AnnotationDetector(triteMenus.getTriteJection(TriteMenuBinding.class));

            ClassLoader classLoader = triteMenus.getClass().getClassLoader();
            String[] objects = Arrays.stream(Package.getPackages()).map(Package::getName).toArray(String[]::new);
            System.out.println(Arrays.toString(objects));
            annotationDetector.detect(classLoader, objects);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
