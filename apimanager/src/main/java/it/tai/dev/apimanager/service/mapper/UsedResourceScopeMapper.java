package it.tai.dev.apimanager.service.mapper;

import it.tai.dev.apimanager.domain.*;
import it.tai.dev.apimanager.service.dto.UsedResourceScopeDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity {@link UsedResourceScope} and its DTO {@link UsedResourceScopeDTO}.
 */
@Mapper(componentModel = "spring", uses = {ResourceMapper.class, ResourceScopeMapper.class})
public interface UsedResourceScopeMapper extends EntityMapper<UsedResourceScopeDTO, UsedResourceScope> {

    @Mapping(source = "resource.id", target = "resourceId")
    @Mapping(source = "scope.id", target = "scopeId")
    UsedResourceScopeDTO toDto(UsedResourceScope usedResourceScope);

    @Mapping(source = "resourceId", target = "resource")
    @Mapping(source = "scopeId", target = "scope")
    UsedResourceScope toEntity(UsedResourceScopeDTO usedResourceScopeDTO);

    default UsedResourceScope fromId(Long id) {
        if (id == null) {
            return null;
        }
        UsedResourceScope usedResourceScope = new UsedResourceScope();
        usedResourceScope.setId(id);
        return usedResourceScope;
    }
}
