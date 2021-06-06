import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IIot, Iot } from '../iot.model';
import { IotService } from '../service/iot.service';

@Injectable({ providedIn: 'root' })
export class IotRoutingResolveService implements Resolve<IIot> {
  constructor(protected service: IotService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IIot> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((iot: HttpResponse<Iot>) => {
          if (iot.body) {
            return of(iot.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new Iot());
  }
}
