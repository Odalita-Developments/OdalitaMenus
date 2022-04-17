package nl.tritewolf.tritemenus;

import lombok.Getter;
import nl.tritewolf.tritejection.TriteJection;
import nl.tritewolf.tritejection.utils.AnnotationDetector;
import nl.tritewolf.tritemenus.menu.TriteMenuBinding;
import nl.tritewolf.tritemenus.modules.TriteMenusModule;

import java.util.Arrays;

@Getter
public class TriteMenus {

    @Getter
    static TriteJection triteMenus;

    public TriteMenus() {
        triteMenus = TriteJection.createTriteJection(new TriteMenusModule());

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
