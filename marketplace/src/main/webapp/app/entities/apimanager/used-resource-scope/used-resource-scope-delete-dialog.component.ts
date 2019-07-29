import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';

import { NgbActiveModal, NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { IUsedResourceScope } from 'app/shared/model/apimanager/used-resource-scope.model';
import { UsedResourceScopeService } from './used-resource-scope.service';

@Component({
  selector: 'jhi-used-resource-scope-delete-dialog',
  templateUrl: './used-resource-scope-delete-dialog.component.html'
})
export class UsedResourceScopeDeleteDialogComponent {
  usedResourceScope: IUsedResourceScope;

  constructor(
    protected usedResourceScopeService: UsedResourceScopeService,
    public activeModal: NgbActiveModal,
    protected eventManager: JhiEventManager
  ) {}

  clear() {
    this.activeModal.dismiss('cancel');
  }

  confirmDelete(id: number) {
    this.usedResourceScopeService.delete(id).subscribe(response => {
      this.eventManager.broadcast({
        name: 'usedResourceScopeListModification',
        content: 'Deleted an usedResourceScope'
      });
      this.activeModal.dismiss(true);
    });
  }
}

@Component({
  selector: 'jhi-used-resource-scope-delete-popup',
  template: ''
})
export class UsedResourceScopeDeletePopupComponent implements OnInit, OnDestroy {
  protected ngbModalRef: NgbModalRef;

  constructor(protected activatedRoute: ActivatedRoute, protected router: Router, protected modalService: NgbModal) {}

  ngOnInit() {
    this.activatedRoute.data.subscribe(({ usedResourceScope }) => {
      setTimeout(() => {
        this.ngbModalRef = this.modalService.open(UsedResourceScopeDeleteDialogComponent as Component, { size: 'lg', backdrop: 'static' });
        this.ngbModalRef.componentInstance.usedResourceScope = usedResourceScope;
        this.ngbModalRef.result.then(
          result => {
            this.router.navigate(['/used-resource-scope', { outlets: { popup: null } }]);
            this.ngbModalRef = null;
          },
          reason => {
            this.router.navigate(['/used-resource-scope', { outlets: { popup: null } }]);
            this.ngbModalRef = null;
          }
        );
      }, 0);
    });
  }

  ngOnDestroy() {
    this.ngbModalRef = null;
  }
}
