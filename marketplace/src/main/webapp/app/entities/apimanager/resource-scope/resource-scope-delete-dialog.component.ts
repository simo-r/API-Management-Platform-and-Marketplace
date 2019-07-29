import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';

import { NgbActiveModal, NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { IResourceScope } from 'app/shared/model/apimanager/resource-scope.model';
import { ResourceScopeService } from './resource-scope.service';

@Component({
  selector: 'jhi-resource-scope-delete-dialog',
  templateUrl: './resource-scope-delete-dialog.component.html'
})
export class ResourceScopeDeleteDialogComponent {
  resourceScope: IResourceScope;

  constructor(
    protected resourceScopeService: ResourceScopeService,
    public activeModal: NgbActiveModal,
    protected eventManager: JhiEventManager
  ) {}

  clear() {
    this.activeModal.dismiss('cancel');
  }

  confirmDelete(id: number) {
    this.resourceScopeService.delete(id).subscribe(response => {
      this.eventManager.broadcast({
        name: 'resourceScopeListModification',
        content: 'Deleted an resourceScope'
      });
      this.activeModal.dismiss(true);
    });
  }
}

@Component({
  selector: 'jhi-resource-scope-delete-popup',
  template: ''
})
export class ResourceScopeDeletePopupComponent implements OnInit, OnDestroy {
  protected ngbModalRef: NgbModalRef;

  constructor(protected activatedRoute: ActivatedRoute, protected router: Router, protected modalService: NgbModal) {}

  ngOnInit() {
    this.activatedRoute.data.subscribe(({ resourceScope }) => {
      setTimeout(() => {
        this.ngbModalRef = this.modalService.open(ResourceScopeDeleteDialogComponent as Component, { size: 'lg', backdrop: 'static' });
        this.ngbModalRef.componentInstance.resourceScope = resourceScope;
        this.ngbModalRef.result.then(
          result => {
            this.router.navigate(['/resource-scope', { outlets: { popup: null } }]);
            this.ngbModalRef = null;
          },
          reason => {
            this.router.navigate(['/resource-scope', { outlets: { popup: null } }]);
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
