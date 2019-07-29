package it.tai.dev.apimanager.service.dto;

import javax.ws.rs.core.Response;

/**
 * Wrapper for saved resource
 */
//@Data
public class SaveDTO {

    private final ResourceDTO resourceDTO;

    private final Response response;

    public SaveDTO(ResourceDTO resourceDTO, Response response){
        this.resourceDTO = resourceDTO;
        this.response = response;
    }

    public Response getResponse() {
        return response;
    }

    public ResourceDTO getResourceDTO() {
        return resourceDTO;
    }
}
