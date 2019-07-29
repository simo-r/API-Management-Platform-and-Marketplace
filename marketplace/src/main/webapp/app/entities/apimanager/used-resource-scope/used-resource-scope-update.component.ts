import { Component, OnInit } from '@angular/core';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { JhiAlertService } from 'ng-jhipster';
import { IUsedResourceScope, UsedResourceScope } from 'app/shared/model/apimanager/used-resource-scope.model';
import { UsedResourceScopeService } from './used-resource-scope.service';
import { IResource } from 'app/shared/model/apimanager/resource.model';
import { ResourceService } from 'app/entities/apimanager/resource';
import { IResourceScope } from 'app/shared/model/apimanager/resource-scope.model';
import { ResourceScopeService } from 'app/entities/apimanager/resource-scope';

@Component({
  selector: 'jhi-used-resource-scope-update',
  templateUrl: './used-resource-scope-update.component.html'
})
export class UsedResourceScopeUpdateComponent implements OnInit {
  isSaving: boolean;

  resources: IResource[];

  resourcescopes: IResourceScope[];

  editForm = this.fb.group({
    id: [],
    resourceId: [null, Validators.required],
    scopeId: [null, Validators.required]
  });

  constructor(
    protected jhiAlertService: JhiAlertService,
    protected usedResourceScopeService: UsedResourceScopeService,
    protected resourceService: ResourceService,
    protected resourceScopeService: ResourceScopeService,
    protected activatedRoute: ActivatedRoute,
    private fb: FormBuilder
  ) {}

  ngOnInit() {
    this.isSaving = false;
    this.activatedRoute.data.subscribe(({ usedResourceScope }) => {
      this.updateForm(usedResourceScope);
    });
    this.resourceService
      .query()
      .pipe(
        filter((mayBeOk: HttpResponse<IResource[]>) => mayBeOk.ok),
        map((response: HttpResponse<IResource[]>) => response.body)
      )
      .subscribe((res: IResource[]) => (this.resources = res), (res: HttpErrorResponse) => this.onError(res.message));
    this.resourceScopeService
      .query()
      .pipe(
        filter((mayBeOk: HttpResponse<IResourceScope[]>) => mayBeOk.ok),
        map((response: HttpResponse<IResourceScope[]>) => response.body)
      )
      .subscribe((res: IResourceScope[]) => (this.resourcescopes = res), (res: HttpErrorResponse) => this.onError(res.message));
  }

  updateForm(usedResourceScope: IUsedResourceScope) {
    this.editForm.patchValue({
      id: usedResourceScope.id,
      resourceId: usedResourceScope.resourceId,
      scopeId: usedResourceScope.scopeId
    });
  }

  previousState() {
    window.history.back();
  }

  save() {
    this.isSaving = true;
    const usedResourceScope = this.createFromForm();
    if (usedResourceScope.id !== undefined) {
      this.subscribeToSaveResponse(this.usedResourceScopeService.update(usedResourceScope));
    } else {
      this.subscribeToSaveResponse(this.usedResourceScopeService.create(usedResourceScope));
    }
  }

  private createFromForm(): IUsedResourceScope {
    const entity = {
      ...new UsedResourceScope(),
      id: this.editForm.get(['id']).value,
      resourceId: this.editForm.get(['resourceId']).value,
      scopeId: this.editForm.get(['scopeId']).value
    };
    return entity;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IUsedResourceScope>>) {
    result.subscribe((res: HttpResponse<IUsedResourceScope>) => this.onSaveSuccess(), (res: HttpErrorResponse) => this.onSaveError());
  }

  protected onSaveSuccess() {
    this.isSaving = false;
    this.previousState();
  }

  protected onSaveError() {
    this.isSaving = false;
  }
  protected onError(errorMessage: string) {
    this.jhiAlertService.error(errorMessage, null, null);
  }

  trackResourceById(index: number, item: IResource) {
    return item.id;
  }

  trackResourceScopeById(index: number, item: IResourceScope) {
    return item.id;
  }
}
