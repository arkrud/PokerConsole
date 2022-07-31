package com.arkrud.pokerconsole.Util;

import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

// TODO: Auto-generated Javadoc
/**
 * The Class Reversed.
 *
 * @param <T> the generic type
 */
public class Reversed<T> implements Iterable<T> {


	/** The original. */
	private final List<T> original;

	/**
	 * Instantiates a new reversed.
	 *
	 * @param original the original
	 */
	public Reversed(List<T> original) {
		this.original = original;
	}

	/* (non-Javadoc)
	 * @see java.lang.Iterable#iterator()
	 */
	@Override
	public Iterator<T> iterator() {
		final ListIterator<T> i = original.listIterator(original.size());
		return new Iterator<T>() {
			@Override
			public boolean hasNext() {
				return i.hasPrevious();
			}

			@Override
			public T next() {
				return i.previous();
			}

			@Override
			public void remove() {
				i.remove();
			}
		};
	}

}
