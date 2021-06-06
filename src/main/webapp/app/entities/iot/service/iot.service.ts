import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IIot, getIotIdentifier } from '../iot.model';

export type EntityResponseType = HttpResponse<IIot>;
export type EntityArrayResponseType = HttpResponse<IIot[]>;

@Injectable({ providedIn: 'root' })
export class IotService {
  public resourceUrl = this.applicationConfigService.getEndpointFor('api/iots');

  constructor(protected http: HttpClient, private applicationConfigService: ApplicationConfigService) {}

  create(iot: IIot): Observable<EntityResponseType> {
    return this.http.post<IIot>(this.resourceUrl, iot, { observe: 'response' });
  }

  update(iot: IIot): Observable<EntityResponseType> {
    return this.http.put<IIot>(`${this.resourceUrl}/${getIotIdentifier(iot) as number}`, iot, { observe: 'response' });
  }

  partialUpdate(iot: IIot): Observable<EntityResponseType> {
    return this.http.patch<IIot>(`${this.resourceUrl}/${getIotIdentifier(iot) as number}`, iot, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IIot>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IIot[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addIotToCollectionIfMissing(iotCollection: IIot[], ...iotsToCheck: (IIot | null | undefined)[]): IIot[] {
    const iots: IIot[] = iotsToCheck.filter(isPresent);
    if (iots.length > 0) {
      const iotCollectionIdentifiers = iotCollection.map(iotItem => getIotIdentifier(iotItem)!);
      const iotsToAdd = iots.filter(iotItem => {
        const iotIdentifier = getIotIdentifier(iotItem);
        if (iotIdentifier == null || iotCollectionIdentifiers.includes(iotIdentifier)) {
          return false;
        }
        iotCollectionIdentifiers.push(iotIdentifier);
        return true;
      });
      return [...iotsToAdd, ...iotCollection];
    }
    return iotCollection;
  }
}
