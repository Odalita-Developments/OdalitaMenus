package nl.odalitadevelopments.menus.identity;

import org.jetbrains.annotations.NotNull;

import java.lang.reflect.ParameterizedType;

public interface Identity<T> {

    @NotNull T ident();

    @SuppressWarnings("unchecked")
    default @NotNull Class<T> type() {
        return (Class<T>) ((ParameterizedType) this.getClass().getGenericSuperclass()).getActualTypeArguments()[0];
    }
}