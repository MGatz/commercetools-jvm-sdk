package io.sphere.sdk.cartdiscounts.commands;

import io.sphere.sdk.cartdiscounts.*;
import io.sphere.sdk.cartdiscounts.commands.updateactions.*;
import io.sphere.sdk.commands.UpdateAction;
import io.sphere.sdk.models.LocalizedStrings;
import io.sphere.sdk.test.IntegrationTest;
import io.sphere.sdk.test.SphereTestUtils;
import io.sphere.sdk.utils.MoneyImpl;
import org.junit.Test;

import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;

import static io.sphere.sdk.cartdiscounts.CartDiscountFixtures.withPersistentCartDiscount;
import static io.sphere.sdk.test.SphereTestUtils.*;
import static java.lang.String.format;
import static org.assertj.core.api.Assertions.assertThat;

public class CartDiscountUpdateCommandTest extends IntegrationTest {
    @Test
    public void changeValue() throws Exception {
        withPersistentCartDiscount(client(), cartDiscount -> {
            final CartDiscountValue newValue = CartDiscountValue.ofAbsolute(MoneyImpl.of(randomInt(), EUR));

            assertThat(cartDiscount.getValue()).isNotEqualTo(newValue);

            final CartDiscount updatedDiscount =
                    execute(CartDiscountUpdateCommand.of(cartDiscount, ChangeValue.of(newValue)));

            assertThat(updatedDiscount.getValue()).isEqualTo(newValue);
        });
    }

    @Test
    public void changeCartPredicate() throws Exception {
        withPersistentCartDiscount(client(), cartDiscount -> {
            final String newPredicate = format("totalPrice > \"%d.00 EUR\"", randomInt());

            assertThat(cartDiscount.getCartPredicate()).isNotEqualTo(newPredicate);

            final CartDiscount updatedDiscount =
                    execute(CartDiscountUpdateCommand.of(cartDiscount, ChangeCartPredicate.of(newPredicate)));

            assertThat(updatedDiscount.getCartPredicate()).isEqualTo(newPredicate);
        });
    }

    @Test
    public void changeTarget() throws Exception {
        withPersistentCartDiscount(client(), cartDiscount -> {
            final CartDiscountTarget newTarget = cartDiscount.getTarget() instanceof LineItemsTarget ?
                    CustomLineItemsTarget.of("1 = 1") : LineItemsTarget.of("1 = 1");

            assertThat(cartDiscount.getTarget()).isNotEqualTo(newTarget);

            final CartDiscount updatedDiscount =
                    execute(CartDiscountUpdateCommand.of(cartDiscount, ChangeTarget.of(newTarget)));

            assertThat(updatedDiscount.getTarget()).isEqualTo(newTarget);
        });
    }

    @Test
    public void changeIsActive() throws Exception {
        withPersistentCartDiscount(client(), cartDiscount -> {
            final boolean newIsActice = !cartDiscount.isActive();

            assertThat(cartDiscount.isActive()).isNotEqualTo(newIsActice);

            final CartDiscount updatedDiscount =
                    execute(CartDiscountUpdateCommand.of(cartDiscount, ChangeIsActive.of(newIsActice)));

            assertThat(updatedDiscount.isActive()).isEqualTo(newIsActice);
        });
    }

    @Test
    public void changeName() throws Exception {
        withPersistentCartDiscount(client(), cartDiscount -> {
            final LocalizedStrings newName = randomSlug();

            assertThat(cartDiscount.getName()).isNotEqualTo(newName);

            final CartDiscount updatedDiscount =
                    execute(CartDiscountUpdateCommand.of(cartDiscount, ChangeName.of(newName)));

            assertThat(updatedDiscount.getName()).isEqualTo(newName);

            //clean up test, cart discount is reused by name
            execute(CartDiscountUpdateCommand.of(updatedDiscount, ChangeName.of(cartDiscount.getName())));
        });
    }

    @Test
    public void setDescription() throws Exception {
        withPersistentCartDiscount(client(), cartDiscount -> {
            final LocalizedStrings newDescription = randomSlug();

            assertThat(cartDiscount.getDescription()).isNotEqualTo(Optional.of(newDescription));

            final CartDiscount updatedDiscount =
                    execute(CartDiscountUpdateCommand.of(cartDiscount, SetDescription.of(newDescription)));

            assertThat(updatedDiscount.getDescription()).isEqualTo(newDescription);
        });
    }

    @Test
    public void changeSortOrder() throws Exception {
        withPersistentCartDiscount(client(), cartDiscount -> {
            final String newSortOrder = format("0.%d", randomInt());

            assertThat(cartDiscount.getSortOrder()).isNotEqualTo(newSortOrder);

            final CartDiscount updatedDiscount =
                    execute(CartDiscountUpdateCommand.of(cartDiscount, ChangeSortOrder.of(newSortOrder)));

            assertThat(updatedDiscount.getSortOrder()).isEqualTo(newSortOrder);
        });
    }

    @Test
    public void changeRequiresDiscountCode() throws Exception {
        withPersistentCartDiscount(client(), cartDiscount -> {
            final boolean newRequiresDiscountCode = !cartDiscount.isRequiringDiscountCode();

            assertThat(cartDiscount.isRequiringDiscountCode()).isNotEqualTo(newRequiresDiscountCode);

            final CartDiscount updatedDiscount =
                    execute(CartDiscountUpdateCommand.of(cartDiscount, ChangeRequiresDiscountCode.of(newRequiresDiscountCode)));

            assertThat(updatedDiscount.isRequiringDiscountCode()).isEqualTo(newRequiresDiscountCode);
        });
    }

    @Test
    public void setValidFrom() throws Exception {
        withPersistentCartDiscount(client(), cartDiscount -> {
            final ZonedDateTime dateTime = SphereTestUtils.now();

            assertThat(cartDiscount.getValidFrom()).isNotEqualTo(dateTime);

            final List<UpdateAction<CartDiscount>> updateActions =
                    asList(SetValidUntil.of(dateTime.plus(7, ChronoUnit.DAYS)), SetValidFrom.of(dateTime));
            final CartDiscount updatedDiscount = execute(CartDiscountUpdateCommand.of(cartDiscount, updateActions));

            assertThat(updatedDiscount.getValidFrom()).isEqualTo(dateTime);
        });
    }
    @Test
    public void setValidUntil() throws Exception {
        withPersistentCartDiscount(client(), cartDiscount -> {
            //until must be after valid from
            final ZonedDateTime dateTime = dateTimeAfterValidFromAndOldValidUntil(cartDiscount);

            assertThat(cartDiscount.getValidUntil()).isNotEqualTo(Optional.of(dateTime));

            final CartDiscount updatedDiscount =
                    execute(CartDiscountUpdateCommand.of(cartDiscount, SetValidUntil.of(dateTime)));

            assertThat(updatedDiscount.getValidUntil()).isEqualTo(dateTime);
        });
    }

    private ZonedDateTime dateTimeAfterValidFromAndOldValidUntil(final CartDiscount cartDiscount) {
        return Optional.ofNullable(cartDiscount.getValidUntil())
                .orElse(Optional.ofNullable(cartDiscount.getValidFrom()).orElse(SphereTestUtils.now()).plusSeconds(1000)).plusSeconds(1);
    }
}