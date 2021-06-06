jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { IotService } from '../service/iot.service';
import { IIot, Iot } from '../iot.model';
import { ISensor } from 'app/entities/sensor/sensor.model';
import { SensorService } from 'app/entities/sensor/service/sensor.service';

import { IotUpdateComponent } from './iot-update.component';

describe('Component Tests', () => {
  describe('Iot Management Update Component', () => {
    let comp: IotUpdateComponent;
    let fixture: ComponentFixture<IotUpdateComponent>;
    let activatedRoute: ActivatedRoute;
    let iotService: IotService;
    let sensorService: SensorService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [IotUpdateComponent],
        providers: [FormBuilder, ActivatedRoute],
      })
        .overrideTemplate(IotUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(IotUpdateComponent);
      activatedRoute = TestBed.inject(ActivatedRoute);
      iotService = TestBed.inject(IotService);
      sensorService = TestBed.inject(SensorService);

      comp = fixture.componentInstance;
    });

    describe('ngOnInit', () => {
      it('Should call Sensor query and add missing value', () => {
        const iot: IIot = { id: 456 };
        const sensor: ISensor = { id: 56624 };
        iot.sensor = sensor;

        const sensorCollection: ISensor[] = [{ id: 25021 }];
        spyOn(sensorService, 'query').and.returnValue(of(new HttpResponse({ body: sensorCollection })));
        const additionalSensors = [sensor];
        const expectedCollection: ISensor[] = [...additionalSensors, ...sensorCollection];
        spyOn(sensorService, 'addSensorToCollectionIfMissing').and.returnValue(expectedCollection);

        activatedRoute.data = of({ iot });
        comp.ngOnInit();

        expect(sensorService.query).toHaveBeenCalled();
        expect(sensorService.addSensorToCollectionIfMissing).toHaveBeenCalledWith(sensorCollection, ...additionalSensors);
        expect(comp.sensorsSharedCollection).toEqual(expectedCollection);
      });

      it('Should update editForm', () => {
        const iot: IIot = { id: 456 };
        const sensor: ISensor = { id: 40941 };
        iot.sensor = sensor;

        activatedRoute.data = of({ iot });
        comp.ngOnInit();

        expect(comp.editForm.value).toEqual(expect.objectContaining(iot));
        expect(comp.sensorsSharedCollection).toContain(sensor);
      });
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const iot = { id: 123 };
        spyOn(iotService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ iot });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: iot }));
        saveSubject.complete();

        // THEN
        expect(comp.previousState).toHaveBeenCalled();
        expect(iotService.update).toHaveBeenCalledWith(iot);
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const iot = new Iot();
        spyOn(iotService, 'create').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ iot });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: iot }));
        saveSubject.complete();

        // THEN
        expect(iotService.create).toHaveBeenCalledWith(iot);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).toHaveBeenCalled();
      });

      it('Should set isSaving to false on error', () => {
        // GIVEN
        const saveSubject = new Subject();
        const iot = { id: 123 };
        spyOn(iotService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ iot });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.error('This is an error!');

        // THEN
        expect(iotService.update).toHaveBeenCalledWith(iot);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).not.toHaveBeenCalled();
      });
    });

    describe('Tracking relationships identifiers', () => {
      describe('trackSensorById', () => {
        it('Should return tracked Sensor primary key', () => {
          const entity = { id: 123 };
          const trackResult = comp.trackSensorById(0, entity);
          expect(trackResult).toEqual(entity.id);
        });
      });
    });
  });
});
