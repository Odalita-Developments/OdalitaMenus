package nl.tritewolf.tritemenus.providers.processors;

import nl.tritewolf.tritemenus.providers.providers.CooldownProvider;
import nl.tritewolf.tritemenus.utils.cooldown.Cooldown;

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