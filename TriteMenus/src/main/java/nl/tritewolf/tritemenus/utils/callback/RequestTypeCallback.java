package nl.tritewolf.tritemenus.utils.callback;

public interface RequestTypeCallback<T, P> {

    T request(P param);
}