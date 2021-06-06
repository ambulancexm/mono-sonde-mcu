import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { IotDetailComponent } from './iot-detail.component';

describe('Component Tests', () => {
  describe('Iot Management Detail Component', () => {
    let comp: IotDetailComponent;
    let fixture: ComponentFixture<IotDetailComponent>;

    beforeEach(() => {
      TestBed.configureTestingModule({
        declarations: [IotDetailComponent],
        providers: [
          {
            provide: ActivatedRoute,
            useValue: { data: of({ iot: { id: 123 } }) },
          },
        ],
      })
        .overrideTemplate(IotDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(IotDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load iot on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.iot).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });
  });
});
