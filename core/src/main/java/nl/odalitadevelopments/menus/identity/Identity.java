package nl.odalitadevelopments.menus.identity;

import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public abstract class Identity<T> {

    private final T ident;

    public Identity(@NotNull T ident) {
        this.ident = ident;
    }

    public final @NotNull T ident() {
        return this.ident;
    }

    @SuppressWarnings("unchecked")
    public final @NotNull Class<T> type() {
        return (Class<T>) this.ident.getClass();
    }

    @Override
    public final boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Identity<?> identity = (Identity<?>) o;
        return Objects.equals(this.ident, identity.ident);
    }

    @Override
    public final int hashCode() {
        return Objects.hash(this.ident);
    }
}