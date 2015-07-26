package io.sphere.sdk.products.commands.updateactions;

import io.sphere.sdk.models.Reference;
import io.sphere.sdk.models.Referenceable;
import io.sphere.sdk.products.Product;
import io.sphere.sdk.commands.UpdateAction;
import io.sphere.sdk.taxcategories.TaxCategory;

import javax.annotation.Nullable;
import java.util.Optional;

/**
 * Adds, changes or removes a product's tax category. This change can never be staged and is thus immediately visible in published products.
 *
 * {@include.example io.sphere.sdk.products.commands.ProductUpdateCommandTest#setTaxCategory()}
 *
 */
public class SetTaxCategory extends UpdateAction<Product> {
    @Nullable
    private final Reference<TaxCategory> taxCategory;

    private SetTaxCategory(final Reference<TaxCategory> taxCategory) {
        super("setTaxCategory");
        this.taxCategory = taxCategory;
    }


    public static SetTaxCategory of(@Nullable final Referenceable<TaxCategory> taxCategory) {
        return new SetTaxCategory(Optional.ofNullable(taxCategory).map(t -> t.toReference()).orElse(null));
    }

    public static SetTaxCategory unset() {
        return of(null);
    }

    @Nullable
    public Reference<TaxCategory> getTaxCategory() {
        return taxCategory;
    }

    public static SetTaxCategory to(final Referenceable<TaxCategory> taxCategory) {
        return of(taxCategory);
    }
}
