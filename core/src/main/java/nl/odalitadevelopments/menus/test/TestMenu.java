package nl.odalitadevelopments.menus.test;

import nl.odalitadevelopments.menus.contents.MenuContents;
import nl.odalitadevelopments.menus.items.DisplayItem;
import nl.odalitadevelopments.menus.iterators.MenuIteratorType;
import nl.odalitadevelopments.menus.iterators.MenuObjectIterator;
import nl.odalitadevelopments.menus.menu.providers.PlayerMenuProvider;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;

public class TestMenu implements PlayerMenuProvider {

    @Override
    public void onLoad(@NotNull Player player, @NotNull MenuContents contents) {
        Collection<Report> reports = List.of(
                new Report(0, System.currentTimeMillis(), UUID.randomUUID(), UUID.randomUUID()),
                new Report(1, System.currentTimeMillis(), UUID.randomUUID(), UUID.randomUUID()),
                new Report(2, System.currentTimeMillis(), UUID.randomUUID(), UUID.randomUUID()),
                new Report(3, System.currentTimeMillis(), UUID.randomUUID(), UUID.randomUUID()),
                new Report(4, System.currentTimeMillis(), UUID.randomUUID(), UUID.randomUUID()),
                new Report(5, System.currentTimeMillis(), UUID.randomUUID(), UUID.randomUUID()),
                new Report(6, System.currentTimeMillis(), UUID.randomUUID(), UUID.randomUUID()),
                new Report(7, System.currentTimeMillis(), UUID.randomUUID(), UUID.randomUUID()),
                new Report(8, System.currentTimeMillis(), UUID.randomUUID(), UUID.randomUUID()),
                new Report(9, System.currentTimeMillis(), UUID.randomUUID(), UUID.randomUUID()),
                new Report(10, System.currentTimeMillis(), UUID.randomUUID(), UUID.randomUUID()),
                new Report(11, System.currentTimeMillis(), UUID.randomUUID(), UUID.randomUUID()),
                new Report(12, System.currentTimeMillis(), UUID.randomUUID(), UUID.randomUUID()),
                new Report(13, System.currentTimeMillis(), UUID.randomUUID(), UUID.randomUUID())
        );

        MenuObjectIterator<Report> iterator = contents.createObjectIterator(MenuIteratorType.HORIZONTAL, 0, 0, reports, (report) -> {
            return DisplayItem.of(new ItemStack(Material.PAPER));
        });

        iterator.sort(Comparator.comparingLong(Report::createdAt));
        iterator.sort(Comparator.comparingLong(Report::createdAt).reversed());
    }

    private record Report(int id, long createdAt, UUID reporter, UUID target) {
    }
}