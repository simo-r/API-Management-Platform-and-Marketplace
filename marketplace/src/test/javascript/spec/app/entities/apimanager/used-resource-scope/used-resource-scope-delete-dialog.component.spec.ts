/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, inject, fakeAsync, tick } from '@angular/core/testing';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { Observable, of } from 'rxjs';
import { JhiEventManager } from 'ng-jhipster';

import { MarketplaceTestModule } from '../../../../test.module';
import { UsedResourceScopeDeleteDialogComponent } from 'app/entities/apimanager/used-resource-scope/used-resource-scope-delete-dialog.component';
import { UsedResourceScopeService } from 'app/entities/apimanager/used-resource-scope/used-resource-scope.service';

describe('Component Tests', () => {
  describe('UsedResourceScope Management Delete Component', () => {
    let comp: UsedResourceScopeDeleteDialogComponent;
    let fixture: ComponentFixture<UsedResourceScopeDeleteDialogComponent>;
    let service: UsedResourceScopeService;
    let mockEventManager: any;
    let mockActiveModal: any;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [MarketplaceTestModule],
        declarations: [UsedResourceScopeDeleteDialogComponent]
      })
        .overrideTemplate(UsedResourceScopeDeleteDialogComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(UsedResourceScopeDeleteDialogComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(UsedResourceScopeService);
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
