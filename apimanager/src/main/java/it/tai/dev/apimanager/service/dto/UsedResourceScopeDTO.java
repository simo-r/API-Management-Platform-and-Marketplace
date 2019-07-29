package it.tai.dev.apimanager.service.dto;

import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link it.tai.dev.apimanager.domain.UsedResourceScope} entity.
 */
public class UsedResourceScopeDTO implements Serializable {

    private Long id;


    private Long resourceId;

    private Long scopeId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getResourceId() {
        return resourceId;
    }

    public void setResourceId(Long resourceId) {
        this.resourceId = resourceId;
    }

    public Long getScopeId() {
        return scopeId;
    }

    public void setScopeId(Long resourceScopeId) {
        this.scopeId = resourceScopeId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        UsedResourceScopeDTO usedResourceScopeDTO = (UsedResourceScopeDTO) o;
        if (usedResourceScopeDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), usedResourceScopeDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "UsedResourceScopeDTO{" +
            "id=" + getId() +
            ", resource=" + getResourceId() +
            ", scope=" + getScopeId() +
            "}";
    }
}
