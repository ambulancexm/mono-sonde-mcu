import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IIot } from '../iot.model';

@Component({
  selector: 'jhi-iot-detail',
  templateUrl: './iot-detail.component.html',
})
export class IotDetailComponent implements OnInit {
  iot: IIot | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ iot }) => {
      this.iot = iot;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
