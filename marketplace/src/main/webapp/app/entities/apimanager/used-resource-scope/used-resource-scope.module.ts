import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';
import { JhiLanguageService } from 'ng-jhipster';
import { JhiLanguageHelper } from 'app/core';

import { MarketplaceSharedModule } from 'app/shared';
import {
  UsedResourceScopeComponent,
  UsedResourceScopeDetailComponent,
  UsedResourceScopeUpdateComponent,
  UsedResourceScopeDeletePopupComponent,
  UsedResourceScopeDeleteDialogComponent,
  usedResourceScopeRoute,
  usedResourceScopePopupRoute
} from './';

const ENTITY_STATES = [...usedResourceScopeRoute, ...usedResourceScopePopupRoute];

@NgModule({
  imports: [MarketplaceSharedModule, RouterModule.forChild(ENTITY_STATES)],
  declarations: [
    UsedResourceScopeComponent,
    UsedResourceScopeDetailComponent,
    UsedResourceScopeUpdateComponent,
    UsedResourceScopeDeleteDialogComponent,
    UsedResourceScopeDeletePopupComponent
  ],
  entryComponents: [
    UsedResourceScopeComponent,
    UsedResourceScopeUpdateComponent,
    UsedResourceScopeDeleteDialogComponent,
    UsedResourceScopeDeletePopupComponent
  ],
  providers: [{ provide: JhiLanguageService, useClass: JhiLanguageService }],
  schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class ApimanagerUsedResourceScopeModule {
  constructor(private languageService: JhiLanguageService, private languageHelper: JhiLanguageHelper) {
    this.languageHelper.language.subscribe((languageKey: string) => {
      if (languageKey !== undefined) {
        this.languageService.changeLanguage(languageKey);
      }
    });
  }
}
