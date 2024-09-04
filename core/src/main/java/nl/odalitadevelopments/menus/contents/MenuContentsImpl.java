package nl.odalitadevelopments.menus.contents;

import nl.odalitadevelopments.menus.contents.action.MenuContentsActions;
import nl.odalitadevelopments.menus.contents.event.MenuContentsEvents;
import nl.odalitadevelopments.menus.contents.scheduler.MenuContentsScheduler;
import nl.odalitadevelopments.menus.menu.MenuSession;
import nl.odalitadevelopments.menus.menu.cache.MenuSessionCache;
import org.jetbrains.annotations.NotNull;

final class MenuContentsImpl implements MenuContents, IMenuContentsImpl, IPlaceableItemsImpl, IFrameContentsImpl, IPaginationScrollableContentsImpl {

    private final MenuSession menuSession;
    private final MenuContentsScheduler scheduler;
    private final MenuContentsActions actions;
    private final MenuContentsEvents events;

    MenuContentsImpl(MenuSession menuSession) {
        this.menuSession = menuSession;

        this.scheduler = MenuContentsScheduler.create(menuSession.cache());
        this.actions = MenuContentsActions.create(menuSession);
        this.events = MenuContentsEvents.create(menuSession);
    }

    @Override
    public @NotNull MenuSession menuSession() {
        return this.menuSession;
    }

    @Override
    public @NotNull MenuContentsScheduler scheduler() {
        return this.scheduler;
    }

    @Override
    public @NotNull MenuContentsActions actions() {
        return this.actions;
    }

    @Override
    public @NotNull MenuContentsEvents events() {
        return this.events;
    }

    @Override
    public @NotNull MenuSessionCache cache() {
        return this.menuSession.cache();
    }
}