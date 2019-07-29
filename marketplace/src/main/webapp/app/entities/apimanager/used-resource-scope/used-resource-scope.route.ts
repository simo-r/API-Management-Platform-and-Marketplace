import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';
import { UserRouteAccessService } from 'app/core';
import { Observable, of } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { UsedResourceScope } from 'app/shared/model/apimanager/used-resource-scope.model';
import { UsedResourceScopeService } from './used-resource-scope.service';
import { UsedResourceScopeComponent } from './used-resource-scope.component';
import { UsedResourceScopeDetailComponent } from './used-resource-scope-detail.component';
import { UsedResourceScopeUpdateComponent } from './used-resource-scope-update.component';
import { UsedResourceScopeDeletePopupComponent } from './used-resource-scope-delete-dialog.component';
import { IUsedResourceScope } from 'app/shared/model/apimanager/used-resource-scope.model';

@Injectable({ providedIn: 'root' })
export class UsedResourceScopeResolve implements Resolve<IUsedResourceScope> {
  constructor(private service: UsedResourceScopeService) {}

  resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): Observable<IUsedResourceScope> {
    const id = route.params['id'] ? route.params['id'] : null;
    if (id) {
      return this.service.find(id).pipe(
        filter((response: HttpResponse<UsedResourceScope>) => response.ok),
        map((usedResourceScope: HttpResponse<UsedResourceScope>) => usedResourceScope.body)
      );
    }
    return of(new UsedResourceScope());
  }
}

export const usedResourceScopeRoute: Routes = [
  {
    path: '',
    component: UsedResourceScopeComponent,
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'marketplaceApp.apimanagerUsedResourceScope.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/view',
    component: UsedResourceScopeDetailComponent,
    resolve: {
      usedResourceScope: UsedResourceScopeResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'marketplaceApp.apimanagerUsedResourceScope.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: 'new',
    component: UsedResourceScopeUpdateComponent,
    resolve: {
      usedResourceScope: UsedResourceScopeResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'marketplaceApp.apimanagerUsedResourceScope.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/edit',
    component: UsedResourceScopeUpdateComponent,
    resolve: {
      usedResourceScope: UsedResourceScopeResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'marketplaceApp.apimanagerUsedResourceScope.home.title'
    },
    canActivate: [UserRouteAccessService]
  }
];

export const usedResourceScopePopupRoute: Routes = [
  {
    path: ':id/delete',
    component: UsedResourceScopeDeletePopupComponent,
    resolve: {
      usedResourceScope: UsedResourceScopeResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'marketplaceApp.apimanagerUsedResourceScope.home.title'
    },
    canActivate: [UserRouteAccessService],
    outlet: 'popup'
  }
];
