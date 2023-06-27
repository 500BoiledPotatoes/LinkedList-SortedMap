import dsa.iface.IIterator;

/**
 * Abstract Data Type representing a type of sorted map.
 *
 * This ADT stores key/value pairs. Keys are unique and can be accessed either in key order or in insertion order.
 *
 * Inserting a key that is already in the map will overwrite the associated value.
 *
 * The "insertion order" is based on the last time a key was put into the map, NOT the first time it was
 *   put into the map. Therefore whenever a key/value pair is added using the put(...) method, it will
 *   be considered to be the last inserted key.
 *
 * A key/value pair can be removed at any time.
 *
 * @param <K> Generic type representing the keys in this map. Keys must be comparable.
 * @param <V> Generic type representing the values in this map.
 */
public interface IMySortedMap<K extends Comparable<K>,V> {

    /**
     * Get the value associated with a given key in the map.
     *
     * @param key The key to search for.
     * @return The value associated with {@code key}, or {@code null} if {@code key}
     * is not a key in the map.
     */
    public V get( K key );

    /**
     * This method allows the keys to be iterated over in ascending order.
     *
     * (i.e. the lowest key should be first and
     * the highest key should be last).
     *
     * @return An {@code dsa.iface.IIterator} that allows iteration over the keys in key order.
     */
    public IIterator<K> getKeysInKeyOrder();

    /**
     * This method allows the keys to be iterated over in insertion order.
     *
     * (i.e. the key that was first inserted
     * into the map should come first, and the key that was most recently inserted into the map should come last).
     *
     * @return An {@code dsa.iface.IIterator} that allows iteration over the keys in insertion order.
     */
    public IIterator<K> getKeysInInsertionOrder();

    /**
     * Remove the key/value pair with key {@code key} from the map.
     *
     * @param key The key to remove.
     * @return The value that was associated with key {@code key}, or {@code null}
     * if {@code key} was not in the map.
     */
    public V remove( K key );

    /**
     * Add a new key/value pair to the map. If the key already exists in the map,
     * this method will replace its associated value, and return the value that
     * was replaced.
     *
     * Even if the key was previously in the map, this key is now the most recent
     *  key to be inserted into the map.
     *
     * @param key The key to add.
     * @param value The value associated with {@code key}.
     * @return The value formerly associated with {@code key}, if {@code key} was
     * already in the map, {@code null} otherwise.
     */
    public V put( K key, V value );

    /**
     * Get the number of entries in the map.
     *
     * @return The number of entries stored in th emap.
     */
    public int size();

    /**
     * Check whether the map is empty.
     *
     * @return {@code true} if the map is empty, {@code false} otherwise.
     */
    public boolean isEmpty();
}
