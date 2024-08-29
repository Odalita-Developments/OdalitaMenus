package nl.odalitadevelopments.menus.identity;

import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public final class Identity<T> {

    private final IdentityKey<T> key;
    private final T identity;

    public Identity(@NotNull IdentityKey<T> key, @NotNull T identity) {
        this.key = key;
        this.identity = identity;
    }

    public @NotNull IdentityKey<T> identityKey() {
        return this.key;
    }

    public @NotNull T identity() {
        return this.identity;
    }

    public @NotNull String key() {
        return this.key.key();
    }

    public @NotNull Class<? extends T> type() {
        return this.key.type();
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (object == null || getClass() != object.getClass()) return false;

        Identity<?> identity1 = (Identity<?>) object;
        return Objects.equals(key, identity1.key) && Objects.equals(identity, identity1.identity);
    }

    @Override
    public int hashCode() {
        return Objects.hash(key, identity);
    }

    @Override
    public String toString() {
        return "Identity{" +
                "key=" + key +
                ", identity=" + identity +
                '}';
    }
}