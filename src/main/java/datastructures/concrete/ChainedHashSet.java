package datastructures.concrete;

import datastructures.concrete.dictionaries.ChainedHashDictionary;
import datastructures.interfaces.IDictionary;
import datastructures.interfaces.ISet;


import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * Use ISet as interface.
 */
public class ChainedHashSet<T> implements ISet<T> {

    private IDictionary<T, Boolean> map;

    public ChainedHashSet() {
        this.map = new ChainedHashDictionary<>();
    }

    @Override
    public void add(T item) {
        if (!contains(item)) {
            this.map.put(item, true);
        }
    }

    @Override
    public void remove(T item) {
        if (!contains(item)) {
            throw new NoSuchElementException();
        }
        this.map.remove(item);
    }

    @Override
    public boolean contains(T item) {
        return this.map.containsKey(item);
    }

    @Override
    public int size() {
        return this.map.size();
    }

    @Override
    public Iterator<T> iterator() {
        return new SetIterator<>(this.map.iterator());
    }

    private static class SetIterator<T> implements Iterator<T> {

        private Iterator<KVPair<T, Boolean>> iter;

        public SetIterator(Iterator<KVPair<T, Boolean>> iter) {
            this.iter = iter;
        }

        @Override
        public boolean hasNext() {
            return this.iter.hasNext();
        }

        @Override
        public T next() {
            if (!iter.hasNext()) {
                throw new NoSuchElementException();
            } else {
                return this.iter.next().getKey();
            }

        }
    }
}
