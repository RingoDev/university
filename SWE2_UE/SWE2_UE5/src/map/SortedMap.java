package map;

/**
 * A {@link Map} which maps {@link Comparable} keys to values. Duplicate keys are not
 * allowed and a key can only map to exactly one value. Keys are inserted in ascending
 * order, which is preserved by the iteration order of all provided iterators.
 *
 * @param <K> The type of the keys. Must be {@link Comparable}.
 * @param <V> The type of the values.
 */
public interface SortedMap<K extends Comparable<? super K> , V> extends Map<K,V>{}
