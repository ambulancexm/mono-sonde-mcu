import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { ITypeIot } from '../type-iot.model';
import { TypeIotService } from '../service/type-iot.service';
import { TypeIotDeleteDialogComponent } from '../delete/type-iot-delete-dialog.component';

@Component({
  selector: 'jhi-type-iot',
  templateUrl: './type-iot.component.html',
})
export class TypeIotComponent implements OnInit {
  typeIots?: ITypeIot[];
  isLoading = false;

  constructor(protected typeIotService: TypeIotService, protected modalService: NgbModal) {}

  loadAll(): void {
    this.isLoading = true;

    this.typeIotService.query().subscribe(
      (res: HttpResponse<ITypeIot[]>) => {
        this.isLoading = false;
        this.typeIots = res.body ?? [];
      },
      () => {
        this.isLoading = false;
      }
    );
  }

  ngOnInit(): void {
    this.loadAll();
  }

  trackId(index: number, item: ITypeIot): number {
    return item.id!;
  }

  delete(typeIot: ITypeIot): void {
    const modalRef = this.modalService.open(TypeIotDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.typeIot = typeIot;
    // unsubscribe not needed because closed completes on modal close
    modalRef.closed.subscribe(reason => {
      if (reason === 'deleted') {
        this.loadAll();
      }
    });
  }
}
