/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { Observable, of } from 'rxjs';

import { MarketplaceTestModule } from '../../../../test.module';
import { UsedResourceScopeUpdateComponent } from 'app/entities/apimanager/used-resource-scope/used-resource-scope-update.component';
import { UsedResourceScopeService } from 'app/entities/apimanager/used-resource-scope/used-resource-scope.service';
import { UsedResourceScope } from 'app/shared/model/apimanager/used-resource-scope.model';

describe('Component Tests', () => {
  describe('UsedResourceScope Management Update Component', () => {
    let comp: UsedResourceScopeUpdateComponent;
    let fixture: ComponentFixture<UsedResourceScopeUpdateComponent>;
    let service: UsedResourceScopeService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [MarketplaceTestModule],
        declarations: [UsedResourceScopeUpdateComponent],
        providers: [FormBuilder]
      })
        .overrideTemplate(UsedResourceScopeUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(UsedResourceScopeUpdateComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(UsedResourceScopeService);
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', fakeAsync(() => {
        // GIVEN
        const entity = new UsedResourceScope(123);
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
        const entity = new UsedResourceScope();
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
