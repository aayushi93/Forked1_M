package ca.jrvs.practice.dataStructure.list;

/**
 * This is a simplified version of JDK List (java.util.List)
 * JavaDoc is also copied from JDK (java.util.List)
 *
 * @param <T> the type of elements in this list
 *
 * @see java.util.List for JDK full version of List
 */
interface JList<T> {

  /**
   * Appends the specified element to the end of this list (optional
   * operation).
   *
   * @param t element to be appended to this list
   * @return <tt>true</tt>
   * @throws NullPointerException if the specified element is null and this
   *         list does not permit null elements
   */
  boolean add(T t);

  /**
   * Returns an array containing all of the elements in this list in proper
   * sequence (from first to last element).
   *
   * <p>This method acts as bridge between array-based and collection-based
   * APIs.
   *
   * @return an array containing all of the elements in this list in proper
   *         sequence
   */
  Object[] toArray();

  /**
   * The size of the ArrayList (the number of elements it contains).
   *
   */
  int size();

  /**
   * Returns <tt>true</tt> if this list contains no elements.
   *
   * @return <tt>true</tt> if this list contains no elements
   */
  boolean isEmpty();

  /**
   * Returns the index of the first occurrence of the specified element
   * in this list, or -1 if this list does not contain the element.
   * More formally, returns the lowest index <tt>i</tt> such that
   * <tt>(o==null&nbsp;?&nbsp;get(i)==null&nbsp;:&nbsp;o.equals(get(i)))</tt>,
   * or -1 if there is no such index.
   */
  int indexOf(T t);

  /**
   * Returns <tt>true</tt> if this list contains the specified element.
   * More formally, returns <tt>true</tt> if and only if this list contains
   * at least one element <tt>e</tt> such that
   * <tt>(o==null&nbsp;?&nbsp;e==null&nbsp;:&nbsp;o.equals(e))</tt>.
   *
   * @param t element whose presence in this list is to be tested
   * @return <tt>true</tt> if this list contains the specified element
   * @throws NullPointerException if the specified element is null and this
   *         list does not permit null elements
   */
  boolean contains(T t);

  /**
   * Returns the element at the specified position in this list.
   *
   * @param index index of the element to return
   * @return the element at the specified position in this list
   * @throws IndexOutOfBoundsException if the index is out of range
   *         (<tt>index &lt; 0 || index &gt;= size()</tt>)
   */
  T get(int index);

  /**
   * Removes the element at the specified position in this list.
   * Shifts any subsequent elements to the left (subtracts one from their
   * indices).
   *
   * @param index the index of the element to be removed
   * @return the element that was removed from the list
   * @throws IndexOutOfBoundsException {@inheritDoc}
   */
  T remove(int index);

  /**
   * Removes all of the elements from this list (optional operation).
   * The list will be empty after this call returns.
   */
  void clear();

}