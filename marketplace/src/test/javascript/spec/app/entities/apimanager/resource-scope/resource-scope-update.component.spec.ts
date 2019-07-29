/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { Observable, of } from 'rxjs';

import { MarketplaceTestModule } from '../../../../test.module';
import { ResourceScopeUpdateComponent } from 'app/entities/apimanager/resource-scope/resource-scope-update.component';
import { ResourceScopeService } from 'app/entities/apimanager/resource-scope/resource-scope.service';
import { ResourceScope } from 'app/shared/model/apimanager/resource-scope.model';

describe('Component Tests', () => {
  describe('ResourceScope Management Update Component', () => {
    let comp: ResourceScopeUpdateComponent;
    let fixture: ComponentFixture<ResourceScopeUpdateComponent>;
    let service: ResourceScopeService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [MarketplaceTestModule],
        declarations: [ResourceScopeUpdateComponent],
        providers: [FormBuilder]
      })
        .overrideTemplate(ResourceScopeUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(ResourceScopeUpdateComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(ResourceScopeService);
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', fakeAsync(() => {
        // GIVEN
        const entity = new ResourceScope(123);
        spyOn(service, 'update').and.returnValue(of(new HttpResponse({ body: entity })));
        comp.updateForm(entity);
        // WHEN
        comp.save();
        tick(); // simulate async

        // THEN
        expect(service.update).toHaveBeenCalledWith(entity);
        expect(comp.isSaving).toEqual(false);
      }));

      it('Should call create service on save for new entity', fakeAsync(() => {
        // GIVEN
        const entity = new ResourceScope();
        spyOn(service, 'create').and.returnValue(of(new HttpResponse({ body: entity })));
        comp.updateForm(entity);
        // WHEN
        comp.save();
        tick(); // simulate async

        // THEN
        expect(service.create).toHaveBeenCalledWith(entity);
        expect(comp.isSaving).toEqual(false);
      }));
    });
  });
});
