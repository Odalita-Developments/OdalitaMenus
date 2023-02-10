package nl.odalitadevelopments.menus.utils;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.jetbrains.annotations.NotNull;

@Getter
@Setter
@AllArgsConstructor
public final class Triple<F, S, T> {

    public static <F, S, T> @NotNull Triple<F, S, T> of(F first, S second, T third) {
        return new Triple<>(first, second, third);
    }

    private F first;
    private S second;
    private T third;
}