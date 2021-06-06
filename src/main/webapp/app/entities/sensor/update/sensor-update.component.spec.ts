jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { SensorService } from '../service/sensor.service';
import { ISensor, Sensor } from '../sensor.model';

import { SensorUpdateComponent } from './sensor-update.component';

describe('Component Tests', () => {
  describe('Sensor Management Update Component', () => {
    let comp: SensorUpdateComponent;
    let fixture: ComponentFixture<SensorUpdateComponent>;
    let activatedRoute: ActivatedRoute;
    let sensorService: SensorService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [SensorUpdateComponent],
        providers: [FormBuilder, ActivatedRoute],
      })
        .overrideTemplate(SensorUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(SensorUpdateComponent);
      activatedRoute = TestBed.inject(ActivatedRoute);
      sensorService = TestBed.inject(SensorService);

      comp = fixture.componentInstance;
    });

    describe('ngOnInit', () => {
      it('Should update editForm', () => {
        const sensor: ISensor = { id: 456 };

        activatedRoute.data = of({ sensor });
        comp.ngOnInit();

        expect(comp.editForm.value).toEqual(expect.objectContaining(sensor));
      });
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const sensor = { id: 123 };
        spyOn(sensorService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ sensor });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: sensor }));
        saveSubject.complete();

        // THEN
        expect(comp.previousState).toHaveBeenCalled();
        expect(sensorService.update).toHaveBeenCalledWith(sensor);
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const sensor = new Sensor();
        spyOn(sensorService, 'create').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ sensor });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: sensor }));
        saveSubject.complete();

        // THEN
        expect(sensorService.create).toHaveBeenCalledWith(sensor);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).toHaveBeenCalled();
      });

      it('Should set isSaving to false on error', () => {
        // GIVEN
        const saveSubject = new Subject();
        const sensor = { id: 123 };
        spyOn(sensorService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ sensor });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.error('This is an error!');

        // THEN
        expect(sensorService.update).toHaveBeenCalledWith(sensor);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).not.toHaveBeenCalled();
      });
    });
  });
});
