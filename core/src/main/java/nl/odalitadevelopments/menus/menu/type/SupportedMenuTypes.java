package nl.odalitadevelopments.menus.menu.type;

import nl.odalitadevelopments.menus.menu.type.types.*;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

public final class SupportedMenuTypes {

    private final Map<MenuType, SupportedMenuType> supportedMenuTypes = new HashMap<>();

    public SupportedMenuTypes() {
        this.register(new AnvilMenuType());
        this.register(new BeaconMenuType());
        this.register(new BlastFurnaceMenuType());
        this.register(new BrewingMenuType());
        this.register(new CartographyMenuType());
        this.register(new ChestOneRowMenuType());
        this.register(new ChestTwoRowMenuType());
        this.register(new ChestThreeRowMenuType());
        this.register(new ChestFourRowMenuType());
        this.register(new ChestFiveRowMenuType());
        this.register(new ChestSixRowMenuType());
        this.register(new CraftingMenuType());
        this.register(new EnchantingMenuType());
        this.register(new FurnaceMenuType());
        this.register(new GrindstoneMenuType());
        this.register(new HopperMenuType());
        this.register(new LecternMenuType());
        this.register(new LoomMenuType());
        this.register(new SmithingMenuType());
        this.register(new SmokerMenuType());
        this.register(new StonecutterMenuType());
        this.register(new WindowThreeThreeMenuType());
    }

    public @NotNull SupportedMenuType getSupportedMenuType(@NotNull MenuType type) {
        SupportedMenuType menuType = this.supportedMenuTypes.get(type);
        if (menuType == null) {
            throw new IllegalArgumentException("The menu type '" + type + "' is not supported.");
        }

        return menuType;
    }

    private void register(@NotNull SupportedMenuType menuType) {
        this.supportedMenuTypes.put(menuType.type(), menuType);
    }
}