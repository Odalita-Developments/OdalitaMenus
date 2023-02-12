package nl.odalitadevelopments.menus.utils.collection;

import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

public class Table<R, C, V> implements Iterable<Table.Entry<R, C, V>> {

    private final Map<R, Map<C, V>> rowMap;
    private final Function<R, Map<C, V>> colMapSupplier;

    public Table() {
        this(new HashMap<>(), (Supplier<Map<C, V>>) HashMap::new);
    }

    public Table(Supplier<Map<C, V>> columnMapSupplier) {
        this(new HashMap<>(), columnMapSupplier);
    }

    public Table(Map<R, Map<C, V>> backingRowMap, Supplier<Map<C, V>> columnMapSupplier) {
        this(backingRowMap, (r) -> columnMapSupplier.get());
    }

    public Table(Map<R, Map<C, V>> backingRowMap, Function<R, Map<C, V>> columnMapSupplier) {
        this.rowMap = backingRowMap;
        this.colMapSupplier = columnMapSupplier;
    }


    public V get(R row, C col) {
        return getIfExists(row, col);
    }

    public V getOrDefault(R row, C col, V def) {
        Map<C, V> colMap = getColMapIfExists(row);
        if (colMap == null) {
            return def;
        }

        V v = colMap.get(col);
        if (v != null || colMap.containsKey(col)) {
            return v;
        }
        return def;
    }

    public boolean containsKey(R row, C col) {
        Map<C, V> colMap = getColMapIfExists(row);
        if (colMap == null) {
            return false;
        }
        return colMap.containsKey(col);
    }

    @Nullable
    public V put(R row, C col, V val) {
        return getColMapForWrite(row).put(col, val);
    }

    public void forEach(TableConsumer<R, C, V> consumer) {
        for (Iterator<Entry<R, C, V>> it = this.iterator(); it.hasNext(); ) {
            Entry<R, C, V> entry = it.next();
            consumer.accept(entry.getRow(), entry.getCol(), entry.getValue());
        }
    }

    public void forEach(TablePredicate<R, C, V> predicate) {
        for (Iterator<Entry<R, C, V>> it = this.iterator(); it.hasNext(); ) {
            Entry<R, C, V> entry = it.next();
            if (!predicate.test(entry.getRow(), entry.getCol(), entry.getValue())) {
                return;
            }
        }
    }

    public void removeIf(TablePredicate<R, C, V> predicate) {
        for (Iterator<Entry<R, C, V>> it = this.iterator(); it.hasNext(); ) {
            Entry<R, C, V> entry = it.next();
            if (predicate.test(entry.getRow(), entry.getCol(), entry.getValue())) {
                it.remove();
            }
        }
    }

    public Stream<Entry<R, C, V>> stream() {
        return stream(false);
    }

    public Stream<Entry<R, C, V>> stream(boolean parallel) {
        return StreamSupport.stream(Spliterators.spliteratorUnknownSize(iterator(), 0), parallel);
    }

    public Iterator<Entry<R, C, V>> iterator() {
        return new Iterator<Entry<R, C, V>>() {
            Iterator<Map.Entry<R, Map<C, V>>> rowIter = rowMap.entrySet().iterator();
            Iterator<Map.Entry<C, V>> colIter = null;

            private Map.Entry<R, Map<C, V>> rowEntry;
            private Map.Entry<C, V> colEntry;

            private Entry<R, C, V> next = getNext();

            private Entry<R, C, V> getNext() {
                if (colIter == null || !colIter.hasNext()) {
                    if (!rowIter.hasNext()) {
                        return null;
                    }
                    rowEntry = rowIter.next();
                    colIter = rowEntry.getValue().entrySet().iterator();
                }

                if (!colIter.hasNext()) {
                    return null;
                }

                colEntry = colIter.next();

                return new Node(rowEntry, colEntry);
            }

            @Override
            public boolean hasNext() {
                return this.next != null;
            }

            @Override
            public Entry<R, C, V> next() {
                Entry<R, C, V> entry = this.next;
                this.next = getNext();
                return entry;
            }

            @Override
            public void remove() {
                this.colIter.remove();
            }
        };
    }

    public void replaceAll(TableFunction<R, C, V, V> function) {
        for (Iterator<Entry<R, C, V>> it = this.iterator(); it.hasNext(); ) {
            Entry<R, C, V> entry = it.next();
            entry.setValue(function.compose(entry.getRow(), entry.getCol(), entry.getValue()));
        }
    }

    public V remove(R row, C col) {
        Map<C, V> rowMap = this.rowMap.get(row);
        if (rowMap == null) {
            return null;
        }
        return rowMap.remove(col);
    }

    @Nullable
    public V replace(R row, C col, V val) {
        Map<C, V> rowMap = getColMapIfExists(row);
        if (rowMap == null) {
            return null;
        }
        if (rowMap.get(col) != null || rowMap.containsKey(col)) {
            return rowMap.put(col, val);
        }
        return null;
    }


    @Nullable
    public boolean replace(R row, C col, V old, V val) {
        Map<C, V> rowMap = getColMapIfExists(row);
        if (rowMap == null) {
            return false;
        }
        if (Objects.equals(rowMap.get(col), old)) {
            rowMap.put(col, val);
            return true;
        }
        return false;
    }

    public V computeIfAbsent(R row, C col, BiFunction<R, C, V> function) {
        return getColMapForWrite(row).computeIfAbsent(col, c -> function.apply(row, col));
    }

    public V computeIfPresent(R row, C col, TableFunction<R, C, V, V> function) {
        Map<C, V> colMap = getColMapForWrite(row);
        V v = colMap.computeIfPresent(col, (c, old) -> function.compose(row, col, old));
        removeIfEmpty(row, colMap);
        return v;
    }

    public V compute(R row, C col, TableFunction<R, C, V, V> function) {
        Map<C, V> colMap = getColMapForWrite(row);
        V v = colMap.compute(col, (c, old) -> function.compose(row, col, old));
        removeIfEmpty(row, colMap);
        return v;
    }

    public V merge(R row, C col, V val, TableFunction<R, C, V, V> function) {
        Map<C, V> colMap = getColMapForWrite(row);
        V v = colMap.merge(col, val, (c, old) -> function.compose(row, col, old));
        removeIfEmpty(row, colMap);
        return v;
    }

    public Map<C, V> row(R row) {
        Map<C, V> EMPTY = new HashMap<>(0);
        return new DelegatingMap<C, V>() {
            @Override
            public Map<C, V> delegate(boolean readOnly) {
                if (readOnly) {
                    return Table.this.rowMap.getOrDefault(row, EMPTY);
                }
                return getColMapForWrite(row);
            }

            @Override
            public V remove(Object key) {
                Map<C, V> delegate = delegate(false);
                V remove = delegate.remove(key);
                removeIfEmpty(row, delegate);
                return remove;
            }
            // iterators may leave us empty, but the next get will remove it.
        };
    }
    // Other stuff

    public interface TablePredicate<R, C, V> {
        boolean test(R row, C col, V val);
    }

    public interface TableFunction<R, C, V, RETURN> {
        RETURN compose(R row, C col, V val);
    }

    public interface TableConsumer<R, C, V> {
        void accept(R row, C col, V val);
    }

    private V getIfExists(R row, C col) {
        Map<C, V> colMap = getColMapIfExists(row);
        if (colMap == null) {
            return null;
        }

        return colMap.get(col);
    }

    private Map<C, V> getColMapIfExists(R row) {
        Map<C, V> colMap = this.rowMap.get(row);
        if (colMap != null && colMap.isEmpty()) {
            rowMap.remove(row);
            colMap = null;
        }
        return colMap;
    }

    private Map<C, V> getColMapForWrite(R row) {
        return this.rowMap.computeIfAbsent(row, this.colMapSupplier);
    }

    private void removeIfEmpty(R row, Map<C, V> colMap) {
        if (colMap.isEmpty()) {
            this.rowMap.remove(row);
        }
    }

    public interface Entry<R, C, V> {
        R getRow();

        C getCol();

        V getValue();

        V setValue(V value);
    }

    private class Node implements Entry<R, C, V> {

        private final Map.Entry<R, Map<C, V>> rowEntry;
        private final Map.Entry<C, V> colEntry;

        Node(Map.Entry<R, Map<C, V>> rowEntry, Map.Entry<C, V> entry) {
            this.rowEntry = rowEntry;
            this.colEntry = entry;
        }

        @Override
        public final R getRow() {
            return rowEntry.getKey();
        }

        @Override
        public final C getCol() {
            return colEntry.getKey();
        }

        @Override
        public final V getValue() {
            return colEntry.getValue();
        }

        @Override
        public final V setValue(V value) {
            return colEntry.setValue(value);
        }
    }
}