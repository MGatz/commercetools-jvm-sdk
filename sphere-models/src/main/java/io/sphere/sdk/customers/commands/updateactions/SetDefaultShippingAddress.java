package io.sphere.sdk.customers.commands.updateactions;

import io.sphere.sdk.commands.UpdateAction;
import io.sphere.sdk.customers.Customer;
import io.sphere.sdk.models.Address;

import javax.annotation.Nullable;

import static java.lang.String.format;

/**
 * Sets the default shipping address from the customer's addresses.
 *
 * {@include.example io.sphere.sdk.customers.commands.CustomerUpdateCommandTest#setDefaultShippingAddress()}
 */
public class SetDefaultShippingAddress extends UpdateAction<Customer> {
    @Nullable
    private final String addressId;

    private SetDefaultShippingAddress(final String addressId) {
        super("setDefaultShippingAddress");
        this.addressId = addressId;
    }

    public static SetDefaultShippingAddress of(final String addressId) {
        return new SetDefaultShippingAddress(addressId);
    }

    public static SetDefaultShippingAddress of(@Nullable final Address address) {
        if (address.getId() == null) {
            throw new IllegalArgumentException(format("The address %s should have an id.", address));
        }
        return of(address.getId());
    }

    @Nullable
    public String getAddressId() {
        return addressId;
    }
}
