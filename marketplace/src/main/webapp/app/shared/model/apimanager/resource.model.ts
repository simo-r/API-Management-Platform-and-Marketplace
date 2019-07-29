import { IResourceScope } from 'app/shared/model/apimanager/resource-scope.model';
import { IUsedResourceScope } from 'app/shared/model/apimanager/used-resource-scope.model';

export const enum Status {
  DRAFT = 'DRAFT',
  PENDING = 'PENDING',
  PUBLISHED = 'PUBLISHED',
  REJECTED = 'REJECTED',
  ARCHIVED = 'ARCHIVED'
}

export const enum ResourceType {
  API = 'API',
  CLIENT = 'CLIENT'
}

export interface IResource {
  id?: number;
  owner?: string;
  approver?: string;
  description?: string;
  redirectUrl?: string;
  status?: Status;
  clientId?: string;
  type?: ResourceType;
  imageContentType?: string;
  image?: any;
  scopes?: IResourceScope[];
  usedBies?: IUsedResourceScope[];
}

export class Resource implements IResource {
  constructor(
    public id?: number,
    public owner?: string,
    public approver?: string,
    public description?: string,
    public redirectUrl?: string,
    public status?: Status,
    public clientId?: string,
    public type?: ResourceType,
    public imageContentType?: string,
    public image?: any,
    public scopes?: IResourceScope[],
    public usedBies?: IUsedResourceScope[]
  ) {}
}
