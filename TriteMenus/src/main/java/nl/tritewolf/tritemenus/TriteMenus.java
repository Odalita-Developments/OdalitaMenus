package nl.tritewolf.tritemenus;

import lombok.Getter;
import nl.tritewolf.tritejection.TriteJection;
import nl.tritewolf.tritemenus.modules.TriteMenusModule;

@Getter
public class TriteMenus {

    public TriteMenus() {
        TriteJection.createTriteJection(new TriteMenusModule());
    }
}
