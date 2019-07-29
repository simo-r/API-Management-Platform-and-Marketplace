package it.tai.dev.apimanager.repository;

import it.tai.dev.apimanager.domain.UsedResourceScope;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


/**
 * Spring Data  repository for the UsedResourceScope entity.
 */
@SuppressWarnings("unused")
@Repository
public interface UsedResourceScopeRepository extends JpaRepository<UsedResourceScope, Long> {

    void deleteByResource_IdAndScope_Id(Long resourceId, Long scopeId);

    Optional<UsedResourceScope> findByResource_IdAndScope_Id(Long resourceId, Long scopeId);

    @Query(value = "SELECT u.resource.id from UsedResourceScope u WHERE u.scope.id = ?1")
    List<Long> findByScope_Id(Long scopeId);

}
