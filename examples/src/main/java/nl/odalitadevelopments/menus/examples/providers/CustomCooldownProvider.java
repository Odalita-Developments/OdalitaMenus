package nl.odalitadevelopments.menus.examples.providers;

import nl.odalitadevelopments.menus.providers.providers.CooldownProvider;
import nl.odalitadevelopments.menus.utils.cooldown.Cooldown;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.TimeUnit;

public final class CustomCooldownProvider implements CooldownProvider {

    @Override
    public Cooldown pageCooldown() {
        return Cooldown.of(500, TimeUnit.MILLISECONDS);
    }

    @Override
    public Cooldown scrollCooldown() {
        return Cooldown.of(250, TimeUnit.MILLISECONDS);
    }

    @Override
    public @Nullable Cooldown frameLoadCooldown() {
        return null; // No cooldown
    }
}