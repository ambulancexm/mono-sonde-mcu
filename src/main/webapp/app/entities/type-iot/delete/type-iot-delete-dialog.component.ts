import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { ITypeIot } from '../type-iot.model';
import { TypeIotService } from '../service/type-iot.service';

@Component({
  templateUrl: './type-iot-delete-dialog.component.html',
})
export class TypeIotDeleteDialogComponent {
  typeIot?: ITypeIot;

  constructor(protected typeIotService: TypeIotService, public activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.typeIotService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
