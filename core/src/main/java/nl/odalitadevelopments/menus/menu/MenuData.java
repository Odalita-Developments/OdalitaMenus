package nl.odalitadevelopments.menus.menu;

import com.google.common.collect.ImmutableMap;
import nl.odalitadevelopments.menus.identity.Identity;
import nl.odalitadevelopments.menus.menu.type.SupportedMenuType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public record MenuData(Map<Key<?>, Object> data) {

    public MenuData {
        data = new ConcurrentHashMap<>(data); // Make sure it's mutable
    }

    public MenuData() {
        this(new HashMap<>());
    }

    public @NotNull Map<Key<?>, Object> data() {
        return ImmutableMap.copyOf(this.data);
    }

    public <T> @Nullable T get(@NotNull Key<T> key) {
        Object value = this.data.get(key);
        return value == null ? null : key.type().cast(value);
    }

    public <T> @NotNull T getOrDefault(@NotNull Key<T> key, @NotNull T defaultValue) {
        Object value = this.data.get(key);
        return value == null ? defaultValue : key.type().cast(value);
    }

    public <T> @NotNull T getOrThrow(@NotNull Key<T> key) {
        T value = this.get(key);
        if (value == null) {
            throw new IllegalStateException("Key " + key.key() + " is not set.");
        }

        return value;
    }

    <T> void set(@NotNull Key<T> key, @Nullable T value) {
        if (!(key instanceof MutableKey<T>)) {
            throw new IllegalArgumentException("Key " + key.key() + " is not mutable.");
        }

        this.data.put(key, value);
    }

    public sealed interface Key<T> permits MutableKey, ImmutableKey {

        // Immutable keys
        Key<UUID> UNIQUE_ID = new ImmutableKey<>("unique-id", UUID.class);
        Key<Identity<?>> IDENTITY = new ImmutableKey<>("identity", Identity.TYPE);

        // Mutable keys
        Key<String> ID = new MutableKey<>("id", String.class);
        Key<SupportedMenuType> TYPE = new MutableKey<>("type", SupportedMenuType.class);
        Key<String> TITLE = new MutableKey<>("title", String.class);
        Key<String> GLOBAL_CACHE_KEY = new MutableKey<>("global_cache_key", String.class);

        @NotNull
        String key();

        @NotNull
        Class<? extends T> type();

        default boolean mutable() {
            return this instanceof MutableKey<T>;
        }
    }

    private record MutableKey<T>(@NotNull String key, @NotNull Class<? extends T> type) implements Key<T> {
    }

    private record ImmutableKey<T>(@NotNull String key, @NotNull Class<? extends T> type) implements Key<T> {
    }
}