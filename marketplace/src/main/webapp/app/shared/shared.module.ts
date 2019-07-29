import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { MarketplaceSharedCommonModule, JhiLoginModalComponent, HasAnyAuthorityDirective } from './';

@NgModule({
  imports: [MarketplaceSharedCommonModule],
  declarations: [JhiLoginModalComponent, HasAnyAuthorityDirective],
  entryComponents: [JhiLoginModalComponent],
  exports: [MarketplaceSharedCommonModule, JhiLoginModalComponent, HasAnyAuthorityDirective],
  schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class MarketplaceSharedModule {
  static forRoot() {
    return {
      ngModule: MarketplaceSharedModule
    };
  }
}
