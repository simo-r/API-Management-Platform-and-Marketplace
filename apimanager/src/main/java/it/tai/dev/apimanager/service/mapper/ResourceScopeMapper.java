package it.tai.dev.apimanager.service.mapper;

import it.tai.dev.apimanager.domain.*;
import it.tai.dev.apimanager.service.dto.ResourceScopeDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity {@link ResourceScope} and its DTO {@link ResourceScopeDTO}.
 */
@Mapper(componentModel = "spring", uses = {ResourceMapper.class})
public interface ResourceScopeMapper extends EntityMapper<ResourceScopeDTO, ResourceScope> {

    @Mapping(source = "resource.id", target = "resourceId")
    ResourceScopeDTO toDto(ResourceScope resourceScope);

    @Mapping(target = "usedBies", ignore = true)
    @Mapping(target = "removeUsedBy", ignore = true)
    @Mapping(source = "resourceId", target = "resource")
    ResourceScope toEntity(ResourceScopeDTO resourceScopeDTO);

    default ResourceScope fromId(Long id) {
        if (id == null) {
            return null;
        }
        ResourceScope resourceScope = new ResourceScope();
        resourceScope.setId(id);
        return resourceScope;
    }
}
