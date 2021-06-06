import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { ITypeIot, TypeIot } from '../type-iot.model';
import { TypeIotService } from '../service/type-iot.service';

@Injectable({ providedIn: 'root' })
export class TypeIotRoutingResolveService implements Resolve<ITypeIot> {
  constructor(protected service: TypeIotService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<ITypeIot> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((typeIot: HttpResponse<TypeIot>) => {
          if (typeIot.body) {
            return of(typeIot.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new TypeIot());
  }
}
