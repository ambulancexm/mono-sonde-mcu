jest.mock('@angular/router');

import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of } from 'rxjs';

import { ISensor, Sensor } from '../sensor.model';
import { SensorService } from '../service/sensor.service';

import { SensorRoutingResolveService } from './sensor-routing-resolve.service';

describe('Service Tests', () => {
  describe('Sensor routing resolve service', () => {
    let mockRouter: Router;
    let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
    let routingResolveService: SensorRoutingResolveService;
    let service: SensorService;
    let resultSensor: ISensor | undefined;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        providers: [Router, ActivatedRouteSnapshot],
      });
      mockRouter = TestBed.inject(Router);
      mockActivatedRouteSnapshot = TestBed.inject(ActivatedRouteSnapshot);
      routingResolveService = TestBed.inject(SensorRoutingResolveService);
      service = TestBed.inject(SensorService);
      resultSensor = undefined;
    });

    describe('resolve', () => {
      it('should return ISensor returned by find', () => {
        // GIVEN
        service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultSensor = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultSensor).toEqual({ id: 123 });
      });

      it('should return new ISensor if id is not provided', () => {
        // GIVEN
        service.find = jest.fn();
        mockActivatedRouteSnapshot.params = {};

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultSensor = result;
        });

        // THEN
        expect(service.find).not.toBeCalled();
        expect(resultSensor).toEqual(new Sensor());
      });

      it('should route to 404 page if data not found in server', () => {
        // GIVEN
        spyOn(service, 'find').and.returnValue(of(new HttpResponse({ body: null })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultSensor = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultSensor).toEqual(undefined);
        expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
      });
    });
  });
});
