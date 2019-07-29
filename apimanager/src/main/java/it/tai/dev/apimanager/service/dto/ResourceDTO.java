package it.tai.dev.apimanager.service.dto;

import it.tai.dev.apimanager.domain.enumeration.ResourceType;
import it.tai.dev.apimanager.domain.enumeration.Status;

import javax.persistence.Lob;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link it.tai.dev.apimanager.domain.Resource} entity.
 */
public class ResourceDTO implements Serializable {

    private Long id;

    @NotNull
    private String owner;

    @NotNull
    private String approver;

    @NotNull
    private String description;

    @NotNull
    private String redirectUrl;

    @NotNull
    private Status status;

    @NotNull
    private String clientId;

    @NotNull
    private ResourceType type;

    @Lob
    private byte[] image;

    private String imageContentType;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public String getApprover() {
        return approver;
    }

    public void setApprover(String approver) {
        this.approver = approver;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getRedirectUrl() {
        return redirectUrl;
    }

    public void setRedirectUrl(String redirectUrl) {
        this.redirectUrl = redirectUrl;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public ResourceType getType() {
        return type;
    }

    public void setType(ResourceType type) {
        this.type = type;
    }

    public byte[] getImage() {
        return image;
    }

    public void setImage(byte[] image) {
        this.image = image;
    }

    public String getImageContentType() {
        return imageContentType;
    }

    public void setImageContentType(String imageContentType) {
        this.imageContentType = imageContentType;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        ResourceDTO resourceDTO = (ResourceDTO) o;
        if (resourceDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), resourceDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "ResourceDTO{" +
            "id=" + getId() +
            ", owner='" + getOwner() + "'" +
            ", approver='" + getApprover() + "'" +
            ", description='" + getDescription() + "'" +
            ", redirectUrl='" + getRedirectUrl() + "'" +
            ", status='" + getStatus() + "'" +
            ", clientId='" + getClientId() + "'" +
            ", type='" + getType() + "'" +
            ", image='" + getImage() + "'" +
            "}";
    }
}
