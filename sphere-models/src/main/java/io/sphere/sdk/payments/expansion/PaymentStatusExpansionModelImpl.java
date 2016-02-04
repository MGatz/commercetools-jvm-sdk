package io.sphere.sdk.payments.expansion;

import io.sphere.sdk.expansion.ExpansionModel;
import io.sphere.sdk.states.expansion.StateExpansionModel;

import javax.annotation.Nullable;
import java.util.List;

final class PaymentStatusExpansionModelImpl<T> extends ExpansionModel<T> implements PaymentStatusExpansionModel<T> {
    PaymentStatusExpansionModelImpl(final List<String> parentPath, @Nullable final String path) {
        super(parentPath, path);
    }

    @Override
    public StateExpansionModel<T> state() {
        return StateExpansionModel.of(buildPathExpression(), "state");
    }
}
