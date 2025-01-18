package map;


import java.lang.*;
import java.util.Iterator;

public class ArrayMap<K, V> implements Map<K, V> {

    @SuppressWarnings("unchecked")
    protected Entry<K, V>[] entries = (Entry<K, V>[]) new Entry[16];
    protected int nEntries = 0;


    protected int indexOf(K key) {
        for (int i = 0; i < entries.length; i++) {
            if (key.equals(entries[i].getKey())) return i;
        }
        return -1;
    }

    @Override
    public void put(K key, V value) {
        Entry<K, V> newEntry = new SimpleEntry<>(key, value);
        //checking if Key already exists
        if (contains(key)) {//Key exists
            this.entries[indexOf(key)] = newEntry;
        } else {//Key doesnt exist
            if (entries.length > this.nEntries) {//has enough space
                for (int i = 0; i < entries.length; i++) {//find next space
                    if (entries[i] == null) {
                        entries[i] = newEntry;
                        nEntries++;
                        break;
                    }
                }
            } else {//not enough space...array is full
                @SuppressWarnings("unchecked")
                Entry<K, V>[] temp = (Entry<K, V>[]) new Entry[this.entries.length * 2];
                System.arraycopy(this.entries, 0, temp, 0, this.entries.length);
                this.entries = temp;
                for (int i = 0; i < this.entries.length; i++) {//find next space
                    if (this.entries[i] == null) {
                        this.entries[i] = newEntry;
                        this.nEntries++;
                        break;
                    }
                }
            }
        }


    }

    @Override
    public V get(K key) {
        for (int i = 0; i < nEntries; i++) {
            if (key.equals(entries[i].getKey())) return entries[i].getValue();
        }
        return null;
    }

    @Override
    public boolean contains(K key) {
        return get(key) != null;
    }

    @Override
    public boolean remove(K key) {
        if (contains(key)) {
            int i = indexOf(key);
            //copy all entries after removed entry one index lower to not leave null in the middle
            System.arraycopy(this.entries, i + 1, this.entries, i, this.entries.length - (i + 1));
            nEntries--;
            return true;
        }
        return false;
    }

    @Override
    public int size() {
        return this.nEntries;
    }

    @Override
    public Iterator<K> keyIterator() {
        return new Iterator<>() {
            int position = 0;

            @Override
            public boolean hasNext() {
                return position < nEntries;
            }

            @Override
            public K next() {
                return entries[position++].getKey();
            }


        };
    }

    @Override
    public Iterator<V> valueIterator() {
        return new Iterator<>() {
            int position = 0;

            @Override
            public boolean hasNext() {
                return position < nEntries;
            }

            @Override
            public V next() {
                return entries[position++].getValue();
            }


        };
    }

    @Override
    public Iterator<Entry<K, V>> iterator() {
        return new Iterator<>() {
            int position = 0;

            @Override
            public boolean hasNext() {
                return position < nEntries;
            }

            @Override
            public Entry<K, V> next() {
                return entries[position++];
            }


        };
    }


    protected static class SimpleEntry<K, V> implements Map.Entry<K, V> {
        private final K key;
        private final V value;

        protected SimpleEntry(K key, V value) {
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
