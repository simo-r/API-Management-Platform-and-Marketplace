export interface IUsedResourceScope {
  id?: number;
  resourceId?: number;
  scopeId?: number;
}

export class UsedResourceScope implements IUsedResourceScope {
  constructor(public id?: number, public resourceId?: number, public scopeId?: number) {}
}
