package io.sphere.sdk.products;

import io.sphere.sdk.client.TestClient;
import io.sphere.sdk.products.commands.ProductCreateCommand;
import io.sphere.sdk.products.commands.ProductDeleteByIdCommand;
import io.sphere.sdk.products.commands.ProductUpdateCommand;
import io.sphere.sdk.products.commands.updateactions.Unpublish;
import io.sphere.sdk.products.queries.FetchProductById;
import io.sphere.sdk.products.queries.ProductQuery;
import io.sphere.sdk.producttypes.ProductType;
import io.sphere.sdk.producttypes.ProductTypeFixtures;
import io.sphere.sdk.queries.PagedQueryResult;
import io.sphere.sdk.suppliers.SimpleCottonTShirtNewProductSupplier;
import io.sphere.sdk.suppliers.TShirtNewProductTypeSupplier;
import io.sphere.sdk.utils.SphereInternalLogger;

import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Supplier;

import static io.sphere.sdk.test.SphereTestUtils.*;
import static java.util.Arrays.asList;

public class ProductFixtures {
    public static final SphereInternalLogger PRODUCT_FIXTURES_LOGGER = SphereInternalLogger.getLogger("products.fixtures");

    public static void withProduct(final TestClient client, final Consumer<Product> user) {
        withProduct(client, randomString(), user);
    }

    public static void withProduct(final TestClient client, final String testName, final Consumer<Product> user) {
        withProductType(client, ProductReferenceExpansionTest.class.getName() + "." + testName, productType -> {
            withProduct(client, new SimpleCottonTShirtNewProductSupplier(productType, "foo" + testName), user);
        });
    }

    public static void withProduct(final TestClient client, final Supplier<NewProduct> creator, final Consumer<Product> user) {
        final NewProduct newProduct = creator.get();
        final String slug = englishSlugOf(newProduct);
        final PagedQueryResult<Product> pagedQueryResult = client.execute(new ProductQuery().bySlug(ProductProjectionType.CURRENT, Locale.ENGLISH, slug));
        delete(client, pagedQueryResult.getResults());
        final Product product = client.execute(new ProductCreateCommand(newProduct));
        PRODUCT_FIXTURES_LOGGER.debug(() -> "created product " + englishSlugOf(product.getMasterData().getCurrent().get()) + " " + product.getId() + " of product type " + product.getProductType().getId());
        user.accept(product);
        delete(client, product);
    }

    public static void withProductType(final TestClient client, final String productTypeName, final Consumer<ProductType> user) {
        ProductTypeFixtures.withProductType(client, new TShirtNewProductTypeSupplier(productTypeName), user);
    }

    public static void delete(final TestClient client, final List<Product> products) {
        products.forEach(product -> delete(client, product));
    }

    public static void delete(final TestClient client, final Product product) {
        final Optional<Product> freshLoadedProduct = client.execute(new FetchProductById(product));
        freshLoadedProduct.ifPresent(loadedProduct -> {
            final boolean isPublished = loadedProduct.getMasterData().isPublished();
            PRODUCT_FIXTURES_LOGGER.debug(() -> "product is published " + isPublished);
            final Product unPublishedProduct;
            if (isPublished) {
                unPublishedProduct = client.execute(new ProductUpdateCommand(loadedProduct, asList(Unpublish.of())));
            } else {
                unPublishedProduct = loadedProduct;
            }
            PRODUCT_FIXTURES_LOGGER.debug(() -> "attempt to delete product " + englishSlugOf(product.getMasterData().getCurrent().get()) + " " + product.getId());
            client.execute(new ProductDeleteByIdCommand(unPublishedProduct));
            PRODUCT_FIXTURES_LOGGER.debug(() -> "deleted product " + englishSlugOf(product.getMasterData().getCurrent().get()) + " " + product.getId());
        });
    }

}