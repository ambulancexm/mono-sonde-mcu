import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { ITypeIot, TypeIot } from '../type-iot.model';
import { TypeIotService } from '../service/type-iot.service';
import { IIot } from 'app/entities/iot/iot.model';
import { IotService } from 'app/entities/iot/service/iot.service';

@Component({
  selector: 'jhi-type-iot-update',
  templateUrl: './type-iot-update.component.html',
})
export class TypeIotUpdateComponent implements OnInit {
  isSaving = false;

  iotsSharedCollection: IIot[] = [];

  editForm = this.fb.group({
    id: [],
    name: [],
    iot: [],
  });

  constructor(
    protected typeIotService: TypeIotService,
    protected iotService: IotService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ typeIot }) => {
      this.updateForm(typeIot);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const typeIot = this.createFromForm();
    if (typeIot.id !== undefined) {
      this.subscribeToSaveResponse(this.typeIotService.update(typeIot));
    } else {
      this.subscribeToSaveResponse(this.typeIotService.create(typeIot));
    }
  }

  trackIotById(index: number, item: IIot): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ITypeIot>>): void {
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

  protected updateForm(typeIot: ITypeIot): void {
    this.editForm.patchValue({
      id: typeIot.id,
      name: typeIot.name,
      iot: typeIot.iot,
    });

    this.iotsSharedCollection = this.iotService.addIotToCollectionIfMissing(this.iotsSharedCollection, typeIot.iot);
  }

  protected loadRelationshipsOptions(): void {
    this.iotService
      .query()
      .pipe(map((res: HttpResponse<IIot[]>) => res.body ?? []))
      .pipe(map((iots: IIot[]) => this.iotService.addIotToCollectionIfMissing(iots, this.editForm.get('iot')!.value)))
      .subscribe((iots: IIot[]) => (this.iotsSharedCollection = iots));
  }

  protected createFromForm(): ITypeIot {
    return {
      ...new TypeIot(),
      id: this.editForm.get(['id'])!.value,
      name: this.editForm.get(['name'])!.value,
      iot: this.editForm.get(['iot'])!.value,
    };
  }
}
