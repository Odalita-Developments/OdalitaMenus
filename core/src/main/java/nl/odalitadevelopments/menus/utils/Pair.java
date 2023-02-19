package nl.odalitadevelopments.menus.utils;

import org.jetbrains.annotations.NotNull;

public final class Pair<K, V> {

    public static <K, V> @NotNull Pair<K, V> of(K key, V value) {
        return new Pair<>(key, value);
    }

    private K key;
    private V value;

    private Pair(K key, V value) {
        this.key = key;
        this.value = value;
    }

    public K getKey() {
        return this.key;
    }

    public V getValue() {
        return this.value;
    }

    public void setKey(K key) {
        this.key = key;
    }

    public void setValue(V value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return "Pair{" +
                "key=" + this.key +
                ", value=" + this.value +
                '}';
    }
}