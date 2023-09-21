package nl.odalitadevelopments.menus.contents;

import nl.odalitadevelopments.menus.contents.interfaces.IDefaultContents;
import nl.odalitadevelopments.menus.contents.interfaces.IPaginationScrollableContents;
import nl.odalitadevelopments.menus.menu.MenuSession;
import org.jetbrains.annotations.NotNull;

public sealed interface MenuFrameContents
        extends IDefaultContents, IPaginationScrollableContents
        permits MenuFrameContentsImpl {

    @NotNull
    MenuSession menuSession();

    @NotNull
    MenuContentsScheduler scheduler();

    @NotNull MenuFrameData menuFrameData();
}