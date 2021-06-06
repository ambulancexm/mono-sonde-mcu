import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import { ISensor, Sensor } from '../sensor.model';
import { SensorService } from '../service/sensor.service';

@Component({
  selector: 'jhi-sensor-update',
  templateUrl: './sensor-update.component.html',
})
export class SensorUpdateComponent implements OnInit {
  isSaving = false;

  editForm = this.fb.group({
    id: [],
    typeSensor: [],
    value: [],
  });

  constructor(protected sensorService: SensorService, protected activatedRoute: ActivatedRoute, protected fb: FormBuilder) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ sensor }) => {
      this.updateForm(sensor);
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const sensor = this.createFromForm();
    if (sensor.id !== undefined) {
      this.subscribeToSaveResponse(this.sensorService.update(sensor));
    } else {
      this.subscribeToSaveResponse(this.sensorService.create(sensor));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ISensor>>): void {
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

  protected updateForm(sensor: ISensor): void {
    this.editForm.patchValue({
      id: sensor.id,
      typeSensor: sensor.typeSensor,
      value: sensor.value,
    });
  }

  protected createFromForm(): ISensor {
    return {
      ...new Sensor(),
      id: this.editForm.get(['id'])!.value,
      typeSensor: this.editForm.get(['typeSensor'])!.value,
      value: this.editForm.get(['value'])!.value,
    };
  }
}
