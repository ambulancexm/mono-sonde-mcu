jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { TypeIotService } from '../service/type-iot.service';
import { ITypeIot, TypeIot } from '../type-iot.model';
import { IIot } from 'app/entities/iot/iot.model';
import { IotService } from 'app/entities/iot/service/iot.service';

import { TypeIotUpdateComponent } from './type-iot-update.component';

describe('Component Tests', () => {
  describe('TypeIot Management Update Component', () => {
    let comp: TypeIotUpdateComponent;
    let fixture: ComponentFixture<TypeIotUpdateComponent>;
    let activatedRoute: ActivatedRoute;
    let typeIotService: TypeIotService;
    let iotService: IotService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [TypeIotUpdateComponent],
        providers: [FormBuilder, ActivatedRoute],
      })
        .overrideTemplate(TypeIotUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(TypeIotUpdateComponent);
      activatedRoute = TestBed.inject(ActivatedRoute);
      typeIotService = TestBed.inject(TypeIotService);
      iotService = TestBed.inject(IotService);

      comp = fixture.componentInstance;
    });

    describe('ngOnInit', () => {
      it('Should call Iot query and add missing value', () => {
        const typeIot: ITypeIot = { id: 456 };
        const iot: IIot = { id: 68857 };
        typeIot.iot = iot;

        const iotCollection: IIot[] = [{ id: 43308 }];
        spyOn(iotService, 'query').and.returnValue(of(new HttpResponse({ body: iotCollection })));
        const additionalIots = [iot];
        const expectedCollection: IIot[] = [...additionalIots, ...iotCollection];
        spyOn(iotService, 'addIotToCollectionIfMissing').and.returnValue(expectedCollection);

        activatedRoute.data = of({ typeIot });
        comp.ngOnInit();

        expect(iotService.query).toHaveBeenCalled();
        expect(iotService.addIotToCollectionIfMissing).toHaveBeenCalledWith(iotCollection, ...additionalIots);
        expect(comp.iotsSharedCollection).toEqual(expectedCollection);
      });

      it('Should update editForm', () => {
        const typeIot: ITypeIot = { id: 456 };
        const iot: IIot = { id: 55253 };
        typeIot.iot = iot;

        activatedRoute.data = of({ typeIot });
        comp.ngOnInit();

        expect(comp.editForm.value).toEqual(expect.objectContaining(typeIot));
        expect(comp.iotsSharedCollection).toContain(iot);
      });
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const typeIot = { id: 123 };
        spyOn(typeIotService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ typeIot });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: typeIot }));
        saveSubject.complete();

        // THEN
        expect(comp.previousState).toHaveBeenCalled();
        expect(typeIotService.update).toHaveBeenCalledWith(typeIot);
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const typeIot = new TypeIot();
        spyOn(typeIotService, 'create').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ typeIot });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: typeIot }));
        saveSubject.complete();

        // THEN
        expect(typeIotService.create).toHaveBeenCalledWith(typeIot);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).toHaveBeenCalled();
      });

      it('Should set isSaving to false on error', () => {
        // GIVEN
        const saveSubject = new Subject();
        const typeIot = { id: 123 };
        spyOn(typeIotService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ typeIot });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.error('This is an error!');

        // THEN
        expect(typeIotService.update).toHaveBeenCalledWith(typeIot);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).not.toHaveBeenCalled();
      });
    });

    describe('Tracking relationships identifiers', () => {
      describe('trackIotById', () => {
        it('Should return tracked Iot primary key', () => {
          const entity = { id: 123 };
          const trackResult = comp.trackIotById(0, entity);
          expect(trackResult).toEqual(entity.id);
        });
      });
    });
  });
});
