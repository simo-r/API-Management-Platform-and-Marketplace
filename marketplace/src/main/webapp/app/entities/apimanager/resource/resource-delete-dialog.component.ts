import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';

import { NgbActiveModal, NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { IResource } from 'app/shared/model/apimanager/resource.model';
import { ResourceService } from './resource.service';

@Component({
  selector: 'jhi-resource-delete-dialog',
  templateUrl: './resource-delete-dialog.component.html'
})
export class ResourceDeleteDialogComponent {
  resource: IResource;

  constructor(protected resourceService: ResourceService, public activeModal: NgbActiveModal, protected eventManager: JhiEventManager) {}

  clear() {
    this.activeModal.dismiss('cancel');
  }

  confirmDelete(id: number) {
    this.resourceService.delete(id).subscribe(response => {
      this.eventManager.broadcast({
        name: 'resourceListModification',
        content: 'Deleted an resource'
      });
      this.activeModal.dismiss(true);
    });
  }
}

@Component({
  selector: 'jhi-resource-delete-popup',
  template: ''
})
export class ResourceDeletePopupComponent implements OnInit, OnDestroy {
  protected ngbModalRef: NgbModalRef;

  constructor(protected activatedRoute: ActivatedRoute, protected router: Router, protected modalService: NgbModal) {}

  ngOnInit() {
    this.activatedRoute.data.subscribe(({ resource }) => {
      setTimeout(() => {
        this.ngbModalRef = this.modalService.open(ResourceDeleteDialogComponent as Component, { size: 'lg', backdrop: 'static' });
        this.ngbModalRef.componentInstance.resource = resource;
        this.ngbModalRef.result.then(
          result => {
            this.router.navigate(['/resource', { outlets: { popup: null } }]);
            this.ngbModalRef = null;
          },
          reason => {
            this.router.navigate(['/resource', { outlets: { popup: null } }]);
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
