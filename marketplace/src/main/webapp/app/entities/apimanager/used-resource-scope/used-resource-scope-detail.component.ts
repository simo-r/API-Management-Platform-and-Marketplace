import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IUsedResourceScope } from 'app/shared/model/apimanager/used-resource-scope.model';

@Component({
  selector: 'jhi-used-resource-scope-detail',
  templateUrl: './used-resource-scope-detail.component.html'
})
export class UsedResourceScopeDetailComponent implements OnInit {
  usedResourceScope: IUsedResourceScope;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit() {
    this.activatedRoute.data.subscribe(({ usedResourceScope }) => {
      this.usedResourceScope = usedResourceScope;
    });
  }

  previousState() {
    window.history.back();
  }
}
