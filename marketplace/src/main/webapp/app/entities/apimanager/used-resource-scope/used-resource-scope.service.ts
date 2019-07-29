import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared';
import { IUsedResourceScope } from 'app/shared/model/apimanager/used-resource-scope.model';

type EntityResponseType = HttpResponse<IUsedResourceScope>;
type EntityArrayResponseType = HttpResponse<IUsedResourceScope[]>;

@Injectable({ providedIn: 'root' })
export class UsedResourceScopeService {
  public resourceUrl = SERVER_API_URL + 'services/apimanager/api/used-resource-scopes';

  constructor(protected http: HttpClient) {}

  create(usedResourceScope: IUsedResourceScope): Observable<EntityResponseType> {
    return this.http.post<IUsedResourceScope>(this.resourceUrl, usedResourceScope, { observe: 'response' });
  }

  update(usedResourceScope: IUsedResourceScope): Observable<EntityResponseType> {
    return this.http.put<IUsedResourceScope>(this.resourceUrl, usedResourceScope, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IUsedResourceScope>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IUsedResourceScope[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<any>> {
    return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }
}
