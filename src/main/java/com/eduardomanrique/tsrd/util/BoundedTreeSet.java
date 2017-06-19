package com.eduardomanrique.tsrd.util;

import java.util.Collection;
import java.util.Comparator;
import java.util.SortedSet;
import java.util.TreeSet;

/**
 * Created by emanrique on 19/06/17.
 */
public class BoundedTreeSet<E> extends TreeSet<E> {

    private final int limit;

    public BoundedTreeSet(final int limit, final Comparator<? super E> comparator) {
        super(comparator);
        this.limit = limit;
    }

    @Override
    public boolean add(final E e) {
        boolean result = super.add(e);
        resize();
        return result;
    }

    @Override
    public boolean addAll(Collection<? extends E> c) {
        boolean result = super.addAll(c);
        resize();
        return result;
    }

    private void resize() {
        if (this.size() > this.limit) {
            this.pollFirst();
        }
    }
}