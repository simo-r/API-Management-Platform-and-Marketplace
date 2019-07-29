/* tslint:disable max-line-length */
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { MarketplaceTestModule } from '../../../../test.module';
import { ResourceDetailComponent } from 'app/entities/apimanager/resource/resource-detail.component';
import { Resource } from 'app/shared/model/apimanager/resource.model';

describe('Component Tests', () => {
  describe('Resource Management Detail Component', () => {
    let comp: ResourceDetailComponent;
    let fixture: ComponentFixture<ResourceDetailComponent>;
    const route = ({ data: of({ resource: new Resource(123) }) } as any) as ActivatedRoute;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [MarketplaceTestModule],
        declarations: [ResourceDetailComponent],
        providers: [{ provide: ActivatedRoute, useValue: route }]
      })
        .overrideTemplate(ResourceDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(ResourceDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should call load all on init', () => {
        // GIVEN

        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.resource).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });
  });
});
