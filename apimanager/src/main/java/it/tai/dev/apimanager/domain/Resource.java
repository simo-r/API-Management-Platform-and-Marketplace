package it.tai.dev.apimanager.domain;

import it.tai.dev.apimanager.domain.enumeration.ResourceType;
import it.tai.dev.apimanager.domain.enumeration.Status;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 * A Resource.
 */
@Entity
@Table(name = "resource")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Resource implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "owner", nullable = false)
    private String owner;

    @NotNull
    @Column(name = "approver", nullable = false)
    private String approver;

    @NotNull
    @Column(name = "description", nullable = false)
    private String description;

    @NotNull
    @Column(name = "redirect_url", nullable = false)
    private String redirectUrl;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private Status status;

    @NotNull
    @Column(name = "client_id", nullable = false, unique = true)
    private String clientId;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "type", nullable = false)
    private ResourceType type;

    @Lob
    @Column(name = "image")
    private byte[] image;

    @Column(name = "image_content_type")
    private String imageContentType;

    @OneToMany(mappedBy = "resource"/*,cascade = CascadeType.ALL*/,orphanRemoval = true)
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<ResourceScope> scopes = new HashSet<>();

    @OneToMany(mappedBy = "resource"/*,cascade = CascadeType.ALL*/,orphanRemoval = true)
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<UsedResourceScope> usedBies = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getOwner() {
        return owner;
    }

    public Resource owner(String owner) {
        this.owner = owner;
        return this;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public String getApprover() {
        return approver;
    }

    public Resource approver(String approver) {
        this.approver = approver;
        return this;
    }

    public void setApprover(String approver) {
        this.approver = approver;
    }

    public String getDescription() {
        return description;
    }

    public Resource description(String description) {
        this.description = description;
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getRedirectUrl() {
        return redirectUrl;
    }

    public Resource redirectUrl(String redirectUrl) {
        this.redirectUrl = redirectUrl;
        return this;
    }

    public void setRedirectUrl(String redirectUrl) {
        this.redirectUrl = redirectUrl;
    }

    public Status getStatus() {
        return status;
    }

    public Resource status(Status status) {
        this.status = status;
        return this;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public String getClientId() {
        return clientId;
    }

    public Resource clientId(String clientId) {
        this.clientId = clientId;
        return this;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public ResourceType getType() {
        return type;
    }

    public Resource type(ResourceType type) {
        this.type = type;
        return this;
    }

    public void setType(ResourceType type) {
        this.type = type;
    }

    public byte[] getImage() {
        return image;
    }

    public Resource image(byte[] image) {
        this.image = image;
        return this;
    }

    public void setImage(byte[] image) {
        this.image = image;
    }

    public String getImageContentType() {
        return imageContentType;
    }

    public Resource imageContentType(String imageContentType) {
        this.imageContentType = imageContentType;
        return this;
    }

    public void setImageContentType(String imageContentType) {
        this.imageContentType = imageContentType;
    }

    public Set<ResourceScope> getScopes() {
        return scopes;
    }

    public Resource scopes(Set<ResourceScope> resourceScopes) {
        this.scopes = resourceScopes;
        return this;
    }

    public Resource addScopes(ResourceScope resourceScope) {
        this.scopes.add(resourceScope);
        resourceScope.setResource(this);
        return this;
    }

    public Resource removeScopes(ResourceScope resourceScope) {
        this.scopes.remove(resourceScope);
        resourceScope.setResource(null);
        return this;
    }

    public void setScopes(Set<ResourceScope> resourceScopes) {
        this.scopes = resourceScopes;
    }

    public Set<UsedResourceScope> getUsedBies() {
        return usedBies;
    }

    public Resource usedBies(Set<UsedResourceScope> usedResourceScopes) {
        this.usedBies = usedResourceScopes;
        return this;
    }

    public Resource addUsedBy(UsedResourceScope usedResourceScope) {
        this.usedBies.add(usedResourceScope);
        usedResourceScope.setResource(this);
        return this;
    }

    public Resource removeUsedBy(UsedResourceScope usedResourceScope) {
        this.usedBies.remove(usedResourceScope);
        usedResourceScope.setResource(null);
        return this;
    }

    public void setUsedBies(Set<UsedResourceScope> usedResourceScopes) {
        this.usedBies = usedResourceScopes;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Resource)) {
            return false;
        }
        return id != null && id.equals(((Resource) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "Resource{" +
            "id=" + getId() +
            ", owner='" + getOwner() + "'" +
            ", approver='" + getApprover() + "'" +
            ", description='" + getDescription() + "'" +
            ", redirectUrl='" + getRedirectUrl() + "'" +
            ", status='" + getStatus() + "'" +
            ", clientId='" + getClientId() + "'" +
            ", type='" + getType() + "'" +
            ", image='" + getImage() + "'" +
            ", imageContentType='" + getImageContentType() + "'" +
            "}";
    }
}
