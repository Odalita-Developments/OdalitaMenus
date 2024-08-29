package nl.odalitadevelopments.menus.identity;

import org.jetbrains.annotations.NotNull;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

public record IdentityKey<T>(@NotNull String key, @NotNull Class<? extends T> type) {

    private static final Set<String> USED_KEYS = new HashSet<>();

    public IdentityKey {
        if (!USED_KEYS.add(key)) {
            throw new IllegalArgumentException("Key " + key + " is already used.");
        }
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (object == null || getClass() != object.getClass()) return false;

        IdentityKey<?> that = (IdentityKey<?>) object;
        return Objects.equals(key, that.key) && Objects.equals(type, that.type);
    }

    @Override
    public int hashCode() {
        return Objects.hash(key, type);
    }

    @Override
    public String toString() {
        return "IdentityKey{" +
                "key='" + key + '\'' +
                ", type=" + type +
                '}';
    }
}