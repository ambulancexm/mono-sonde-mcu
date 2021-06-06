import { NgModule } from '@angular/core';

import { SharedModule } from 'app/shared/shared.module';
import { IotComponent } from './list/iot.component';
import { IotDetailComponent } from './detail/iot-detail.component';
import { IotUpdateComponent } from './update/iot-update.component';
import { IotDeleteDialogComponent } from './delete/iot-delete-dialog.component';
import { IotRoutingModule } from './route/iot-routing.module';

@NgModule({
  imports: [SharedModule, IotRoutingModule],
  declarations: [IotComponent, IotDetailComponent, IotUpdateComponent, IotDeleteDialogComponent],
  entryComponents: [IotDeleteDialogComponent],
})
export class IotModule {}
