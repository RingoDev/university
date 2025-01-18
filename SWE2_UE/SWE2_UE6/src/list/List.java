package list;

import java.util.Comparator;
import java.util.Optional;
import java.util.function.BiFunction;
import java.util.function.BinaryOperator;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.function.UnaryOperator;

public interface List<E> extends Iterable<E> {
	
	/** Creates a list with the given elements
	 * @param <T> the element type
	 * @param ts the elements for the list
	 * @return the list with the given elements
	 */
	public static <T> List<T> of(@SuppressWarnings("unchecked") T...ts) {
		List<T> list = new LinkedList<T>(); 
		for (T t: ts) {
			list.add(t);
		}
		return list; 
	}
	
	/**
	 * Returns the number of elements of this list
	 * 
	 * @return the number of elements
	 */
	public int size();

	/**
	 * Tests if this list is empty.
	 * 
	 * @return <code>true</code> if empty, <code>false</code> otherwise
	 */
	public default boolean isEmpty() {
		return size() == 0;
	}

	/**
	 * Adds an element to this list
	 * 
	 * @param elem the element to be added
	 */
	public void add(E elem);

	/**
	 * Tests if the object is contained in this list
	 * 
	 * @param obj the object to be tested
	 * @return <code>true</code> if object is contained, <code>false</code>
	 *         otherwise
	 */
	public boolean contains(Object obj);

	/**
	 * Adds all elements to this list.
	 * 
	 * @param elems the elements to add
	 * @return <code>true</code> if successful
	 */
	public boolean addAll(Iterable<? extends E> elems);

	/**
	 * Tests if all elements are contained in this list.
	 * 
	 * @param c the elements to test
	 * @return <code>true</code> if all elements are contained
	 */
	public boolean containsAll(Iterable<?> c);

	/**
	 * Creates an object array with the elements of this list.
	 * 
	 * @return the object array with the elements
	 */
	public Object[] toArray();

	/**
	 * Creates an array of type <code>T</code> with the elements of this list.
	 * 
	 * @return the object array with the elements
	 */
	public <T> T[] toArray(T[] a);

	// Demo 6 Lambdas  ---------------------

	/**
	 * Returns a List consisting of the results of the mapping 
	 * function applied to the elements of this list. 
	 * 
	 * @param <R> The type of result elements
	 * @param mapper The mapping function 
	 * @return The list of the mapped elements 
	 */
	public default <R> List<R> map(Function<? super E, ? extends R> mapper) {
		List<R> result = createResultList();
		for (E e : this) {
			result.add(mapper.apply(e));
		}
		return result;
	}

	/**
	 * Returns a list with the elements of this list
	 * which pass the test of the given predicate function. 
	 * @param predicate the test function
	 * @return the list with the elements passing the test
	 */
	public default List<E> filter(Predicate<? super E> predicate) {
		List<E> result = createResultList();
		for (E e : this) {
			if (predicate.test(e)) {
				result.add(e);
			}
		}
		return result;
	}

	/**
	 * Performs for each element in this list the given action. 
	 * @param action the action function 
	 */
	public default void forEach(Consumer<? super E> action) {
		for (E e : this) {
			action.accept(e);
		}
	}

	/**
	 * Reduces the elements of this list to a result value. 
	 * @param <U> the type of the result value
	 * @param identity the start value for the accumulation 
	 * @param accumulator the function for combining a element with the partial result 
	 * @return result of the reduction 
	 */
	public default <U> U reduce(U identity, BiFunction<U, ? super E, U> accumulator) {
		U result = identity;
		for (E e : this) {
			result = accumulator.apply(result, e);
		}
		return result;
	}

	/**
	 * Reduces the elements of this list to a result value starting from the first 
	 * element of this list. 
	 * The result has the same type as the elements. 
	 * If the list is empty an Opitional.empty() is returned 
	 * @param accumulator the binary reduction operator
	 * @return an Optional value with the result; Optional.empty() for an empty list. 
	 */
	public default Optional<E> reduce(BinaryOperator<E> accumulator) {
		if (this.isEmpty()) {
			return Optional.empty();
		} else {
			return Optional.of(reduce(this.iterator().next(), accumulator));
		}
	}

	/**
	 * Returns the first element satisfying the given predicate 
	 * @param predicate the test predicate 
	 * @return an Optional value with found element; Optional.empty() if not found.
	 */
	public default Optional<E> find(Predicate<? super E> predicate) {
		for (E e : this) {
			if (predicate.test(e)) {
				return Optional.of(e);
			}
		}
		return Optional.empty();
	}
	
	/**
	 * Returns the maximum element of this list according to order as define by 
	 * the given Comparator.  
	 * 
	 * @param comparator the {@code Comparator} for comparing elements
	 * @return an Optional value with the maximum; Optional.empty() for an empty list.
	 */
	public default Optional<E> max(Comparator<? super E> comparator) {
		return reduce((max, e) -> {
			if (comparator.compare(e, max) > 0) {
				return e; 
			} else {
				return max; 
			}
		});
	}
	
	/**
	 * Returns the minimum element of this list according to order as define by 
	 * the given Comparator.  
	 * 
	 * @param comparator the {@code Comparator} for comparing elements
	 * @return an Optional value with the minimum; Optional.empty() for an empty list.
	 */
	public default Optional<E> min(Comparator<? super E> comparator) {
		return reduce((max, e) -> {
			if (comparator.compare(e, max) < 0) {
				return e; 
			} else {
				return max; 
			}
		});
	}
	
	/** 
	 * Tests if all the elements fulfill the given predicate. 
	 * Implements an all quantifier. 
	 * @param predicate the test predicate
	 * @return true if all elements fulfill the given predicate, false otherwise
	 */
	public default boolean all(Predicate<? super E> predicate) {
		return find(predicate.negate()).isEmpty(); 
	}

	/** 
	 * Tests if any element fulfills the given predicate. 
	 * Implements an exist quantifier. 
	 * @param predicate the test predicate
	 * @return true if any element fulfill the given predicate, false otherwise
	 */
	public default boolean any(Predicate<? super E> predicate) {
		return find(predicate).isPresent(); 
	}
	
	/**
	 * Returns a list with n values generated by the given supplier function. 
	 * @param <T> the type of the generated values
	 * @param n the number of value generated
	 * @param supplier the generator function
	 * @return a list of n generated values
	 */
	public static <T> List<T> generate(int n, Supplier<T> supplier) {
		List<T> result = new LinkedList<T>(); 
		for (int i = 0; i < n; i++) {
			result.add(supplier.get());
		}
		return result; 
	}
	
	/**
	 * Returns a list with n values generated by the given operator 
	 * starting from an initial value. 
	 * 
	 * @param <T> the type of the generated values
	 * @param n the number of value generated
	 * @param first the first value
	 * @param next the operator with generates a next value from the current value
	 * @return a list of n generated values
	 */
	public static <T> List<T> iterate(int n, T first, UnaryOperator<T> next) {
		List<T> result = new LinkedList<T>(); 
		T nextElem = first; 
		result.add(first);
		for (int i = 1; i < n; i++) {
			nextElem = next.apply(nextElem); 
			result.add(nextElem);
		}
		return result; 
	}

	/**
	 * Abstract method for creating result lists.
	 * This method should be overridden by the concrete list implementations.  
	 * The concrete type of the result list should be the same 
	 * as the concrete type of this list. 
	 * 
	 * @param <R> the type of elements of the list
	 * @return the concrete list
	 */
	public <R> List<R> createResultList();
}
