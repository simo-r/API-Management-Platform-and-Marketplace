import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared';
import { IResourceScope } from 'app/shared/model/apimanager/resource-scope.model';

type EntityResponseType = HttpResponse<IResourceScope>;
type EntityArrayResponseType = HttpResponse<IResourceScope[]>;

@Injectable({ providedIn: 'root' })
export class ResourceScopeService {
  public resourceUrl = SERVER_API_URL + 'services/apimanager/api/resource-scopes';

  constructor(protected http: HttpClient) {}

  create(resourceScope: IResourceScope): Observable<EntityResponseType> {
    return this.http.post<IResourceScope>(this.resourceUrl, resourceScope, { observe: 'response' });
  }

  update(resourceScope: IResourceScope): Observable<EntityResponseType> {
    return this.http.put<IResourceScope>(this.resourceUrl, resourceScope, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IResourceScope>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IResourceScope[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<any>> {
    return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }
}
