package nl.odalitadevelopments.menus.providers.processors;

import nl.odalitadevelopments.menus.providers.providers.CooldownProvider;
import nl.odalitadevelopments.menus.utils.cooldown.Cooldown;

import java.util.concurrent.TimeUnit;

public final class CooldownProcessor implements CooldownProvider {

    @Override
    public Cooldown pageCooldown() {
        return Cooldown.of(250, TimeUnit.MILLISECONDS);
    }

    @Override
    public Cooldown scrollCooldown() {
        return Cooldown.of(500, TimeUnit.MILLISECONDS);
    }

    @Override
    public Cooldown frameLoadCooldown() {
        return Cooldown.of(500, TimeUnit.MILLISECONDS);
    }
}