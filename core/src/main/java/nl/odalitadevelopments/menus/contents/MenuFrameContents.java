package nl.odalitadevelopments.menus.contents;

import nl.odalitadevelopments.menus.contents.frame.MenuFrameData;
import nl.odalitadevelopments.menus.contents.interfaces.IMenuContents;
import nl.odalitadevelopments.menus.contents.interfaces.IPaginationScrollableContents;
import nl.odalitadevelopments.menus.contents.scheduler.MenuContentsScheduler;
import org.jetbrains.annotations.NotNull;

public sealed interface MenuFrameContents
        extends IMenuContents, IPaginationScrollableContents
        permits MenuFrameContentsImpl {

    @NotNull
    MenuContentsScheduler scheduler();

    @NotNull MenuFrameData menuFrameData();
}