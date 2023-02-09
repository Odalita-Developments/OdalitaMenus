package nl.tritewolf.tritemenus.utils.cooldown;

import java.util.concurrent.TimeUnit;

public record Cooldown(long created, long expireDate) {

    public Cooldown(int value, TimeUnit timeUnit) {
        this(System.currentTimeMillis(), System.currentTimeMillis() + timeUnit.toMillis(value));
    }

    public static Cooldown of(long created, long expireDate) {
        return new Cooldown(created, expireDate);
    }

    public static Cooldown of(int value, TimeUnit timeUnit) {
        return new Cooldown(value, timeUnit);
    }

    public long getSecondsLeft() {
        return (this.expireDate - System.currentTimeMillis()) / 1000;
    }

    public boolean isExpired() {
        return System.currentTimeMillis() >= this.expireDate;
    }
}