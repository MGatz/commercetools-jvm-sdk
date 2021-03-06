package io.sphere.sdk.customers.commands;

import com.neovisionaries.i18n.CountryCode;
import io.sphere.sdk.carts.Cart;
import io.sphere.sdk.carts.CartFixtures;
import io.sphere.sdk.carts.CartState;
import io.sphere.sdk.carts.LineItem;
import io.sphere.sdk.carts.commands.CartUpdateCommand;
import io.sphere.sdk.carts.commands.updateactions.AddLineItem;
import io.sphere.sdk.carts.commands.updateactions.SetCountry;
import io.sphere.sdk.carts.queries.CartByIdGet;
import io.sphere.sdk.client.BlockingSphereClient;
import io.sphere.sdk.client.ErrorResponseException;
import io.sphere.sdk.customers.Customer;
import io.sphere.sdk.customers.CustomerFixtures;
import io.sphere.sdk.customers.CustomerIntegrationTest;
import io.sphere.sdk.customers.CustomerSignInResult;
import io.sphere.sdk.customers.errors.CustomerInvalidCredentials;
import io.sphere.sdk.products.ProductFixtures;
import org.junit.Test;

import java.util.function.Consumer;
import java.util.function.Function;

import static io.sphere.sdk.customers.CustomerFixtures.PASSWORD;
import static io.sphere.sdk.customers.CustomerFixtures.withCustomer;
import static java.util.Arrays.asList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class CustomerSignInCommandIntegrationTest extends CustomerIntegrationTest {
    @Test
    public void execution() throws Exception {
        withCustomer(client(), customer -> {
            final CustomerSignInResult result = client().executeBlocking(CustomerSignInCommand.of(customer.getEmail(), PASSWORD));
            assertThat(result.getCustomer()).isEqualTo(customer);
        });
    }

    @Test
    public void signInWithAnonymousCart() throws Exception {
        withCustomerCustomerCartAndAnonymousCart(client(), customer -> customersCart -> anonymousCart -> {
            assertThat(customersCart.getCustomerId())
                    .as("customersCart belongs to customer")
                    .isEqualTo(customer.getId());
            assertThat(anonymousCart.getCustomerId()).as("anonymous cart has no customer").isNull();
            assertThat(customersCart.getLineItems().get(0).getProductId())
                    .as("both carts have the same product variant")
                    .isEqualTo(anonymousCart.getLineItems().get(0).getProductId());
            assertThat(customersCart.getLineItems().get(0).getQuantity())
                    .describedAs("both carts have the same variant in different quantities")
                    .isNotEqualTo(anonymousCart.getLineItems().get(0).getQuantity());
            assertThat(customersCart.getLineItems().get(0).getQuantity()).isEqualTo(3);
            assertThat(anonymousCart.getLineItems().get(0).getQuantity()).isEqualTo(7);
            final long maxOfBothQuantities = Math.max(customersCart.getLineItems().get(0).getQuantity(),
                    anonymousCart.getLineItems().get(0).getQuantity());
            assertThat(maxOfBothQuantities).isEqualTo(7);

            final CustomerSignInResult result = client().executeBlocking(CustomerSignInCommand
                    .of(customer.getEmail(), PASSWORD, anonymousCart.getId()));

            final Cart mergeResultCart = result.getCart();
            final LineItem mergeResultLineItem = mergeResultCart.getLineItems().get(0);
            assertThat(mergeResultLineItem.getQuantity()).isEqualTo(maxOfBothQuantities).isEqualTo(7);
            assertThat(mergeResultLineItem.getProductId())
                    .describedAs("same product variant as before")
                    .isEqualTo(customersCart.getLineItems().get(0).getProductId());
            assertThat(mergeResultCart.getId()).isEqualTo(customersCart.getId());
            assertThat(mergeResultCart.getId()).isEqualTo(customersCart.getId());
            assertThat(mergeResultCart.getCartState()).isEqualTo(CartState.ACTIVE);
            final Cart abandonedCart = client().executeBlocking(CartByIdGet.of(anonymousCart.getId()));
            assertThat(abandonedCart.getCartState()).isEqualTo(CartState.MERGED);
        });
    }

    private void withCustomerCustomerCartAndAnonymousCart(final BlockingSphereClient client, final Function<Customer, Function<Cart, Consumer<Cart>>> curriedFunctions) {
        CustomerFixtures.withCustomerAndCart(client(), ((customer, cart) -> {
            ProductFixtures.withTaxedProduct(client(), product -> {
                final Cart customerCartWithProduct = client.executeBlocking(CartUpdateCommand.of(cart, asList(SetCountry.of(CountryCode.DE), AddLineItem.of(product, 1, 3L))));
                final Cart anonymousCart = CartFixtures.createCartWithCountry(client());
                final Cart anonymousCartWithProduct = client.executeBlocking(CartUpdateCommand.of(anonymousCart, asList(SetCountry.of(CountryCode.DE), AddLineItem.of(product, 1, 7L))));
                curriedFunctions.apply(customer).apply(customerCartWithProduct).accept(anonymousCartWithProduct);
            });
        }));
    }

    @Test
    public void executionWithInvalidEmail() throws Exception {
        withCustomer(client(), customer -> {
            assertThatThrownBy(() -> client().executeBlocking(CustomerSignInCommand.of("notpresent@null.sphere.io", PASSWORD)))
                    .isInstanceOf(ErrorResponseException.class)
                    .matches(e -> ((ErrorResponseException) e).hasErrorCode(CustomerInvalidCredentials.CODE));
        });
    }
}