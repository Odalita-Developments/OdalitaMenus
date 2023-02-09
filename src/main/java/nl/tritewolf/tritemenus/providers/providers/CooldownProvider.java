package nl.tritewolf.tritemenus.providers.providers;

import nl.tritewolf.tritemenus.utils.cooldown.Cooldown;
import org.jetbrains.annotations.Nullable;

public interface CooldownProvider {

    @Nullable Cooldown pageCooldown();

    @Nullable Cooldown scrollCooldown();

    @Nullable Cooldown frameLoadCooldown();
}