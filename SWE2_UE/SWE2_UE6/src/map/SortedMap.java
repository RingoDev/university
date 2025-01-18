package map;

/**
 * A map which is sorted according to the keys. 
 *
 * @param <K> The type of the keys
 * @param <V> The type of the values
 */
public interface SortedMap<K extends Comparable<? super K>, V> 
	extends Map<K, V> {

}
