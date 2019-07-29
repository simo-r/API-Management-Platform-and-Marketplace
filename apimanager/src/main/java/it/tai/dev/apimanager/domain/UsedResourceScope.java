package it.tai.dev.apimanager.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * A UsedResourceScope.
 */
@Entity
@Table(name = "used_resource_scope")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class UsedResourceScope implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties("usedBies")
    private Resource resource;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties("usedBies")
    private ResourceScope scope;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Resource getResource() {
        return resource;
    }

    public UsedResourceScope resource(Resource resource) {
        this.resource = resource;
        return this;
    }

    public void setResource(Resource resource) {
        this.resource = resource;
    }

    public ResourceScope getScope() {
        return scope;
    }

    public UsedResourceScope scope(ResourceScope resourceScope) {
        this.scope = resourceScope;
        return this;
    }

    public void setScope(ResourceScope resourceScope) {
        this.scope = resourceScope;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof UsedResourceScope)) {
            return false;
        }
        return id != null && id.equals(((UsedResourceScope) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "UsedResourceScope{" +
            "id=" + getId() +
            "}";
    }
}
