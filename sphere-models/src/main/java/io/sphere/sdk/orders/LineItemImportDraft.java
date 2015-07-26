package io.sphere.sdk.orders;

import io.sphere.sdk.carts.ItemState;
import io.sphere.sdk.channels.Channel;
import io.sphere.sdk.models.LocalizedStrings;
import io.sphere.sdk.models.Reference;
import io.sphere.sdk.products.Price;
import io.sphere.sdk.taxcategories.TaxRate;

import javax.annotation.Nullable;
import java.util.Set;

/**
 *
 * @see LineItemImportDraftBuilder
 */
public interface LineItemImportDraft {
    LocalizedStrings getName();

    Price getPrice();

    @Nullable
    String getProductId();

    Long getQuantity();

    @Nullable
    Set<ItemState> getState();

    @Nullable
    Reference<Channel> getSupplyChannel();

    @Nullable
    TaxRate getTaxRate();

    ProductVariantImportDraft getVariant();
}
