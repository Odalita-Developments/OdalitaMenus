package nl.odalitadevelopments.menus.providers.providers;

import nl.odalitadevelopments.menus.utils.cooldown.Cooldown;
import org.jetbrains.annotations.Nullable;

public interface CooldownProvider {

    @Nullable Cooldown pageCooldown();

    @Nullable Cooldown scrollCooldown();

    @Nullable Cooldown frameLoadCooldown();
}