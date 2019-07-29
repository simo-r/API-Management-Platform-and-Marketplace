import { Component, OnInit } from '@angular/core';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { JhiAlertService } from 'ng-jhipster';
import { IResourceScope, ResourceScope } from 'app/shared/model/apimanager/resource-scope.model';
import { ResourceScopeService } from './resource-scope.service';
import { IResource } from 'app/shared/model/apimanager/resource.model';
import { ResourceService } from 'app/entities/apimanager/resource';

@Component({
  selector: 'jhi-resource-scope-update',
  templateUrl: './resource-scope-update.component.html'
})
export class ResourceScopeUpdateComponent implements OnInit {
  isSaving: boolean;

  resources: IResource[];

  editForm = this.fb.group({
    id: [],
    name: [null, [Validators.required]],
    authLevel: [null, [Validators.required]],
    authType: [null, [Validators.required]],
    resourceId: [null, Validators.required]
  });

  constructor(
    protected jhiAlertService: JhiAlertService,
    protected resourceScopeService: ResourceScopeService,
    protected resourceService: ResourceService,
    protected activatedRoute: ActivatedRoute,
    private fb: FormBuilder
  ) {}

  ngOnInit() {
    this.isSaving = false;
    this.activatedRoute.data.subscribe(({ resourceScope }) => {
      this.updateForm(resourceScope);
    });
    this.resourceService
      .query()
      .pipe(
        filter((mayBeOk: HttpResponse<IResource[]>) => mayBeOk.ok),
        map((response: HttpResponse<IResource[]>) => response.body)
      )
      .subscribe((res: IResource[]) => (this.resources = res), (res: HttpErrorResponse) => this.onError(res.message));
  }

  updateForm(resourceScope: IResourceScope) {
    this.editForm.patchValue({
      id: resourceScope.id,
      name: resourceScope.name,
      authLevel: resourceScope.authLevel,
      authType: resourceScope.authType,
      resourceId: resourceScope.resourceId
    });
  }

  previousState() {
    window.history.back();
  }

  save() {
    this.isSaving = true;
    const resourceScope = this.createFromForm();
    if (resourceScope.id !== undefined) {
      this.subscribeToSaveResponse(this.resourceScopeService.update(resourceScope));
    } else {
      this.subscribeToSaveResponse(this.resourceScopeService.create(resourceScope));
    }
  }

  private createFromForm(): IResourceScope {
    const entity = {
      ...new ResourceScope(),
      id: this.editForm.get(['id']).value,
      name: this.editForm.get(['name']).value,
      authLevel: this.editForm.get(['authLevel']).value,
      authType: this.editForm.get(['authType']).value,
      resourceId: this.editForm.get(['resourceId']).value
    };
    return entity;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IResourceScope>>) {
    result.subscribe((res: HttpResponse<IResourceScope>) => this.onSaveSuccess(), (res: HttpErrorResponse) => this.onSaveError());
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
}
