import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { ITypeIot } from '../type-iot.model';

@Component({
  selector: 'jhi-type-iot-detail',
  templateUrl: './type-iot-detail.component.html',
})
export class TypeIotDetailComponent implements OnInit {
  typeIot: ITypeIot | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ typeIot }) => {
      this.typeIot = typeIot;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
