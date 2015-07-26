package io.sphere.sdk.carts.expansion;

import io.sphere.sdk.expansion.ExpansionModel;
import io.sphere.sdk.expansion.ExpansionPath;

public abstract class CartLikeExpansionModel<T> extends ExpansionModel<T> {
    protected CartLikeExpansionModel(final String parentPath, final String path) {
        super(parentPath, path);
    }

    protected CartLikeExpansionModel() {
        super();
    }

    public ExpansionPath<T> customerGroup() {
        return expansionPath("customerGroup");
    }

    public DiscountCodeInfoExpansionModel<T> discountCodes() {
        return discountCodes("*");
    }

    public DiscountCodeInfoExpansionModel<T> discountCodes(final int index) {
        return discountCodes("" + index);
    }

    public LineItemExpansionModel<T> lineItems() {
        return new LineItemExpansionModel<>(pathExpression(), "lineItems[*]");
    }

    public LineItemExpansionModel<T> lineItems(final int index) {
        return new LineItemExpansionModel<>(pathExpression(), "lineItems[" + index + "]");
    }

    private DiscountCodeInfoExpansionModel<T> discountCodes(final String s) {
        return new DiscountCodeInfoExpansionModel<>(pathExpression(), "discountCodes[" + s + "]");
    }
}
