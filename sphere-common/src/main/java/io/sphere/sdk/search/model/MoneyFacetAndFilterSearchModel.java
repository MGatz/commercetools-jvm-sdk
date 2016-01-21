package io.sphere.sdk.search.model;

import javax.annotation.Nullable;
import javax.money.CurrencyUnit;

public class MoneyFacetAndFilterSearchModel<T> extends SearchModelImpl<T> {

    MoneyFacetAndFilterSearchModel(@Nullable final SearchModel<T> parent, @Nullable final String pathSegment) {
        super(parent, pathSegment);
    }

    public RangeTermFacetAndFilterSearchModel<T> centAmount() {
        return new MoneyCentAmountSearchModel<>(this, "centAmount").facetedAndFiltered();
    }

    public TermFacetAndFilterSearchModel<T> currency() {
        return new CurrencySearchModel<>(this, "currencyCode").facetedAndFiltered();
    }
}