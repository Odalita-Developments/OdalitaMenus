package nl.tritewolf.tritemenus.menu.type;

import nl.tritewolf.tritemenus.menu.type.types.*;
import org.bukkit.event.inventory.InventoryType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;

public final class SupportedMenuTypes {

    private final Map<InventoryType, MenuType> supportedMenuTypes = new HashMap<>();

    public SupportedMenuTypes() {
        this.register(new AnvilMenuType());
        this.register(new BarrelMenuType());
        this.register(new BeaconMenuType());
        this.register(new BlastFurnaceMenuType());
        this.register(new BrewingMenuType());
        this.register(new CartographyMenuType());
        this.register(new ChestMenuType());
        this.register(new DispenserMenuType());
        this.register(new DropperMenuType());
        this.register(new EnchantingMenuType());
        this.register(new EnderChestMenuType());
        this.register(new FurnaceMenuType());
        this.register(new GrindstoneMenuType());
        this.register(new HopperMenuType());
        this.register(new LecternMenuType());
        this.register(new LoomMenuType());
        this.register(new ShulkerBoxMenuType());
        this.register(new SmithingMenuType());
        this.register(new SmokerMenuType());
        this.register(new StonecutterMenuType());
        this.register(new WorkbenchMenuType());
    }

    @Nullable
    public MenuType getSupportedMenuType(@NotNull InventoryType type) {
        return this.supportedMenuTypes.get(type);
    }

    private void register(@NotNull MenuType menuType) {
        this.supportedMenuTypes.put(menuType.type(), menuType);
    }
}