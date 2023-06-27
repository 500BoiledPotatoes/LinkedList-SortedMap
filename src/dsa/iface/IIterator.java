package dsa.iface;

/**
 * Iterator ADT.
 */
public interface IIterator<T> {

    /**
     * Check whether the iterator has more elements.
     *
     * @return {@code true} if there are more elements to iterate through, {@code false} otherwise.
     */
    boolean hasNext();

    /**
     * Get the next element from the iterator.
     *
     * @return The next element, or {@code null} if there are no more elements.
     */
    T next();
}
