import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';
import { UserRouteAccessService } from 'app/core';
import { Observable, of } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { Resource } from 'app/shared/model/apimanager/resource.model';
import { ResourceService } from './resource.service';
import { ResourceComponent } from './resource.component';
import { ResourceDetailComponent } from './resource-detail.component';
import { ResourceUpdateComponent } from './resource-update.component';
import { ResourceDeletePopupComponent } from './resource-delete-dialog.component';
import { IResource } from 'app/shared/model/apimanager/resource.model';

@Injectable({ providedIn: 'root' })
export class ResourceResolve implements Resolve<IResource> {
  constructor(private service: ResourceService) {}

  resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): Observable<IResource> {
    const id = route.params['id'] ? route.params['id'] : null;
    if (id) {
      return this.service.find(id).pipe(
        filter((response: HttpResponse<Resource>) => response.ok),
        map((resource: HttpResponse<Resource>) => resource.body)
      );
    }
    return of(new Resource());
  }
}

export const resourceRoute: Routes = [
  {
    path: '',
    component: ResourceComponent,
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'marketplaceApp.apimanagerResource.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/view',
    component: ResourceDetailComponent,
    resolve: {
      resource: ResourceResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'marketplaceApp.apimanagerResource.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: 'new',
    component: ResourceUpdateComponent,
    resolve: {
      resource: ResourceResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'marketplaceApp.apimanagerResource.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/edit',
    component: ResourceUpdateComponent,
    resolve: {
      resource: ResourceResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'marketplaceApp.apimanagerResource.home.title'
    },
    canActivate: [UserRouteAccessService]
  }
];

export const resourcePopupRoute: Routes = [
  {
    path: ':id/delete',
    component: ResourceDeletePopupComponent,
    resolve: {
      resource: ResourceResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'marketplaceApp.apimanagerResource.home.title'
    },
    canActivate: [UserRouteAccessService],
    outlet: 'popup'
  }
];
