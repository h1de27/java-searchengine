package datastructures.concrete.dictionaries;

import datastructures.concrete.KVPair;
import datastructures.interfaces.IDictionary;
import misc.exceptions.NoSuchKeyException;

import java.util.Iterator;
import java.util.NoSuchElementException;


/**
 * See the spec and IDictionary for more details on what each method should do
 */
public class ChainedHashDictionary<K, V> implements IDictionary<K, V> {
    // You may not change or rename this field: we will be inspecting
    // it using our private tests.
    private IDictionary<K, V>[] chains;
    private int numPairs;
    private int length;


    // You're encouraged to add extra fields (and helper methods) though!

    public ChainedHashDictionary() {
        this.numPairs = 10;
        this.chains = makeArrayOfChains(10);
        this.length = 0;
    }

    /**
     * This method will return a new, empty array of the given size
     * that can contain IDictionary<K, V> objects.
     *
     * Note that each element in the array will initially be null.
     */
    @SuppressWarnings("unchecked")
    private IDictionary<K, V>[] makeArrayOfChains(int size) {
        // Note: You do not need to modify this method.
        // See ArrayDictionary's makeArrayOfPairs(...) method for
        // more background on why we need this method.
        return (IDictionary<K, V>[]) new IDictionary[size];
    }

    @Override
    public V get(K key) {
        if (this.chains[placementIndex(key)]==null) {
            throw new NoSuchKeyException();
        } else {
            return this.chains[placementIndex(key)].get(key);
        }
    }


    @Override
    public void put(K key, V value) {
        if (this.chains[placementIndex(key)] == null) {
            this.chains[placementIndex(key)] = new ArrayDictionary<K, V>();
        }

        if (!this.chains[placementIndex(key)].containsKey(key)) {
            length++;
        }
        this.chains[placementIndex(key)].put(key, value);

        if ((double) this.length / (double) numPairs > 0.75) {
            IDictionary<K, V>[] newChain = resize(this.chains);
            this.chains = newChain;
        }

    }

    private IDictionary<K, V>[] resize(IDictionary<K, V>[] oldArray) {
        int newSize = numPairs * 2;
        IDictionary<K, V>[] returnArray = makeArrayOfChains(newSize);
        // Traverse through the indexes of the oldArray
        for (int i = 0; i < oldArray.length; i++) {
            // If the index has baskets THAT ARE NOT NULL
            if (oldArray[i] != null) {
                // Traverse each basket based on the key (every KVPair inside an ArrayDictionary)
                for (KVPair<K, V> pairs : oldArray[i]) {
                    if (returnArray[Math.abs(pairs.getKey().hashCode()%newSize)]== null) {
                        returnArray[Math.abs(pairs.getKey().hashCode()%newSize)] = new ArrayDictionary<K, V>();
                    }
                    returnArray[Math.abs(pairs.getKey().hashCode()%newSize)].put(pairs.getKey(), pairs.getValue());
                }
            }
        }
        numPairs *= 2;
        return returnArray;
    }


    @Override
    public V remove(K key) {
        if (this.chains[placementIndex(key)] == null) {
            throw new NoSuchKeyException();
        } else {
            this.length--;
            return this.chains[placementIndex(key)].remove(key);
        }
    }

    @Override
    public boolean containsKey(K key) {
        if (this.chains[placementIndex(key)] == null) {
            return false;
        }
        else {
            return this.chains[placementIndex(key)].containsKey(key);
        }
    }

    @Override
    public int size() {
        return this.length;
    }

    private int placementIndex(K key) {
        if (key == null) {
            return 0;
        } else {
            return Math.abs((key.hashCode()) % numPairs);
        }
    }


    @Override
    public Iterator<KVPair<K, V>> iterator() {
        // Note: you do not need to change this method
        return new ChainedIterator<>(this.chains);
    }

    /**
     * Hints:
     *
     * 1. You should add extra fields to keep track of your iteration
     *    state. You can add as many fields as you want. If it helps,
     *    our reference implementation uses three (including the one we
     *    gave you).
     *
<<<<<<< HEAD
     * 2. Think about what exactly your *invariants* are. Once you've
     *    decided, write them down in a comment somewhere to help you
     *    remember.
     *
     * 3. Before you try and write code, try designing an algorithm
     *    using pencil and paper and run through a few examples by hand.
     *
     *    We STRONGLY recommend you spend some time doing this before
     *    coding. Getting the invariants correct can be tricky, and
     *    running through your proposed algorithm using pencil and
     *    paper is a good way of helping you iron them out.
=======
     * 2. Before you try and write code, try designing an algorithm
     *    using pencil and paper and run through a few examples by hand.
     *
     * 3. Think about what exactly your *invariants* are. An *invariant*
     *    is something that must *always* be true once the constructor is
     *    done setting up the class AND must *always* be true both before and
     *    after you call any method in your class.
     *
     *    Once you've decided, write them down in a comment somewhere to
     *    help you remember.
     *
     *    You may also find it useful to write a helper method that checks
     *    your invariants and throws an exception if they're violated.
     *    You can then call this helper method at the start and end of each
     *    method if you're running into issues while debugging.
     *
     *    (Be sure to delete this method once your iterator is fully working.)
>>>>>>> e9154deef026c8fbe3e89b0544561f18888df89f
     *
     * Implementation restrictions:
     *
     * 1. You **MAY NOT** create any new data structures. Iterators
     *    are meant to be lightweight and so should not be copying
     *    the data contained in your dictionary to some other data
     *    structure.
     *
     * 2. You **MAY** call the `.iterator()` method on each IDictionary
     *    instance inside your 'chains' array, however.
     */

    private static class ChainedIterator<K, V> implements Iterator<KVPair<K, V>> {
        private IDictionary<K, V>[] chains;
        private Iterator<KVPair<K, V>> innerItr;
        private int index = 0;

        public ChainedIterator(IDictionary<K, V>[] chains) {
            this.chains = chains;
            validation();
        }


        @Override
        public boolean hasNext() {
            // if go over the chains, return false
            while (chains.length > index) {
                // check whether bucket is null and it has next element
                // if index is equal to length - 1, it means there is no next element
                if (innerItr != null && innerItr.hasNext()) {
                    return true;
                } else if (index == chains.length - 1) {
                    return false;
                } else {
                    // change bucket
                    this.index++;
                    validation();
                }
            }
            return false;
        }

        @Override
        public KVPair<K, V> next() {
            if (!this.hasNext()) {
                throw new NoSuchElementException();
            } else {
                return innerItr.next();
            }
        }

        private void validation() {
            if (this.chains[index] != null) {
                this.innerItr = chains[index].iterator();
            } else {
                this.innerItr = null;
            }
        }
    }
}
