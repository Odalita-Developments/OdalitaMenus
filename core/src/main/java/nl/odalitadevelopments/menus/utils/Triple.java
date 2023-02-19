package nl.odalitadevelopments.menus.utils;

import org.jetbrains.annotations.NotNull;

public final class Triple<F, S, T> {

    public static <F, S, T> @NotNull Triple<F, S, T> of(F first, S second, T third) {
        return new Triple<>(first, second, third);
    }

    private F first;
    private S second;
    private T third;

    private Triple(F first, S second, T third) {
        this.first = first;
        this.second = second;
        this.third = third;
    }

    public F getFirst() {
        return this.first;
    }

    public S getSecond() {
        return this.second;
    }

    public T getThird() {
        return this.third;
    }

    public void setFirst(F first) {
        this.first = first;
    }

    public void setSecond(S second) {
        this.second = second;
    }

    public void setThird(T third) {
        this.third = third;
    }

    @Override
    public String toString() {
        return "Triple{" +
                "first=" + this.first +
                ", second=" + this.second +
                ", third=" + this.third +
                '}';
    }
}