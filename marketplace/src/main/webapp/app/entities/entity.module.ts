import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

@NgModule({
  imports: [
    RouterModule.forChild([
      {
        path: 'resource',
        loadChildren: './apimanager/resource/resource.module#ApimanagerResourceModule'
      },
      {
        path: 'resource-scope',
        loadChildren: './apimanager/resource-scope/resource-scope.module#ApimanagerResourceScopeModule'
      },
      {
        path: 'used-resource-scope',
        loadChildren: './apimanager/used-resource-scope/used-resource-scope.module#ApimanagerUsedResourceScopeModule'
      }
      /* jhipster-needle-add-entity-route - JHipster will add entity modules routes here */
    ])
  ],
  declarations: [],
  entryComponents: [],
  providers: [],
  schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class MarketplaceEntityModule {}
