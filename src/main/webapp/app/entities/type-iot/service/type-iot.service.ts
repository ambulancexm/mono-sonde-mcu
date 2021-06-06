import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { ITypeIot, getTypeIotIdentifier } from '../type-iot.model';

export type EntityResponseType = HttpResponse<ITypeIot>;
export type EntityArrayResponseType = HttpResponse<ITypeIot[]>;

@Injectable({ providedIn: 'root' })
export class TypeIotService {
  public resourceUrl = this.applicationConfigService.getEndpointFor('api/type-iots');

  constructor(protected http: HttpClient, private applicationConfigService: ApplicationConfigService) {}

  create(typeIot: ITypeIot): Observable<EntityResponseType> {
    return this.http.post<ITypeIot>(this.resourceUrl, typeIot, { observe: 'response' });
  }

  update(typeIot: ITypeIot): Observable<EntityResponseType> {
    return this.http.put<ITypeIot>(`${this.resourceUrl}/${getTypeIotIdentifier(typeIot) as number}`, typeIot, { observe: 'response' });
  }

  partialUpdate(typeIot: ITypeIot): Observable<EntityResponseType> {
    return this.http.patch<ITypeIot>(`${this.resourceUrl}/${getTypeIotIdentifier(typeIot) as number}`, typeIot, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<ITypeIot>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<ITypeIot[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addTypeIotToCollectionIfMissing(typeIotCollection: ITypeIot[], ...typeIotsToCheck: (ITypeIot | null | undefined)[]): ITypeIot[] {
    const typeIots: ITypeIot[] = typeIotsToCheck.filter(isPresent);
    if (typeIots.length > 0) {
      const typeIotCollectionIdentifiers = typeIotCollection.map(typeIotItem => getTypeIotIdentifier(typeIotItem)!);
      const typeIotsToAdd = typeIots.filter(typeIotItem => {
        const typeIotIdentifier = getTypeIotIdentifier(typeIotItem);
        if (typeIotIdentifier == null || typeIotCollectionIdentifiers.includes(typeIotIdentifier)) {
          return false;
        }
        typeIotCollectionIdentifiers.push(typeIotIdentifier);
        return true;
      });
      return [...typeIotsToAdd, ...typeIotCollection];
    }
    return typeIotCollection;
  }
}
