package nl.odalitadevelopments.menus.identity;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;

record BlockIdentity(Block ident) implements Identity<Block> {

    BlockIdentity(World world, int x, int y, int z) {
        this(world.getBlockAt(x, y, z));
    }

    BlockIdentity(Location location) {
        this(location.getBlock());
    }
}