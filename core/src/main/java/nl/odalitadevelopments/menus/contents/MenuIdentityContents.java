package nl.odalitadevelopments.menus.contents;

import nl.odalitadevelopments.menus.contents.interfaces.IDefaultContents;
import nl.odalitadevelopments.menus.contents.interfaces.IFrameContents;
import nl.odalitadevelopments.menus.contents.interfaces.IPaginationScrollableContents;
import org.jetbrains.annotations.NotNull;

public sealed interface MenuIdentityContents
    extends IDefaultContents, IPaginationScrollableContents, IFrameContents
    permits MenuIdentityContentsImpl {

    @NotNull
    MenuContentsScheduler scheduler();

    @NotNull
    MenuContentsActions actions();

    @NotNull
    MenuContentsEvents events();
}