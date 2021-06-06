import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { of } from 'rxjs';

import { TypeIotService } from '../service/type-iot.service';

import { TypeIotComponent } from './type-iot.component';

describe('Component Tests', () => {
  describe('TypeIot Management Component', () => {
    let comp: TypeIotComponent;
    let fixture: ComponentFixture<TypeIotComponent>;
    let service: TypeIotService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [TypeIotComponent],
      })
        .overrideTemplate(TypeIotComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(TypeIotComponent);
      comp = fixture.componentInstance;
      service = TestBed.inject(TypeIotService);

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
      expect(comp.typeIots?.[0]).toEqual(jasmine.objectContaining({ id: 123 }));
    });
  });
});
