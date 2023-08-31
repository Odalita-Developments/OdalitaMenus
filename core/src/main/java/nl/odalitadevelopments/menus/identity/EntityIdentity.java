package nl.odalitadevelopments.menus.identity;

import org.bukkit.entity.Entity;

record EntityIdentity<T extends Entity>(T ident) implements Identity<T> {
}