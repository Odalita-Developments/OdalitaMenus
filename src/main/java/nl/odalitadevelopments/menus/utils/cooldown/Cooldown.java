package nl.odalitadevelopments.menus.utils.cooldown;

import java.util.concurrent.TimeUnit;

public record Cooldown(long created, long expireDate, int value, TimeUnit unit) {

    public Cooldown(int value, TimeUnit timeUnit) {
        this(System.currentTimeMillis(), System.currentTimeMillis() + timeUnit.toMillis(value), value, timeUnit);
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