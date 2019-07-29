package it.tai.dev.apimanager.repository;

import it.tai.dev.apimanager.domain.ResourceScope;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


/**
 * Spring Data  repository for the ResourceScope entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ResourceScopeRepository extends JpaRepository<ResourceScope, Long>,
                                                    JpaSpecificationExecutor<ResourceScope>{

    Optional<ResourceScope> getAllByNameAndResource_Id(String name, Long resourceId);

    @Query(value = "SELECT u.id from ResourceScope u WHERE u.resource.id = ?1")
    List<Long> findByResource_Id(Long resourceId);

}


