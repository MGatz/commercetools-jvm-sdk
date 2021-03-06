package io.sphere.sdk.categories;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import io.sphere.sdk.models.*;
import io.sphere.sdk.products.ProductData;
import io.sphere.sdk.products.ProductProjection;
import io.sphere.sdk.types.Custom;
import io.sphere.sdk.types.CustomFields;
import io.sphere.sdk.types.TypeDraft;
import org.apache.commons.lang3.builder.ToStringBuilder;

import javax.annotation.Nullable;
import java.util.List;

import static io.sphere.sdk.utils.SphereInternalUtils.join;

/**
 * Categories are used to organize products in a hierarchical structure.
 *
 *  <p>A category can have {@link io.sphere.sdk.types.Custom custom fields}.</p>
 *
 * <p>Consult the documentation for <a href="{@docRoot}/io/sphere/sdk/meta/CategoryDocumentation.html">categories</a> for more information.</p>
 * @see io.sphere.sdk.categories.commands.CategoryCreateCommand
 * @see io.sphere.sdk.categories.commands.CategoryUpdateCommand
 * @see io.sphere.sdk.categories.commands.CategoryDeleteCommand
 * @see io.sphere.sdk.categories.queries.CategoryQuery
 * @see io.sphere.sdk.categories.queries.CategoryByIdGet
 * @see CategoryTree
 * @see ProductData#getCategories()
 * @see ProductData#getCategoryOrderHints()
 * @see ProductProjection#getCategories()
 * @see ProductProjection#getCategoryOrderHints()
 * @see io.sphere.sdk.products.commands.updateactions.AddToCategory
 * @see io.sphere.sdk.products.commands.updateactions.RemoveFromCategory
 */
@JsonDeserialize(as=CategoryImpl.class)
public interface Category extends Resource<Category>, WithLocalizedSlug, MetaAttributes, Custom {

    /**
     * Name of this category.
     *
     * @see io.sphere.sdk.categories.commands.updateactions.ChangeName
     *
     * @return name
     */
    LocalizedString getName();

    /**
     * Human-readable identifier usually used as deep-link URL part.
     *
     * @see io.sphere.sdk.categories.commands.updateactions.ChangeSlug
     *
     * @return slug
     */
    LocalizedString getSlug();

    /**
     * Description for this category.
     *
     * @see io.sphere.sdk.categories.commands.updateactions.SetDescription
     *
     * @return description or null
     */
    @Nullable
    LocalizedString getDescription();

    List<Reference<Category>> getAncestors();

    /**
     * Reference to the parent category.
     *
     * @see io.sphere.sdk.categories.commands.updateactions.ChangeParent
     *
     * @return parent reference or null
     */
    @Nullable
    Reference<Category> getParent();

    /**
     * An attribute as base for a custom category order in one level.
     *
     * @see io.sphere.sdk.categories.commands.updateactions.ChangeOrderHint
     *
     * @return order hint or null
     */
    @Nullable
    String getOrderHint();

    /**
     * ID which can be used as additional identifier for external Systems like CRM or ERP.
     *
     * @see io.sphere.sdk.categories.commands.updateactions.SetExternalId
     *
     * @return external ID or null
     */
    @Nullable
    String getExternalId();

    /**
     * SEO meta title.
     *
     * @see io.sphere.sdk.categories.commands.updateactions.SetMetaTitle
     *
     * @return SEO meta title.
     */
    @Nullable
    @Override
    LocalizedString getMetaTitle();

    /**
     * SEO meta description.
     *
     * @see io.sphere.sdk.categories.commands.updateactions.SetMetaDescription
     *
     * @return SEO meta description.
     */
    @Nullable
    @Override
    LocalizedString getMetaDescription();

    /**
     * SEO meta keywords.
     *
     * @see io.sphere.sdk.categories.commands.updateactions.SetMetaKeywords
     *
     * @return SEO meta keywords.
     */
    @Nullable
    @Override
    LocalizedString getMetaKeywords();

    /**
     * Custom fields.
     *
     * @see Custom
     * @see io.sphere.sdk.categories.commands.updateactions.SetCustomField
     * @see io.sphere.sdk.categories.commands.updateactions.SetCustomType
     *
     * @return custom fields
     */
    @Nullable
    CustomFields getCustom();

    @Override
    default Reference<Category> toReference() {
        return Reference.of(referenceTypeId(), getId(), this);
    }

    /**
     * An identifier for this resource which supports {@link CustomFields}.
     * @see TypeDraft#getResourceTypeIds()
     * @see io.sphere.sdk.types.Custom
     * @return ID of this resource type
     */
    static String resourceTypeId() {
        return "category";
    }

    /**
     * A type hint for references which resource type is linked in a reference.
     * @see Reference#getTypeId()
     * @return type hint
     */
    static String referenceTypeId() {
        return "category";
    }

    static Reference<Category> reference(final String id) {
        return Reference.of(referenceTypeId(), id);
    }

    /**
     * Creates a container which contains the full Java type information to deserialize this class from JSON.
     *
     * @see io.sphere.sdk.json.SphereJsonUtils#readObject(byte[], TypeReference)
     * @see io.sphere.sdk.json.SphereJsonUtils#readObject(String, TypeReference)
     * @see io.sphere.sdk.json.SphereJsonUtils#readObject(com.fasterxml.jackson.databind.JsonNode, TypeReference)
     * @see io.sphere.sdk.json.SphereJsonUtils#readObjectFromResource(String, TypeReference)
     *
     * @return type reference
     */
    static TypeReference<Category> typeReference() {
        return new TypeReference<Category>() {
            @Override
            public String toString() {
                return "TypeReference<Category>";
            }
        };
    }

    static String toString(final Category category) {
        final List<Reference<Category>> ancestors = category.getAncestors();
        return new ToStringBuilder(category, SdkDefaults.TO_STRING_STYLE)
                .append("id", category.getId())
                .append("version", category.getVersion())
                .append("createdAt", category.getCreatedAt())
                .append("lastModifiedAt", category.getLastModifiedAt())
                .append("name", category.getName())
                .append("slug", category.getSlug())
                .append("description", category.getDescription())
                .append("ancestors", ancestors == null ? null : join(ancestors))
                .append("parent", category.getParent())
                .append("orderHint", category.getOrderHint())
                .toString();
    }

    /**
     * Creates a reference for one item of this class by a known ID.
     *
     * <p>An example for categories but this applies for other resources, too:</p>
     * {@include.example io.sphere.sdk.categories.CategoryTest#referenceOfId()}
     *
     * <p>If you already have a resource object, then use {@link #toReference()} instead:</p>
     *
     * {@include.example io.sphere.sdk.categories.CategoryTest#toReference()}
     *
     * @param id the ID of the resource which should be referenced.
     * @return reference
     */
    static Reference<Category> referenceOfId(final String id) {
        return Reference.of(referenceTypeId(), id);
    }
}
