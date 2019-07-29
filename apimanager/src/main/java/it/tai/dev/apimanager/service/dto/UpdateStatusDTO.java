package it.tai.dev.apimanager.service.dto;

import it.tai.dev.apimanager.domain.enumeration.ValidateErrorCodes;

import java.util.Optional;

/**
 * Wrapper for update transition status of a resource
 */
public class UpdateStatusDTO {


    private final Optional<ResourceDTO> resourceDTO;

    private final ValidateErrorCodes validateErrorCodes;

    public UpdateStatusDTO(Optional<ResourceDTO> resource, ValidateErrorCodes validateErrorCodes) {
        this.resourceDTO = resource;
        this.validateErrorCodes = validateErrorCodes;
    }

    public Optional<ResourceDTO> getResourceDTO() {
        return resourceDTO;
    }


    public ValidateErrorCodes getValidateErrorCodes() {
        return validateErrorCodes;
    }

}


