import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { TypeIotComponent } from '../list/type-iot.component';
import { TypeIotDetailComponent } from '../detail/type-iot-detail.component';
import { TypeIotUpdateComponent } from '../update/type-iot-update.component';
import { TypeIotRoutingResolveService } from './type-iot-routing-resolve.service';

const typeIotRoute: Routes = [
  {
    path: '',
    component: TypeIotComponent,
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: TypeIotDetailComponent,
    resolve: {
      typeIot: TypeIotRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: TypeIotUpdateComponent,
    resolve: {
      typeIot: TypeIotRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: TypeIotUpdateComponent,
    resolve: {
      typeIot: TypeIotRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(typeIotRoute)],
  exports: [RouterModule],
})
export class TypeIotRoutingModule {}
