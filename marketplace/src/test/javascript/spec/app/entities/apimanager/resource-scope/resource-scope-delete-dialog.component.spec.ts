/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, inject, fakeAsync, tick } from '@angular/core/testing';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { Observable, of } from 'rxjs';
import { JhiEventManager } from 'ng-jhipster';

import { MarketplaceTestModule } from '../../../../test.module';
import { ResourceScopeDeleteDialogComponent } from 'app/entities/apimanager/resource-scope/resource-scope-delete-dialog.component';
import { ResourceScopeService } from 'app/entities/apimanager/resource-scope/resource-scope.service';

describe('Component Tests', () => {
  describe('ResourceScope Management Delete Component', () => {
    let comp: ResourceScopeDeleteDialogComponent;
    let fixture: ComponentFixture<ResourceScopeDeleteDialogComponent>;
    let service: ResourceScopeService;
    let mockEventManager: any;
    let mockActiveModal: any;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [MarketplaceTestModule],
        declarations: [ResourceScopeDeleteDialogComponent]
      })
        .overrideTemplate(ResourceScopeDeleteDialogComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(ResourceScopeDeleteDialogComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(ResourceScopeService);
      mockEventManager = fixture.debugElement.injector.get(JhiEventManager);
      mockActiveModal = fixture.debugElement.injector.get(NgbActiveModal);
    });

    describe('confirmDelete', () => {
      it('Should call delete service on confirmDelete', inject(
        [],
        fakeAsync(() => {
          // GIVEN
          spyOn(service, 'delete').and.returnValue(of({}));

          // WHEN
          comp.confirmDelete(123);
          tick();

          // THEN
          expect(service.delete).toHaveBeenCalledWith(123);
          expect(mockActiveModal.dismissSpy).toHaveBeenCalled();
          expect(mockEventManager.broadcastSpy).toHaveBeenCalled();
        })
      ));
    });
  });
});
