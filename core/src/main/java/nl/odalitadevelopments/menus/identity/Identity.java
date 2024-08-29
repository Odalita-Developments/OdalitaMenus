package nl.odalitadevelopments.menus.identity;

import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public abstract class Identity<T> {

    private final T identity;

    public Identity(@NotNull T identity) {
        this.identity = identity;
    }

    public final @NotNull T identity() {
        return this.identity;
    }

    @SuppressWarnings("unchecked")
    public final @NotNull Class<T> type() {
        return (Class<T>) this.identity.getClass();
    }

    @Override
    public final boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Identity<?> identity = (Identity<?>) o;
        return Objects.equals(this.identity, identity.identity);
    }

    @Override
    public final int hashCode() {
        return Objects.hash(this.identity);
    }

    @Override
    public String toString() {
        return "Identity{" +
                "identity=" + this.identity +
                '}';
    }
}