import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { IotComponent } from '../list/iot.component';
import { IotDetailComponent } from '../detail/iot-detail.component';
import { IotUpdateComponent } from '../update/iot-update.component';
import { IotRoutingResolveService } from './iot-routing-resolve.service';

const iotRoute: Routes = [
  {
    path: '',
    component: IotComponent,
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: IotDetailComponent,
    resolve: {
      iot: IotRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: IotUpdateComponent,
    resolve: {
      iot: IotRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: IotUpdateComponent,
    resolve: {
      iot: IotRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(iotRoute)],
  exports: [RouterModule],
})
export class IotRoutingModule {}
