import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { IIot, Iot } from '../iot.model';
import { IotService } from '../service/iot.service';
import { ISensor } from 'app/entities/sensor/sensor.model';
import { SensorService } from 'app/entities/sensor/service/sensor.service';

@Component({
  selector: 'jhi-iot-update',
  templateUrl: './iot-update.component.html',
})
export class IotUpdateComponent implements OnInit {
  isSaving = false;

  sensorsSharedCollection: ISensor[] = [];

  editForm = this.fb.group({
    id: [],
    mac: [],
    sensor: [],
  });

  constructor(
    protected iotService: IotService,
    protected sensorService: SensorService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ iot }) => {
      this.updateForm(iot);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const iot = this.createFromForm();
    if (iot.id !== undefined) {
      this.subscribeToSaveResponse(this.iotService.update(iot));
    } else {
      this.subscribeToSaveResponse(this.iotService.create(iot));
    }
  }

  trackSensorById(index: number, item: ISensor): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IIot>>): void {
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe(
      () => this.onSaveSuccess(),
      () => this.onSaveError()
    );
  }

  protected onSaveSuccess(): void {
    this.previousState();
  }

  protected onSaveError(): void {
    // Api for inheritance.
  }

  protected onSaveFinalize(): void {
    this.isSaving = false;
  }

  protected updateForm(iot: IIot): void {
    this.editForm.patchValue({
      id: iot.id,
      mac: iot.mac,
      sensor: iot.sensor,
    });

    this.sensorsSharedCollection = this.sensorService.addSensorToCollectionIfMissing(this.sensorsSharedCollection, iot.sensor);
  }

  protected loadRelationshipsOptions(): void {
    this.sensorService
      .query()
      .pipe(map((res: HttpResponse<ISensor[]>) => res.body ?? []))
      .pipe(map((sensors: ISensor[]) => this.sensorService.addSensorToCollectionIfMissing(sensors, this.editForm.get('sensor')!.value)))
      .subscribe((sensors: ISensor[]) => (this.sensorsSharedCollection = sensors));
  }

  protected createFromForm(): IIot {
    return {
      ...new Iot(),
      id: this.editForm.get(['id'])!.value,
      mac: this.editForm.get(['mac'])!.value,
      sensor: this.editForm.get(['sensor'])!.value,
    };
  }
}
