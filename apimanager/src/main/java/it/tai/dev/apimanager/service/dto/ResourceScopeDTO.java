package it.tai.dev.apimanager.service.dto;

import it.tai.dev.apimanager.domain.enumeration.AuthType;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Objects;
import java.util.Set;

/**
 * A DTO for the {@link it.tai.dev.apimanager.domain.ResourceScope} entity.
 */
public class ResourceScopeDTO implements Serializable {

    private Long id;

    @NotNull
    private String name;

    @NotNull
    private Integer authLevel;

    @NotNull
    private AuthType authType;

    @NotNull
    private Long resourceId;

    private Set<String> attributeScopes;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getAuthLevel() {
        return authLevel;
    }

    public void setAuthLevel(Integer authLevel) {
        this.authLevel = authLevel;
    }

    public AuthType getAuthType() {
        return authType;
    }

    public void setAuthType(AuthType authType) {
        this.authType = authType;
    }

    public Long getResourceId() {
        return resourceId;
    }

    public void setResourceId(Long resourceId) {
        this.resourceId = resourceId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        ResourceScopeDTO resourceScopeDTO = (ResourceScopeDTO) o;
        if (resourceScopeDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), resourceScopeDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }


    @Override
    public String toString() {
        return "ResourceScopeDTO{" +
            "id=" + id +
            ", name='" + name + '\'' +
            ", authLevel=" + authLevel +
            ", authType=" + authType +
            ", resourceId=" + resourceId +
            ", attributeScopes=" + attributeScopes +
            '}';
    }

    public Set<String> getAttributeScopes() {
        return attributeScopes;
    }

    public void setAttributeScopes(Set<String> attributeScopes) {
        this.attributeScopes = attributeScopes;
    }
}
