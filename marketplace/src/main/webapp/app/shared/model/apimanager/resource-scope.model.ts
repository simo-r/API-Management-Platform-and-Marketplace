import { IUsedResourceScope } from 'app/shared/model/apimanager/used-resource-scope.model';

export const enum AuthType {
  CNS = 'CNS',
  SPID = 'SPID'
}

export interface IResourceScope {
  id?: number;
  name?: string;
  authLevel?: number;
  authType?: AuthType;
  usedBies?: IUsedResourceScope[];
  resourceId?: number;
}

export class ResourceScope implements IResourceScope {
  constructor(
    public id?: number,
    public name?: string,
    public authLevel?: number,
    public authType?: AuthType,
    public usedBies?: IUsedResourceScope[],
    public resourceId?: number
  ) {}
}
