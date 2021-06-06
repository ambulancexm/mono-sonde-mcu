import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { of } from 'rxjs';

import { IotService } from '../service/iot.service';

import { IotComponent } from './iot.component';

describe('Component Tests', () => {
  describe('Iot Management Component', () => {
    let comp: IotComponent;
    let fixture: ComponentFixture<IotComponent>;
    let service: IotService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [IotComponent],
      })
        .overrideTemplate(IotComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(IotComponent);
      comp = fixture.componentInstance;
      service = TestBed.inject(IotService);

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
      expect(comp.iots?.[0]).toEqual(jasmine.objectContaining({ id: 123 }));
    });
  });
});
