package nl.tritewolf.tritemenus.utils;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.jetbrains.annotations.NotNull;

@Getter
@Setter
@AllArgsConstructor
public final class Pair<K, V> {

    public static <K, V> @NotNull Pair<K, V> of(K key, V value) {
        return new Pair<>(key, value);
    }

    private K key;
    private V value;

    @Override
    public String toString() {
        return "Pair{" +
                "key=" + key +
                ", value=" + value +
                '}';
    }
}