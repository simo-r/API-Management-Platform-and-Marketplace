/* tslint:disable max-line-length */
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { MarketplaceTestModule } from '../../../../test.module';
import { ResourceScopeDetailComponent } from 'app/entities/apimanager/resource-scope/resource-scope-detail.component';
import { ResourceScope } from 'app/shared/model/apimanager/resource-scope.model';

describe('Component Tests', () => {
  describe('ResourceScope Management Detail Component', () => {
    let comp: ResourceScopeDetailComponent;
    let fixture: ComponentFixture<ResourceScopeDetailComponent>;
    const route = ({ data: of({ resourceScope: new ResourceScope(123) }) } as any) as ActivatedRoute;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [MarketplaceTestModule],
        declarations: [ResourceScopeDetailComponent],
        providers: [{ provide: ActivatedRoute, useValue: route }]
      })
        .overrideTemplate(ResourceScopeDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(ResourceScopeDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should call load all on init', () => {
        // GIVEN

        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.resourceScope).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });
  });
});
