import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { JhiDataUtils } from 'ng-jhipster';

import { IResource } from 'app/shared/model/apimanager/resource.model';

@Component({
  selector: 'jhi-resource-detail',
  templateUrl: './resource-detail.component.html'
})
export class ResourceDetailComponent implements OnInit {
  resource: IResource;

  constructor(protected dataUtils: JhiDataUtils, protected activatedRoute: ActivatedRoute) {}

  ngOnInit() {
    this.activatedRoute.data.subscribe(({ resource }) => {
      this.resource = resource;
    });
  }

  byteSize(field) {
    return this.dataUtils.byteSize(field);
  }

  openFile(contentType, field) {
    return this.dataUtils.openFile(contentType, field);
  }
  previousState() {
    window.history.back();
  }
}
