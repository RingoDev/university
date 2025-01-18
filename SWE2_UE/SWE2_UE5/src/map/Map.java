package map;

import java.util.Iterator;

/**
 * Interface representing an object which maps keys to values. Duplicate keys are not
 * allowed and a key can only map to exactly one value.
 *
 * @param <K> The type of the keys.
 * @param <V> The type of the values.
 */
public interface Map<K, V> extends Iterable<Map.Entry<K, V>> {

    /**
     * Adds a value for the given key to the map. If a mapping already exists, it is
     * replaced with the new key-value pair.
     *
     * @param key   The key.
     * @param value The value.
     */
    void put(K key, V value);

    /**
     * Returns the value assigned to a given key or <code>null</code> if no matching entry
     * is found.
     *
     * @param key The key.
     * @return The value assigned to the key, or <code>null</code> if no matching entry is
     * found.
     */
    V get(K key);

    /**
     * Checks whether an entry exists for the given key.
     *
     * @param key The key.
     * @return <code>true</code> if this map contains an entry for the given key,
     * <code>false</code> otherwise.
     */
    boolean contains(K key);

    /**
     * Removes the entry for the given key if it exists. Otherwise, the map is not
     * changed.
     *
     * @param key The key.
     * @return <code>true</code> if the entry for the given key was removed,
     * <code>false</code> otherwise.
     */
    boolean remove(K key);

    /**
     * Returns the number of entries in this map
     *
     * @return The number of entries in this map.
     */
    int size();

    /**
     * Returns an iterator that iterates over all keys in this map.
     *
     * @return An iterator that iterates over all keys in this map.
     */
    Iterator<K> keyIterator();

    /**
     * Returns an iterator that iterates over all values in this map.
     *
     * @return An iterator that iterates over all values in this map.
     */
    Iterator<V> valueIterator();

    /**
     * Returns an iterator that iterates over all key-value pairs in this map.
     *
     * @return An iterator that iterates over all key-value pairs in this map.
     */
    Iterator<Entry<K, V>> iterator();

    /**
     * Interface representing a key-value pair for maps.
     *
     * @param <K> The type of the key.
     * @param <V> The type of the value.
     */
    interface Entry<K, V> {

        /**
         * Returns the key corresponding to this entry.
         *
         * @return The key corresponding to this entry.
         */
        K getKey();

        /**
         * Returns the value corresponding to this entry.
         *
         * @return The value corresponding to this entry.
         */
        V getValue();

    }

}
