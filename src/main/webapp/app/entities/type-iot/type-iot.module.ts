import { NgModule } from '@angular/core';

import { SharedModule } from 'app/shared/shared.module';
import { TypeIotComponent } from './list/type-iot.component';
import { TypeIotDetailComponent } from './detail/type-iot-detail.component';
import { TypeIotUpdateComponent } from './update/type-iot-update.component';
import { TypeIotDeleteDialogComponent } from './delete/type-iot-delete-dialog.component';
import { TypeIotRoutingModule } from './route/type-iot-routing.module';

@NgModule({
  imports: [SharedModule, TypeIotRoutingModule],
  declarations: [TypeIotComponent, TypeIotDetailComponent, TypeIotUpdateComponent, TypeIotDeleteDialogComponent],
  entryComponents: [TypeIotDeleteDialogComponent],
})
export class TypeIotModule {}
