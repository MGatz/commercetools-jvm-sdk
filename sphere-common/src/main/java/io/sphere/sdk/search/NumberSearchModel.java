package io.sphere.sdk.search;

import javax.annotation.Nullable;
import java.math.BigDecimal;

public class NumberSearchModel<T, S extends SearchSortDirection> extends SearchModelImpl<T> implements RangeTermModel<T, BigDecimal>, SearchSortingModel<T, S> {

    public NumberSearchModel(@Nullable final SearchModel<T> parent, final String pathSegment) {
        super(parent, pathSegment);
    }

    @Override
    public RangedFilterSearchModel<T, BigDecimal> filterBy() {
        return new RangedFilterSearchModel<>(this, null, TypeSerializer.ofNumber());
    }

    @Override
    public RangedFacetSearchModel<T, BigDecimal> facetOf() {
        return new RangedFacetSearchModel<>(this, null, TypeSerializer.ofNumber());
    }

    @Override
    public SearchSort<T> sort(S sortDirection) {
        return new SphereSearchSort<>(this, sortDirection);
    }
}
