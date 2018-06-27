package datastructures.concrete.dictionaries;

// Import Iterator
import java.util.Iterator;

// Import data structures
import datastructures.concrete.KVPair;
import datastructures.interfaces.IDictionary;

// Import exceptions
import java.util.NoSuchElementException;
import misc.exceptions.NoSuchKeyException;

/**
 * See IDictionary for more details on what this class should do
 */
public class ArrayDictionary<K, V> implements IDictionary<K, V> {
    // You may not change or rename this field: we will be inspecting
    // it using our private tests.
    private Pair<K, V>[] pairs;
    private int size;

    // You're encouraged to add extra fields (and helper methods) though!

    public ArrayDictionary() {
        this.size = 0;
        this.pairs = makeArrayOfPairs(1);
    }

    /**
     * This method will return a new, empty array of the given size
     * that can contain Pair<K, V> objects.
     *
     * Note that each element in the array will initially be null.
     */
    @SuppressWarnings("unchecked")
    private Pair<K, V>[] makeArrayOfPairs(int arraySize) {
        // It turns out that creating arrays of generic objects in Java
        // is complicated due to something known as 'type erasure'.
        //
        // We've given you this helper method to help simplify this part of
        // your assignment. Use this helper method as appropriate when
        // implementing the rest of this class.
        //
        // You are not required to understand how this method works, what
        // type erasure is, or how arrays and generics interact. Do not
        // modify this method in any way.
        return (Pair<K, V>[]) (new Pair[arraySize]);

    }

    @Override
    public V get(K key) {
        int index = this.getIdx(key);
        if (index == -1) {
            throw new NoSuchKeyException();
        } else {
            return this.pairs[index].value;
        }
    }

    @Override
    public void put(K key, V value) {
        int index = this.getIdx(key);
        if (index != -1) {
            this.pairs[index].value = value;
        } else {
            int emptyIdx = this.emptyIdx();
            Pair<K, V> newPair = new Pair<K, V>(key, value);
            if (emptyIdx == -1) {
                int oldSize = this.pairs.length;
                Pair<K, V>[] newArray = this.makeArrayOfPairs(oldSize * 2);
                for (int i = 0; i < oldSize; i++) {
                    newArray[i] = this.pairs[i];
                }
                emptyIdx = oldSize;
                this.pairs = newArray;
            }
            this.pairs[emptyIdx] = newPair;
            size++;
        }
    }


    @Override
    public V remove(K key) {
        V val;
        if (this.getIdx(key) == -1) {
            throw new NoSuchKeyException();
        } else {
            val = this.pairs[this.getIdx(key)].value;
            for (int i = this.getIdx(key); i < this.size() - 1; i++) {
                this.pairs[i] = this.pairs[i + 1];
            }
            this.pairs[this.size() - 1] = null;
        }
        size--;
        return val;
    }

    @Override
    public boolean containsKey(K key) {
        return getIdx(key) != -1;
    }

    @Override
    public int size() {
        return this.size;
    }

    private int getIdx(K key) {
        for (int i = 0; i < this.size(); i++) {
            if (key == null && this.pairs[i].key == null) {
                return i;
            }
            if (this.pairs[i].key != null && this.pairs[i].key.equals(key)) {
                return i;
            }
        }
        return -1;
    }

    private int emptyIdx(){
        for (int i = 0; i < this.pairs.length; i++) {
            if (this.pairs[i] == null) {
                return i;
            }
        }
        return -1;
    }

    @Override
    public Iterator<KVPair<K, V>> iterator() {
        return new ArrayDictionaryIterator<K, V>(this.pairs, this.size);
    }

    private static class ArrayDictionaryIterator<K, V> implements Iterator<KVPair<K, V>> {
        private Pair<K, V>[] pairs;
        private int size;
        private KVPair<K, V> current;
        private int i = 0;

        public ArrayDictionaryIterator(Pair<K, V>[] pairs, int size) {
            this.pairs = pairs;
            this.size = size;
        }
        /**
         * Returns 'true' if the iterator still has elements to look at;
         * returns 'false' otherwise.
         */
        public boolean hasNext() {
            return i < size;
        }

        /**
         * Returns the next item in the iteration and internally updates the
         * iterator to advance one element forward.
         *
         * @throws NoSuchElementException if we have reached the end of the iteration and
         *         there are no more elements to look at.
         */
        public KVPair<K, V> next() {
            if (!hasNext()) {
                throw new NoSuchElementException();
            } else {
                this.current = new KVPair<>(pairs[i].key, pairs[i].value);
                i++;
                return this.current;
            }
        }

    }

    private static class Pair<K, V> {
        public K key;
        public V value;

        // You may add constructors and methods to this class as necessary.
        public Pair(K key, V value) {
            this.key = key;
            this.value = value;
        }

        @Override
        public String toString() {
            return this.key + "=" + this.value;
        }
    }
}
