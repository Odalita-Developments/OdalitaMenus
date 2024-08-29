package nl.odalitadevelopments.menus.contents;

import nl.odalitadevelopments.menus.identity.Identity;
import nl.odalitadevelopments.menus.menu.MenuSession;
import org.jetbrains.annotations.NotNull;

final class MenuIdentityContentsImpl<T, I extends Identity<T>> extends MenuContentsImpl implements MenuIdentityContents<T, I> {

    private final I identity;

    MenuIdentityContentsImpl(MenuSession menuSession, I identity) {
        super(menuSession);

        this.identity = identity;
    }

    @Override
    public @NotNull I identity() {
        return this.identity;
    }
}