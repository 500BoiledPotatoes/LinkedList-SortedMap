import dsa.iface.IIterator;
import dsa.iface.IList;
import dsa.iface.IPosition;
import dsa.impl.DLinkedList;
import dsa.impl.ListIterator;

/**
 * This is an implementation of the IMySortedMap interface.
 *
 * It is designed to produce the correct output, but it is not designed to be efficient.
 *
 * If you find any situations where this code does not give correct output, this is not intentional and is a bug.
 *
 * Please report any bugs to me by email at david.lillis@ucd.ie
 */
public class DLSortedMap<K extends Comparable<K>,V> implements IMySortedMap<K, V> {

    // linked list to store the entries
    // this is designed to be a sorted list, so that the entries
    // are stored in order of their keys
    IList<Entry<K,V>> entries = new DLinkedList<>();


    // helper method to get an entry using its key
     private Entry<K,V> getEntryByKey( K key ) {
        // search the entries to find one with the given key
        IIterator<Entry<K,V>> it = entries.iterator();
        while( it.hasNext() ) {
            Entry<K,V> e = it.next();
            // found the correct key
            if ( e.key.equals(key) )
                return e;
        }
        // key was not found, so return null
        return null;
    }

    @Override
    public V get(K key) {
        // check if this key is in the data structure, and return its element if it is
        Entry<K,V> e = getEntryByKey( key );
        return e == null ? null : e.value;
    }

    @Override
    public IIterator<K> getKeysInKeyOrder() {
        // make a list of the keys and return an iterator for that list
        IList<K> keys = new DLinkedList<>();
        IIterator<Entry<K,V>> it = entries.iterator();

        // 'entries' is designed to be in key order, so no sorting is required
        while ( it.hasNext() ) {
            keys.insertLast( it.next().key );
        }
        return new ListIterator<>( keys );
    }

    @Override
    public IIterator<K> getKeysInInsertionOrder() {
        // create a new list that will store the keys
        // in order of insertion (earliest one first)
        IList<K> keyList = new DLinkedList<>();

        // now iterate over all the entries and put each one
        // in the correct position in the keyList (i.e. according to its insertion time)
        IIterator<Entry<K,V>> it = entries.iterator();
        while( it.hasNext() ) {
            Entry<K,V> e = it.next();
            // key list is empty, so just insert this key
            if ( keyList.isEmpty() ) {
                keyList.insertFirst( e.key );
            }
            else {
                // find the position in keyList where this should be inserted
                // (it should be inserted before the first key in the list
                //    that was inserted later, so this is basically insertion sort)
                IPosition<K> pos = keyList.first();
                while( pos != null && e.insertionTime > getEntryByKey( pos.element() ).insertionTime ) {
                    pos = keyList.next( pos );
                }
                // didn't find anything with a later time, so put this one at the end
                if ( pos == null ) {
                    keyList.insertLast( e.key );
                }
                // insert before the first key I found that has a later time
                else {
                    keyList.insertBefore( pos, e.key );
                }
            }
        }

        return new ListIterator<>( keyList );
    }

    @Override
    public V remove(K key) {
        // find the position of this key in the entries list and remove it
        Entry<K,V> e = getEntryByKey( key );

        // this key is not here, so return null
        if ( e == null )
            return null;

        IPosition<Entry<K,V>> pos = entries.first();
        while ( pos.element() != e )
            pos = entries.next( pos );

        // remove this entry from the list ...
        entries.remove( pos );
        // ... and return the value
        return e.value;
    }

    @Override
    public V put(K key, V value) {
        // check if it's already there first
        Entry<K,V> e = getEntryByKey( key );
        // it's not already here, so add it
        if ( e == null ) {

            // if this is the first entry, then just insert it
            if ( entries.isEmpty() )
                entries.insertLast( new Entry( key,value ) );
            // otherwise insert it in the correct place (i.e. before the first key that is greater than this key)
            else {
                IPosition<Entry<K,V>> pos = entries.first();
                // find the first key that is greater than this key (linear search)
                while( pos != null && key.compareTo( pos.element().key ) > 0 )
                    pos = entries.next( pos );
                // no greater key was found, so we can insert this one at the end
                if ( pos == null )
                    entries.insertLast( new Entry( key,value ) );
                // ... or if a greater key was found, insert this new entry before it
                else
                    entries.insertBefore( pos, new Entry( key,value ) );
            }
            // ... return null because this is a new key
            return null;
        }
        // it was already here, so update the value (and insertion time)
        // ... and return the old value
        else  {
            V toReturn = e.value;
            e.value = value;
            e.insertionTime = System.nanoTime();
            return toReturn;
        }
    }

    @Override
    public int size () {
        return entries.size();
    }

    @Override
    public boolean isEmpty() {
        return size() == 0;
    }

    /*
     * Private class to represent an entry, which stores a key/value pair
     */
    private class Entry<K,V> {
        K key; // store the key ...
        V value; // ... and the value ...
        long insertionTime; // ... and the time this entry was inserted

        // constructor
        Entry(K key, V value) {
            this.key = key;
            this.value = value;
            // this gives a measure of time, in nanoseconds
            insertionTime = System.nanoTime();
        }
    }
}
