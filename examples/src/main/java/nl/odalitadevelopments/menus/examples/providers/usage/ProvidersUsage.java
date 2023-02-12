package nl.odalitadevelopments.menus.examples.providers.usage;

import nl.odalitadevelopments.menus.OdalitaMenus;
import nl.odalitadevelopments.menus.examples.providers.CustomColorProvider;
import nl.odalitadevelopments.menus.examples.providers.CustomCooldownProvider;
import nl.odalitadevelopments.menus.examples.providers.CustomDefaultItemProvider;

public final class ProvidersUsage {

    // Example method to register providers for this instance
    public void registerProviders(OdalitaMenus instance) {
        instance.getProvidersContainer().setColorProvider(new CustomColorProvider());
        instance.getProvidersContainer().setCooldownProvider(new CustomCooldownProvider());
        instance.getProvidersContainer().setDefaultItemProvider(new CustomDefaultItemProvider());
    }
}