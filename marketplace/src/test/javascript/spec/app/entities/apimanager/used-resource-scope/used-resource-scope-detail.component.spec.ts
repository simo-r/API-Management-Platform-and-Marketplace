/* tslint:disable max-line-length */
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { MarketplaceTestModule } from '../../../../test.module';
import { UsedResourceScopeDetailComponent } from 'app/entities/apimanager/used-resource-scope/used-resource-scope-detail.component';
import { UsedResourceScope } from 'app/shared/model/apimanager/used-resource-scope.model';

describe('Component Tests', () => {
  describe('UsedResourceScope Management Detail Component', () => {
    let comp: UsedResourceScopeDetailComponent;
    let fixture: ComponentFixture<UsedResourceScopeDetailComponent>;
    const route = ({ data: of({ usedResourceScope: new UsedResourceScope(123) }) } as any) as ActivatedRoute;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [MarketplaceTestModule],
        declarations: [UsedResourceScopeDetailComponent],
        providers: [{ provide: ActivatedRoute, useValue: route }]
      })
        .overrideTemplate(UsedResourceScopeDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(UsedResourceScopeDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should call load all on init', () => {
        // GIVEN

        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.usedResourceScope).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });
  });
});
