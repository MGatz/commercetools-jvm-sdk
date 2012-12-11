package de.commercetools.internal.util;

import com.google.common.collect.FluentIterable;
import com.google.common.collect.ImmutableList;
import de.commercetools.internal.util.SearchUtil;

public class ListUtil {
    /** Helper for vararg methods with at least one argument. */
    public static <T> ImmutableList<T> list(T t, T... ts) {
        ImmutableList.Builder<T> builder = ImmutableList.builder();
        if (t != null) builder = builder.add(t);
        for (T elem: ts) {
            if (elem != null) builder.add(elem);
        }
        return builder.build();
    }

    /** Creates an copy immutable copy of given collection with given element prepended. */
    public static <T> ImmutableList<T> list(T t, Iterable<T> elems) {
        ImmutableList.Builder<T> builder = ImmutableList.builder();
        if (t != null) builder = builder.add(t);
        for (T e: elems) {
            if (e != null) builder.add(e);
        }
        return builder.build();
    }

    /** Creates an copy immutable copy of given collection with given element prepended. */
    public static <T> ImmutableList<T> list(Iterable<T> elems1, Iterable<T> elems2) {
        ImmutableList.Builder<T> builder = ImmutableList.builder();
        for (T e: elems1) {
            if (e != null) builder.add(e);
        }
        for (T e: elems2) {
            if (e != null) builder.add(e);
        }
        return builder.build();
    }

    /** Converts a Collection to a List. */
    public static <T> ImmutableList<T> toList(Iterable<T> elems) {
        if (elems == null) {
            return ImmutableList.<T>of();
        }
        if (elems instanceof ImmutableList) {
            return (ImmutableList<T>)elems;
        }
        return ImmutableList.copyOf(FluentIterable.from(elems).filter(SearchUtil.isNotNull));
    }
}
