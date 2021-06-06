import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IIot } from '../iot.model';
import { IotService } from '../service/iot.service';

@Component({
  templateUrl: './iot-delete-dialog.component.html',
})
export class IotDeleteDialogComponent {
  iot?: IIot;

  constructor(protected iotService: IotService, public activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.iotService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
