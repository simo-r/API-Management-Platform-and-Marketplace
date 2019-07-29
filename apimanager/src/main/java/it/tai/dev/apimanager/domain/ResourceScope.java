package it.tai.dev.apimanager.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import it.tai.dev.apimanager.domain.enumeration.AuthType;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 * A ResourceScope.
 */
@Entity
@Table(name = "resource_scope")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class ResourceScope implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "name", nullable = false)
    private String name;

    @NotNull
    @Column(name = "auth_level", nullable = false)
    private Integer authLevel;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "auth_type", nullable = false)
    private AuthType authType;

    @OneToMany(mappedBy = "scope"/*,cascade = CascadeType.ALL*/,orphanRemoval = true)
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<UsedResourceScope> usedBies = new HashSet<>();

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties("scopes")
    private Resource resource;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public ResourceScope name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getAuthLevel() {
        return authLevel;
    }

    public ResourceScope authLevel(Integer authLevel) {
        this.authLevel = authLevel;
        return this;
    }

    public void setAuthLevel(Integer authLevel) {
        this.authLevel = authLevel;
    }

    public AuthType getAuthType() {
        return authType;
    }

    public ResourceScope authType(AuthType authType) {
        this.authType = authType;
        return this;
    }

    public void setAuthType(AuthType authType) {
        this.authType = authType;
    }

    public Set<UsedResourceScope> getUsedBies() {
        return usedBies;
    }

    public ResourceScope usedBies(Set<UsedResourceScope> usedResourceScopes) {
        this.usedBies = usedResourceScopes;
        return this;
    }

    public ResourceScope addUsedBy(UsedResourceScope usedResourceScope) {
        this.usedBies.add(usedResourceScope);
        usedResourceScope.setScope(this);
        return this;
    }

    public ResourceScope removeUsedBy(UsedResourceScope usedResourceScope) {
        this.usedBies.remove(usedResourceScope);
        usedResourceScope.setScope(null);
        return this;
    }

    public void setUsedBies(Set<UsedResourceScope> usedResourceScopes) {
        this.usedBies = usedResourceScopes;
    }

    public Resource getResource() {
        return resource;
    }

    public ResourceScope resource(Resource resource) {
        this.resource = resource;
        return this;
    }

    public void setResource(Resource resource) {
        this.resource = resource;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ResourceScope)) {
            return false;
        }
        return id != null && id.equals(((ResourceScope) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "ResourceScope{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", authLevel=" + getAuthLevel() +
            ", authType='" + getAuthType() + "'" +
            "}";
    }
}
