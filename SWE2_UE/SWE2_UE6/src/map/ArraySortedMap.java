package map;

public class ArraySortedMap<K extends Comparable<? super K>, V>
	extends ArrayMap<K, V> 
	implements SortedMap<K, V> {

	@Override
	public void put(K key, V value) {
		int idx = indexOf(key); 
		if (indexOf(key) >= 0) {
			entries[idx] = new AEntry<K, V>(key, value); 
		} else {
			super.put(key, value); 
			// sort
			for (int i = nEntries; i > 0; i--) {
				if (entries[i].getKey().compareTo(entries[i-1].getKey()) < 0) { // i smaller i-1
					Entry<K, V> temp = entries[i-1]; 
					entries[i-1] = entries[i]; 
					entries[i] = temp; 
				}
			}
		}
	}
	
	// protected ----------------
	
	@Override
	protected int indexOf(K key) {
		for (int i = 0; i < nEntries; i++) {
			Entry<K, V> entry = entries[i]; 
			if (entry.getKey().equals(key)) {
				return i; 
			}
			if (entry.getKey().compareTo(key) > 0) {
				return -1; 
			}
		}
		return -1;
	}

}
