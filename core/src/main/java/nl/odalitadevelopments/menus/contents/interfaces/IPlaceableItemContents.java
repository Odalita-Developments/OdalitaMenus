package nl.odalitadevelopments.menus.contents.interfaces;

import nl.odalitadevelopments.menus.contents.placeableitem.PlaceableItemsCloseAction;
import nl.odalitadevelopments.menus.contents.pos.SlotPos;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.Map;
import java.util.Optional;

public interface IPlaceableItemContents {

    void registerPlaceableItemSlots(int... slots);

    void allowPlaceableItemShiftClick(boolean allowShiftClick);

    void allowPlaceableItemDrag(boolean allowDrag);

    void setForcedPlaceableItem(@NotNull SlotPos slotPos, @NotNull ItemStack itemStack);

    void setForcedPlaceableItem(int row, int column, @NotNull ItemStack itemStack);

    void setForcedPlaceableItem(int slot, @NotNull ItemStack itemStack);

    void placeableItemsCloseAction(@NotNull PlaceableItemsCloseAction action);

    @NotNull Optional<@NotNull SlotPos> firstEmptyPlaceableItemSlot();

    @NotNull Map<Integer, ItemStack> getPlaceableItems();

    void closeInventory(@NotNull Player player, @NotNull PlaceableItemsCloseAction action);
}