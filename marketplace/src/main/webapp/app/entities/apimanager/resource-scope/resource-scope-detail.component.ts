import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IResourceScope } from 'app/shared/model/apimanager/resource-scope.model';

@Component({
  selector: 'jhi-resource-scope-detail',
  templateUrl: './resource-scope-detail.component.html'
})
export class ResourceScopeDetailComponent implements OnInit {
  resourceScope: IResourceScope;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit() {
    this.activatedRoute.data.subscribe(({ resourceScope }) => {
      this.resourceScope = resourceScope;
    });
  }

  previousState() {
    window.history.back();
  }
}
