package nl.odalitadevelopments.menus.identity;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.jetbrains.annotations.NotNull;

public interface Identity<T> {

    static @NotNull Identity<Block> block(@NotNull Block block) {
        return new BlockIdentity(block);
    }

    static @NotNull Identity<Block> block(@NotNull World world, int x, int y, int z) {
        return new BlockIdentity(world, x, y, z);
    }

    static @NotNull Identity<Block> block(@NotNull Location location) {
        return new BlockIdentity(location);
    }

    static @NotNull <T extends Entity> Identity<T> entity(@NotNull T entity) {
        return new EntityIdentity<T>(entity);
    }

    @NotNull T ident();
}