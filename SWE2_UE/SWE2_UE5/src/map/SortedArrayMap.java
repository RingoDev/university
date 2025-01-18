package map;
import java.lang.Comparable;

public class SortedArrayMap<K extends Comparable<? super K>, V> extends ArrayMap<K, V> implements SortedMap<K, V> {

    @Override
    public void put(K key, V value) {
        Entry<K, V> newEntry = new SimpleEntry<>(key, value);
        //checking if Key already exists
        if (contains(key)) {//Key exists
            super.entries[indexOf(key)] = newEntry;
        } else {//Key doesnt exist
            if (entries.length == super.nEntries) {//not enough space...array is full
                increaseSize();
            }
            sortedInsert(newEntry);
        }
    }

    protected int indexOf(K key) {
        for (int i = 0; i < nEntries; i++) {
            if (key.equals(entries[i].getKey())) return i;

            //stop search because List is sorted in increasing order
            if (key.compareTo(entries[i].getKey()) < 0) return -1;
        }
        return -1;
    }

    private void sortedInsert(Entry<K, V> entry) {
        for (int i = 0; i < super.entries.length; i++) {//find next space
            if (super.entries[i] != null) {
                if (entry.getKey().compareTo(super.entries[i].getKey()) < 1) {//if -1 or 0 insert
                    //copying all entries 1 position further back
                    System.arraycopy(super.entries, i, super.entries, i + 1, super.entries.length - (i + 1));
                    super.entries[i] = entry;//inserting entry on freed position
                    super.nEntries++;
                    break;
                }
            } else {//if no comparison resulted in 0 or -1 and we found first null element
                super.entries[i] = entry;
                super.nEntries++;
                break;
            }
        }
    }

    private void increaseSize() {
        @SuppressWarnings("unchecked")
        Entry<K, V>[] temp = (Entry<K, V>[]) new Entry[this.entries.length * 2];
        System.arraycopy(this.entries, 0, temp, 0, this.entries.length);
        this.entries = temp;
    }
}
