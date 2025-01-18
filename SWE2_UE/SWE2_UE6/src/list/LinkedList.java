package list;

import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * Linked list implementation of interface {@link List}.  
 * 
 * @param <E> the element type
 */
public class LinkedList<E> implements List<E> {

	@Override
	public int size() {
		return n;
	}

	/**
	 * Adds the element to this list. The element is added at the end.
	 * @param elem  the element to add
	 */
	public void add(E elem) {
		if (head == null) {
			head = new Node<E>(elem);
			tail = head; 
		} else {
			tail.next = new Node<E>(elem);
			tail = tail.next; 
		}
		n++;
	}
	
	@Override
	public boolean contains(Object obj) {
		for (E e : this) {
			if (e.equals(obj)) 
				return true; 
		}
		return false;
	}

	@Override
	public Iterator<E> iterator() {
		return new Iterator<E>() {
			
				private Node<E> current;

				{
					current = head;
				}

				@Override
				public boolean hasNext() {
					return current != null;
				}

				@Override
				public E next() {
					if (current != null) {
						E value = current.value;
						current = current.next;
						return value;
					} else {
						throw new NoSuchElementException("No more element in list");
					}
				}
		};
	}

	@Override
	public boolean addAll(Iterable<? extends E> elems) {
		for (E e : elems) {
			add(e);
		}
		return true;
	}

	@Override
	public boolean containsAll(Iterable<?> elems) {
		for (Object e : elems) {
			if (!contains(e))
				return false;
		}
		return true;
	}
	
	@Override
	public Object[] toArray() {
		Object[] a = new Object[size()];
		int i = 0;
		for (E e : this) {
			a[i++] = e;
		}
		return a;
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T> T[] toArray(T[] a) {
		if (a.length < size()) {
			a = (T[]) java.lang.reflect.Array.newInstance(a.getClass()
					.getComponentType(), size());
		}
		Object[] objs = a;
		int i = 0;
		for (E e : this) {
			objs[i++] = e; 
		}
		return a;
	}

	@Override
	public <R> List<R> createResultList() {
		return new LinkedList<R>();
	}


	// private  --------------------------------------------------------------

	private Node<E> head = null;
	private Node<E> tail = null;

	private int n = 0;

	// inner class Node -------------------------------------------------------

	private static class Node<F> {
		final F value;
		Node<F> next;

		Node(F elem, Node<F> next) {
			this.value = elem;
			this.next = next;
		}
		Node(F elem) {
			this(elem, null);
		}
	}

}
