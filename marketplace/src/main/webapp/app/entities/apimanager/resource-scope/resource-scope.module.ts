import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';
import { JhiLanguageService } from 'ng-jhipster';
import { JhiLanguageHelper } from 'app/core';

import { MarketplaceSharedModule } from 'app/shared';
import {
  ResourceScopeComponent,
  ResourceScopeDetailComponent,
  ResourceScopeUpdateComponent,
  ResourceScopeDeletePopupComponent,
  ResourceScopeDeleteDialogComponent,
  resourceScopeRoute,
  resourceScopePopupRoute
} from './';

const ENTITY_STATES = [...resourceScopeRoute, ...resourceScopePopupRoute];

@NgModule({
  imports: [MarketplaceSharedModule, RouterModule.forChild(ENTITY_STATES)],
  declarations: [
    ResourceScopeComponent,
    ResourceScopeDetailComponent,
    ResourceScopeUpdateComponent,
    ResourceScopeDeleteDialogComponent,
    ResourceScopeDeletePopupComponent
  ],
  entryComponents: [
    ResourceScopeComponent,
    ResourceScopeUpdateComponent,
    ResourceScopeDeleteDialogComponent,
    ResourceScopeDeletePopupComponent
  ],
  providers: [{ provide: JhiLanguageService, useClass: JhiLanguageService }],
  schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class ApimanagerResourceScopeModule {
  constructor(private languageService: JhiLanguageService, private languageHelper: JhiLanguageHelper) {
    this.languageHelper.language.subscribe((languageKey: string) => {
      if (languageKey !== undefined) {
        this.languageService.changeLanguage(languageKey);
      }
    });
  }
}
