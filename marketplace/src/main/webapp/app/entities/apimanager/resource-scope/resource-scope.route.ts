import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';
import { UserRouteAccessService } from 'app/core';
import { Observable, of } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { ResourceScope } from 'app/shared/model/apimanager/resource-scope.model';
import { ResourceScopeService } from './resource-scope.service';
import { ResourceScopeComponent } from './resource-scope.component';
import { ResourceScopeDetailComponent } from './resource-scope-detail.component';
import { ResourceScopeUpdateComponent } from './resource-scope-update.component';
import { ResourceScopeDeletePopupComponent } from './resource-scope-delete-dialog.component';
import { IResourceScope } from 'app/shared/model/apimanager/resource-scope.model';

@Injectable({ providedIn: 'root' })
export class ResourceScopeResolve implements Resolve<IResourceScope> {
  constructor(private service: ResourceScopeService) {}

  resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): Observable<IResourceScope> {
    const id = route.params['id'] ? route.params['id'] : null;
    if (id) {
      return this.service.find(id).pipe(
        filter((response: HttpResponse<ResourceScope>) => response.ok),
        map((resourceScope: HttpResponse<ResourceScope>) => resourceScope.body)
      );
    }
    return of(new ResourceScope());
  }
}

export const resourceScopeRoute: Routes = [
  {
    path: '',
    component: ResourceScopeComponent,
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'marketplaceApp.apimanagerResourceScope.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/view',
    component: ResourceScopeDetailComponent,
    resolve: {
      resourceScope: ResourceScopeResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'marketplaceApp.apimanagerResourceScope.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: 'new',
    component: ResourceScopeUpdateComponent,
    resolve: {
      resourceScope: ResourceScopeResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'marketplaceApp.apimanagerResourceScope.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/edit',
    component: ResourceScopeUpdateComponent,
    resolve: {
      resourceScope: ResourceScopeResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'marketplaceApp.apimanagerResourceScope.home.title'
    },
    canActivate: [UserRouteAccessService]
  }
];

export const resourceScopePopupRoute: Routes = [
  {
    path: ':id/delete',
    component: ResourceScopeDeletePopupComponent,
    resolve: {
      resourceScope: ResourceScopeResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'marketplaceApp.apimanagerResourceScope.home.title'
    },
    canActivate: [UserRouteAccessService],
    outlet: 'popup'
  }
];
