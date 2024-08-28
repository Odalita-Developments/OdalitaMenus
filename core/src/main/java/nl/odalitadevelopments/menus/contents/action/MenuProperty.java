package nl.odalitadevelopments.menus.contents.action;

import lombok.AllArgsConstructor;
import lombok.Getter;
import nl.odalitadevelopments.menus.menu.type.MenuType;

@Getter
@AllArgsConstructor
public enum MenuProperty {

    /**
     * The progress of the down-pointing arrow in a brewing inventory.
     */
    BREW_TIME(0, MenuType.BREWING),
    /**
     * The progress of the fuel slot in a brewing inventory.
     * <p>
     * This is a value between 0 and 20, with 0 making the bar empty, and 20
     * making the bar full.
     */
    FUEL_TIME(1, MenuType.BREWING),
    /**
     * The progress of the flame in a furnace inventory.
     */
    BURN_TIME(0, MenuType.FURNACE),
    /**
     * How many total ticks the current fuel should last.
     */
    TICKS_FOR_CURRENT_FUEL(1, MenuType.FURNACE),
    /**
     * The progress of the right-pointing arrow in a furnace inventory.
     */
    COOK_TIME(2, MenuType.FURNACE),
    /**
     * How many total ticks the current smelting should last.
     */
    TICKS_FOR_CURRENT_SMELTING(3, MenuType.FURNACE),
    /**
     * In an enchanting inventory, the top button's experience level.
     * value.
     */
    ENCHANT_BUTTON1(0, MenuType.ENCHANTING),
    /**
     * In an enchanting inventory, the middle button's experience level.
     * value.
     */
    ENCHANT_BUTTON2(1, MenuType.ENCHANTING),
    /**
     * In an enchanting inventory, the bottom button's experience level.
     * value.
     */
    ENCHANT_BUTTON3(2, MenuType.ENCHANTING),
    /**
     * In an enchanting inventory, the first four bits of the player's xpSeed.
     */
    ENCHANT_XP_SEED(3, MenuType.ENCHANTING),
    /**
     * In an enchanting inventory, the top button's enchantment's id.
     */
    ENCHANT_ID1(4, MenuType.ENCHANTING),
    /**
     * In an enchanting inventory, the middle button's enchantment's id.
     */
    ENCHANT_ID2(5, MenuType.ENCHANTING),
    /**
     * In an enchanting inventory, the bottom button's enchantment's id.
     */
    ENCHANT_ID3(6, MenuType.ENCHANTING),
    /**
     * In an enchanting inventory, the top button's level value.
     */
    ENCHANT_LEVEL1(7, MenuType.ENCHANTING),
    /**
     * In an enchanting inventory, the middle button's level value.
     */
    ENCHANT_LEVEL2(8, MenuType.ENCHANTING),
    /**
     * In an enchanting inventory, the bottom button's level value.
     */
    ENCHANT_LEVEL3(9, MenuType.ENCHANTING),
    /**
     * In a beacon inventory, the levels of the beacon.
     */
    LEVELS(0, MenuType.BEACON),
    /**
     * In a beacon inventory, the primary potion effect.
     */
    PRIMARY_EFFECT(1, MenuType.BEACON),
    /**
     * In a beacon inventory, the secondary potion effect.
     */
    SECONDARY_EFFECT(2, MenuType.BEACON),
    /**
     * The repair's cost in xp levels.
     */
    REPAIR_COST(0, MenuType.ANVIL),
    /**
     * The lectern's current open book page.
     */
    BOOK_PAGE(0, MenuType.LECTERN);

    private final int index;
    private final MenuType menuType;
}