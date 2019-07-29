package it.tai.dev.apimanager.service.dto;

import io.github.jhipster.service.filter.Filter;
import io.github.jhipster.service.filter.IntegerFilter;
import io.github.jhipster.service.filter.StringFilter;
import it.tai.dev.apimanager.domain.enumeration.AuthType;


public class ResourceScopeCriteriaDTO {


    private StringFilter name;

    private IntegerFilter authLevel;

    private Filter<AuthType> authTypeFilter;


    private StringFilter clientId;



    public StringFilter getName() {
        return name;
    }

    public void setName(StringFilter name) {
        this.name = name;
    }

    public IntegerFilter getAuthLevel() {
        return authLevel;
    }

    public void setAuthLevel(IntegerFilter authLevel) {
        this.authLevel = authLevel;
    }

    public Filter<AuthType> getAuthTypeFilter() {
        return authTypeFilter;
    }

    public void setAuthTypeFilter(Filter<AuthType> authTypeFilter) {
        this.authTypeFilter = authTypeFilter;
    }

    public StringFilter getClientId() {
        return clientId;
    }

    public void setClientId(StringFilter clientId) {
        this.clientId = clientId;
    }

}
