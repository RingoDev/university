package map;

import java.util.Iterator;
import java.util.NoSuchElementException;

public class ArrayMap<K, V> implements Map<K, V> {
	
	@SuppressWarnings("unchecked")
	protected Entry<K, V>[] entries = (Entry<K, V>[])new Entry[16]; 
	protected int nEntries = 0; 

	@Override
	public void put(K key, V value) {
		int idx = indexOf(key); 
		if (indexOf(key) >= 0) {
			entries[idx] = new AEntry<K, V>(key, value); 
		} else {
			if (entries.length == nEntries) {
				resize(); 
			}
			entries[nEntries] = new AEntry<K, V>(key, value);
		}
		nEntries++; 
	}

	@Override
	public V get(K key) {
		int idx = indexOf(key); 
		if (idx >= 0) {
			return entries[idx].getValue(); 
		} else {
			return null; 
		}
	}

	@Override
	public boolean contains(K key) {
		return indexOf(key) >= 0; 
	}

	@Override
	public boolean remove(K key) {
		int idx = indexOf(key); 
		if (idx >= 0) {
			for (int i = nEntries + 1; i > idx; i--) {
				entries[i - 1] = entries[i]; 
			}
			nEntries--; 
			entries[nEntries] = null;
			return true; 
		} else {
			return false; 
		}
	}

	@Override
	public int size() {
		return nEntries;
	}

	@Override
	public Iterator<K> keyIterator() {
		return new Iterator<K>() {
			int i = 0; 

			@Override
			public boolean hasNext() {
				return i < nEntries;
			}

			@Override
			public K next() {
				if (! hasNext()) throw new NoSuchElementException("No next key"); 
				K k = entries[i].getKey(); 
				i++;
				return k;
			}
			
		};
	}

	@Override
	public Iterator<V> valueIterator() {
		return new Iterator<V>() {
			int i = 0; 

			@Override
			public boolean hasNext() {
				return i < nEntries;
			}

			@Override
			public V next() {
				if (! hasNext()) throw new NoSuchElementException("No next key"); 
				V v = entries[i].getValue(); 
				i++;
				return v;
			}
			
		};
	}

	@Override
	public Iterator<Entry<K, V>> iterator() {
		return new Iterator<Entry<K, V>>() {
			int i = 0; 

			@Override
			public boolean hasNext() {
				return i < nEntries;
			}

			@Override
			public Entry<K, V> next() {
				if (! hasNext()) throw new NoSuchElementException("No next key"); 
				Entry<K, V> e = entries[i]; 
				i++;
				return e;
			}
			
		};	
	}
	
	// protected and private ----------------
	
	private void resize() {
		@SuppressWarnings("unchecked")
		Entry<K, V>[] enlarged = (Entry<K, V>[])new Entry[nEntries * 2]; 
		System.arraycopy(entries, 0, enlarged, 0, entries.length);
		entries = enlarged; 
	}
	
	protected int indexOf(K key) {
		for (int i = 0; i < nEntries; i++) {
			Entry<K, V> entry = entries[i]; 
			if (entry.getKey().equals(key)) {
				return i; 
			}
		}
		return -1;
	}


	protected static class AEntry<K, V> implements Entry<K, V> {
		
		private final K key; 
		private final V value; 
		
		public AEntry(K key, V value) {
			super();
			this.key = key;
			this.value = value;
		}

		@Override
		public K getKey() {
			return key;
		}

		@Override
		public V getValue() {
			return value;
		}
		
	}
	
}
