package it.tai.dev.apimanager.service.mapper;

import it.tai.dev.apimanager.domain.*;
import it.tai.dev.apimanager.service.dto.ResourceDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity {@link Resource} and its DTO {@link ResourceDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface ResourceMapper extends EntityMapper<ResourceDTO, Resource> {


    @Mapping(target = "scopes", ignore = true)
    @Mapping(target = "removeScopes", ignore = true)
    @Mapping(target = "usedBies", ignore = true)
    @Mapping(target = "removeUsedBy", ignore = true)
    Resource toEntity(ResourceDTO resourceDTO);

    default Resource fromId(Long id) {
        if (id == null) {
            return null;
        }
        Resource resource = new Resource();
        resource.setId(id);
        return resource;
    }
}
