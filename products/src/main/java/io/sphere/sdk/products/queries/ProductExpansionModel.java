package io.sphere.sdk.products.queries;

import io.sphere.sdk.products.Product;
import io.sphere.sdk.queries.ExpansionModel;
import io.sphere.sdk.queries.ExpansionPath;

public class ProductExpansionModel extends ExpansionModel {
    ProductExpansionModel() {
    }

    public ExpansionPath<Product> productType() {
        return newSubPath("productType");
    }

    public ExpansionPath<Product> taxCategory() {
        return newSubPath("taxCategory");
    }

    private ProductExpansionPath newSubPath(final String s) {
        return new ProductExpansionPath(path, s);
    }
}
