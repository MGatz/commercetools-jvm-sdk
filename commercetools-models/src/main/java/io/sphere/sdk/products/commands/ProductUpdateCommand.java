package io.sphere.sdk.products.commands;

import io.sphere.sdk.commands.UpdateAction;
import io.sphere.sdk.commands.UpdateCommandDsl;
import io.sphere.sdk.expansion.MetaModelReferenceExpansionDsl;
import io.sphere.sdk.models.Versioned;
import io.sphere.sdk.products.Product;
import io.sphere.sdk.products.expansion.ProductExpansionModel;

import java.util.Collections;
import java.util.List;

/**

 {@doc.gen list actions}

 */
public interface ProductUpdateCommand extends UpdateCommandDsl<Product, ProductUpdateCommand>, MetaModelReferenceExpansionDsl<Product, ProductUpdateCommand, ProductExpansionModel<Product>> {
    static ProductUpdateCommand of(final Versioned<Product> versioned, final UpdateAction<Product> updateAction) {
        return of(versioned, Collections.singletonList(updateAction));
    }

    static ProductUpdateCommand of(final Versioned<Product> versioned, final List<? extends UpdateAction<Product>> updateActions) {
        return new ProductUpdateCommandImpl(versioned, updateActions);
    }
}
