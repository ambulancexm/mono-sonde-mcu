import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IIot } from '../iot.model';
import { IotService } from '../service/iot.service';
import { IotDeleteDialogComponent } from '../delete/iot-delete-dialog.component';

@Component({
  selector: 'jhi-iot',
  templateUrl: './iot.component.html',
})
export class IotComponent implements OnInit {
  iots?: IIot[];
  isLoading = false;

  constructor(protected iotService: IotService, protected modalService: NgbModal) {}

  loadAll(): void {
    this.isLoading = true;

    this.iotService.query().subscribe(
      (res: HttpResponse<IIot[]>) => {
        this.isLoading = false;
        this.iots = res.body ?? [];
      },
      () => {
        this.isLoading = false;
      }
    );
  }

  ngOnInit(): void {
    this.loadAll();
  }

  trackId(index: number, item: IIot): number {
    return item.id!;
  }

  delete(iot: IIot): void {
    const modalRef = this.modalService.open(IotDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.iot = iot;
    // unsubscribe not needed because closed completes on modal close
    modalRef.closed.subscribe(reason => {
      if (reason === 'deleted') {
        this.loadAll();
      }
    });
  }
}
