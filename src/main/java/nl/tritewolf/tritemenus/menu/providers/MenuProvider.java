package nl.tritewolf.tritemenus.menu.providers;

import org.jetbrains.annotations.ApiStatus;

@ApiStatus.Internal
public sealed interface MenuProvider permits GlobalMenuProvider, PlayerMenuProvider {
}