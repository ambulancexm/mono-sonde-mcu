import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { of } from 'rxjs';

import { SensorService } from '../service/sensor.service';

import { SensorComponent } from './sensor.component';

describe('Component Tests', () => {
  describe('Sensor Management Component', () => {
    let comp: SensorComponent;
    let fixture: ComponentFixture<SensorComponent>;
    let service: SensorService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [SensorComponent],
      })
        .overrideTemplate(SensorComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(SensorComponent);
      comp = fixture.componentInstance;
      service = TestBed.inject(SensorService);

      const headers = new HttpHeaders().append('link', 'link;link');
      spyOn(service, 'query').and.returnValue(
        of(
          new HttpResponse({
            body: [{ id: 123 }],
            headers,
          })
        )
      );
    });

    it('Should call load all on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(service.query).toHaveBeenCalled();
      expect(comp.sensors?.[0]).toEqual(jasmine.objectContaining({ id: 123 }));
    });
  });
});
