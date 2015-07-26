package io.sphere.sdk.channels;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.sphere.sdk.models.Base;
import io.sphere.sdk.models.LocalizedStrings;

import javax.annotation.Nullable;
import java.util.Collections;
import java.util.Set;

import static io.sphere.sdk.utils.SetUtils.asSet;

/**
 * Template to create a new Channel.
 *
 * @see ChannelDraftBuilder
 */
public class ChannelDraft extends Base {
    private final String key;
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    @Nullable
    private final Set<ChannelRole> roles;
    @Nullable
    private final LocalizedStrings name;
    @Nullable
    private final LocalizedStrings description;

    ChannelDraft(final String key, final Set<ChannelRole> roles, @Nullable final LocalizedStrings name, @Nullable final LocalizedStrings description) {
        this.key = key;
        this.roles = roles;
        this.name = name;
        this.description = description;
    }

    public static ChannelDraft of(final String key) {
        return new ChannelDraft(key, Collections.emptySet(), null, null);
    }

    public String getKey() {
        return key;
    }

    public Set<ChannelRole> getRoles() {
        return roles;
    }

    @Nullable
    public LocalizedStrings getName() {
        return name;
    }

    @Nullable
    public LocalizedStrings getDescription() {
        return description;
    }
    
    public ChannelDraft withRoles(@Nullable final Set<ChannelRole> roles) {
        return newBuilder().roles(roles).build();
    }

    public ChannelDraft withRoles(final ChannelRole ... roles) {
        return newBuilder().roles(asSet(roles)).build();
    }
    
    public ChannelDraft withName(@Nullable final LocalizedStrings name) {
        return newBuilder().name(name).build();
    }

    public ChannelDraftBuilder newBuilder() {
        return ChannelDraftBuilder.of(this);
    }

    public ChannelDraft withDescription(@Nullable final LocalizedStrings description) {
        return newBuilder().description(description).build();
    }
}
