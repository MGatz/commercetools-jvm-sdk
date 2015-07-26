package io.sphere.sdk.shippingmethods.expansion;

import io.sphere.sdk.expansion.ExpansionModel;
import io.sphere.sdk.expansion.ExpansionPath;

public class ZoneRateExpansionModel<T> extends ExpansionModel<T> {
    ZoneRateExpansionModel(final String parentPath, final String path) {
        super(parentPath, path);
    }

    ZoneRateExpansionModel() {
        super();
    }

    public ExpansionPath<T> zone() {
        return expansionPath("zone");
    }
}
