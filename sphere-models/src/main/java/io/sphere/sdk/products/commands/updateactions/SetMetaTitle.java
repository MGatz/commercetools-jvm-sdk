package io.sphere.sdk.products.commands.updateactions;

import io.sphere.sdk.models.LocalizedStrings;
import io.sphere.sdk.products.ProductUpdateScope;

import javax.annotation.Nullable;
import java.util.Optional;

/**
 * Sets the SEO attribute title.
 *
 * {@include.example io.sphere.sdk.products.commands.ProductUpdateCommandTest#setMetaTitle()}
 *
 * <p>Create update actions to set the SEO attributes title, description and keywords altogether:</p>
 * {@include.example io.sphere.sdk.products.commands.ProductUpdateCommandTest#setMetaAttributes()}
 */
public class SetMetaTitle extends StageableProductUpdateAction {
    @Nullable
    private final LocalizedStrings metaTitle;

    private SetMetaTitle(@Nullable final LocalizedStrings metaTitle, final ProductUpdateScope productUpdateScope) {
        super("setMetaTitle", productUpdateScope);
        this.metaTitle = metaTitle;
    }

    public static SetMetaTitle of(@Nullable final LocalizedStrings metaTitle, final ProductUpdateScope productUpdateScope) {
        return new SetMetaTitle(metaTitle, productUpdateScope);
    }

    @Nullable
    public LocalizedStrings getMetaTitle() {
        return metaTitle;
    }
}
