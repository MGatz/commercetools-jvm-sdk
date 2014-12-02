package example;

import io.sphere.sdk.client.JavaClient;
import io.sphere.sdk.queries.PagedQueryResult;
import io.sphere.sdk.taxcategories.TaxCategory;
import io.sphere.sdk.taxcategories.queries.TaxCategoryQuery;

import java.util.concurrent.CompletableFuture;

public class TaxCategoryQueryExample {

    JavaClient client;

    public void exampleQuery() {
        CompletableFuture<PagedQueryResult<TaxCategory>> promise = client.execute(TaxCategoryQuery.of().byName("de19"));
    }
}
