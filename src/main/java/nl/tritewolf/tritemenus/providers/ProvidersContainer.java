package nl.tritewolf.tritemenus.providers;

import lombok.Getter;
import lombok.Setter;
import nl.tritewolf.tritemenus.providers.processors.ColorProcessor;
import nl.tritewolf.tritemenus.providers.providers.ColorProvider;

@Getter
@Setter
public class ProvidersContainer {

    public ProvidersContainer() {
        this.colorProvider = new ColorProcessor();
    }

    private ColorProvider colorProvider;

}
